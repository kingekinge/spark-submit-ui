package models.actor

import org.slf4j.LoggerFactory

/**
  * Created by liangkai on 16/7/4.
  */
trait Slf4jLogging extends ActorStack {
  val logger = LoggerFactory.getLogger(getClass)
  private[this] val myPath = self.path.toString

  withAkkaSourceLogging {
    logger.info("Starting actor " + getClass.getName)
  }

  override def receive: Receive = {
    case x =>
      withAkkaSourceLogging {
        super.receive(x)
      }
  }

  private def withAkkaSourceLogging(fn: => Unit) {
    try {
      org.slf4j.MDC.put("akkaSource", myPath)
      fn
    } finally {
      org.slf4j.MDC.remove("akkaSource")
    }
  }
}
