package models.metrics

import akka.actor.Cancellable
import models.{BaseInfo, MetricsData, RPCInfo}
import models.TaskDataProvider.{AppDataObject, TaskData, YarnTaskInfoList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import models.utils.{Config, Configuration}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WS
import play.libs.Akka

import scala.io.{BufferedSource, Source}

/**
  * Created by kinge on 2016/6/7.
  */
class HadoopMetricsProvider extends MetricsProvider{

  private[this] val _config:Config =new Configuration
  private[this] val _factory: Factory = new MetricsFactory

   val startMetrics: Cancellable = scheduleMetricsDate



  private def getMENInfo(json:JsValue)={
    val memNonHeapUsedM = (json \\ "MemNonHeapUsedM")(0).as[Double]
    val memHeapUsedM = (json \\ "MemHeapUsedM")(0).as[Double]
    (memNonHeapUsedM,memHeapUsedM)
  }


  private def getDFSInfo(json:JsValue)={
    val CapacityUsed = (json \\ "CapacityUsed")(0).as[Long]
    val CapacityRemaining = (json \\ "CapacityRemaining")(0).as[Long]
    val CapacityUsedNonDFS=(json \\ "CapacityUsedNonDFS")(0).as[Long]
    (CapacityUsed,CapacityRemaining,CapacityUsedNonDFS)
  }



  private def getRpcInfo(json:JsValue)={
    val receivedBytes = (json \\ "ReceivedBytes")(0).as[Long]
    val sentBytes = (json \\ "SentBytes")(0).as[Long]
    (receivedBytes,sentBytes)
  }



  import scala.concurrent.duration._

  private[this]  def scheduleMetricsDate={
    Akka.system.scheduler.schedule(0.second, _config.getLong("metrics.data-update.interval-ms") millis, new Runnable {
      override def run(): Unit = {
        val url="http://"+_config.getString("hadoop.metrics.host")+":50070/jmx"
        WS.url(url).get() map{
          response => response.status match {
            case  200 => Some{

             val (receivedBytes,sentBytes) = getRpcInfo(response.json)
             val (capacityUsed,capacityRemaining,capacityUsedNonDFS)=getDFSInfo(response.json)
             val  (memNonHeapUsedM,memHeapUsedM)=getMENInfo(response.json)


              MetricsData.updateMetrics(MetricsData(
                receivedBytes,
                sentBytes,
                System.currentTimeMillis(),
                _config.getString("hadoop.metrics.host"),
                capacityUsed,
                capacityRemaining,
                capacityUsedNonDFS,
                memNonHeapUsedM,
                memHeapUsedM))
            }
            case _ => None
          }
        }

      }
    })
  }

  override def getMetricMouled[T](clz: Class[_]): T = {
       _factory.queryMetrics(clz)
  }
}
