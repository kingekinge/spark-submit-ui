package models.deploy.process

import java.io.InputStream
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

import akka.actor.ActorRef
import models.{JobSubmitSuccess, MatchEngine}
import org.apache.spark.Logging
import play.api.{Logger, cache}

import scala.io.Source
import scala.util.matching.Regex
/**
  * Created by kinge on 16/7/12.
  *
  */
class LineBufferedStream(master:Option[String],act:ActorRef, inputStream: InputStream) extends Logging {

  private[this] var _lines: IndexedSeq[String] = IndexedSeq()

  private[this] val _lock = new ReentrantLock()
  private[this] val _condition = _lock.newCondition()
  private[this] var _finished = false
  private val atomicCounter = new AtomicInteger()

  def nextCount(): Int = atomicCounter.getAndIncrement()

  private val thread = new Thread {
    override def run() = {
      val lines:Iterator[String] = Source.fromInputStream(inputStream).getLines()
      val uid: String = UUID.randomUUID().toString
      val regex: Regex = MatchEngine.matchMode(master.get)

      for (line <- lines) {
        _lock.lock()
        line match {
          case regex(id) => act ! JobSubmitSuccess(id);
          case _ => Logger.info(line)
        }
        try {
          _lines = _lines :+ line
          _condition.signalAll()
        } finally {
          _lock.unlock()
        }
      }
      _lock.lock()
      try {
        _finished = true
        _condition.signalAll()
      } finally {
        _lock.unlock()
      }
    }
  }
  thread.setDaemon(true)
  thread.start()

  def lines: IndexedSeq[String] = _lines

  def iterator: Iterator[String] = {
    new LinesIterator
  }

  def waitUntilClose(): Unit = thread.join()

  private class LinesIterator extends Iterator[String] {
    private[this] var index = 0


    override def hasNext: Boolean = {
      if (index < _lines.length) {
        true
      } else {
        _lock.lock()
        try {
          if (_finished) {
            false
          } else {
            _condition.await()
            index < _lines.length
          }
        } finally {
          _lock.unlock()
        }
      }
    }

    override def next(): String = {
      val line = _lines(index)
      index += 1
      line
    }
  }
}
