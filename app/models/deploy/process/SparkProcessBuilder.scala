
package models.deploy.process

import akka.actor.ActorRef
import org.apache.spark.Logging
import play.api.Logger

import scala.collection.JavaConverters._
import scala.collection.immutable.HashMap.HashMap1
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
  * Created by kinge on 16/7/12.
  */
class SparkProcessBuilder(act:ActorRef) extends Logging {

  private[this] var _executable: String = _
  private[this] var _master: Option[String] = None
  private[this] var _deployMode: Option[String] = None
  private[this] var _className: Option[String] = None
  private[this] var _executor_memory: Option[String]=None
  private[this] var _num_executors: Option[String] = None
  private[this] var _driver_memory: Option[String] = None
  private[this] var _total_executor_cores: Option[String] = None
  private[this] var _name: Option[String] = Some("app")
  private[this] var _jarLocation:Option[String] =None
  private[this] var _queue: Option[String] = None
  private[this] var _redirectOutput: Option[ProcessBuilder.Redirect] = None
  private[this] var _redirectError: Option[ProcessBuilder.Redirect] = None
  private[this] var _redirectErrorStream: Option[Boolean] = None

  def jarLocation(jarLocation:String) :SparkProcessBuilder ={
    _jarLocation =Some(jarLocation)
    this
  }

  def executable(executable: String): SparkProcessBuilder = {
    _executable = executable
    this
  }

  def master(masterUrl: String): SparkProcessBuilder = {
    _master = Some(masterUrl)
    this
  }

  def deployMode(deployMode: String): SparkProcessBuilder = {
    _deployMode = Some(deployMode)
    this
  }

  def className(className: String): SparkProcessBuilder = {
    _className = Some(className)
    this
  }

  def name(name: String): SparkProcessBuilder = {
    _name = Some(name)
    this
  }

  def executorCores(executorCores: String): SparkProcessBuilder = {
    _total_executor_cores = Some(executorCores)
    this
  }

  def executorMemory(executorMemory: String): SparkProcessBuilder = {
    _executor_memory =Some(executorMemory)
    this
  }

  def numExecutors(numExecutors: String): SparkProcessBuilder = {
    _num_executors=Some(numExecutors)
    this
  }

  def driverMemory(driverMemory: String): SparkProcessBuilder = {
    _driver_memory=Some(driverMemory)
    this
  }

  def queue(queue: String): SparkProcessBuilder = {
    _queue = Some(queue)
    this
  }

  def redirectOutput(redirect: ProcessBuilder.Redirect): SparkProcessBuilder = {
    _redirectOutput = Some(redirect)
    this
  }

  def redirectError(redirect: ProcessBuilder.Redirect): SparkProcessBuilder = {
    _redirectError = Some(redirect)
    this
  }

  def redirectErrorStream(redirect: Boolean): SparkProcessBuilder = {
    _redirectErrorStream = Some(redirect)
    this
  }

  def start(file: Option[String], args: Traversable[String]): LineBufferedProcess = {
    executable(file.get)
    var arguments = ArrayBuffer[String](_executable)

    def addOpt(option: String, value: Option[String]): Unit = {
      value.foreach { v =>
        arguments += option
        arguments += v
      }
    }

    def addArg(value: Option[String]): Unit = {
      value.foreach{
        v=> arguments += v
      }
    }

    def addList(values:Traversable[String]): Unit ={
      if(values.nonEmpty){
        values.foreach{ v =>
          arguments += v
        }
      }
    }


    addOpt("--master", _master)
    addOpt("--class", _className)
    addOpt("--num-executors",_num_executors)
    addOpt("--executor-memory", _executor_memory)
    addOpt("--driver-memory",_driver_memory)
    addOpt("--total-executor-cores", _total_executor_cores)
    addArg(_jarLocation)
    addList(args)
    Logger.info(s"args $arguments")

    val pb = new ProcessBuilder(arguments.asJava)
    pb.redirectErrorStream(true)
    pb.redirectInput(java.lang.ProcessBuilder.Redirect.PIPE)
    val env = pb.environment()

    env.put("TEST_CLASSPATH", sys.props("java.class.path"))

    _redirectOutput.foreach(pb.redirectOutput)
    _redirectError.foreach(pb.redirectError)
    _redirectErrorStream.foreach(pb.redirectErrorStream)

    val process: LineBufferedProcess = new LineBufferedProcess(_master,act,pb.start())
    process.waitFor()
    process
  }

}
