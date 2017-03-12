package models.metrics

import models._
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

/**
  * Created by kinge on 2016/7/23.
  * <p>metric data computing related @host:port/jmx<p>
  *
  */
private [metrics] abstract  class  Factory {
  var metrics: Seq[MetricsData] =_


  def queryMetrics[T](clz:Class[_]) :T={
    metrics = MetricsData.getMetrics()
    clz.getClass match  {
      case _=> queryMetricsModels(clz).asInstanceOf[T]
    }
  }

   def queryMetricsModels:PartialFunction[Class[_],BaseInfo] = ???

}

private[metrics] class MetricsFactory extends  Factory{

  private[this] val BYTE_NUMBER=1024


  override def queryMetricsModels:PartialFunction[Class[_],BaseInfo]={
    case x if  x == RPCInfo.getClass =>
      RPCInfo(ReceivedInterval, SentInterval)

    case x if  x == DFSInfo.getClass =>
      DFSInfo(CapacityUsed,CapacityRemaining,CapacityUsedNonDFS)

    case x if x==MEMInfo.getClass =>
      MEMInfo(MemHeapUsedM,MemNonHeapUsedM)

    case _ => BadInfo("error")
  }



  private [this] def MemNonHeapUsedM={
    val result:Seq[Double]={
      for(me<- metrics) yield (me.MemNonHeapUsedM)
    }
    Json.toJson(result.reverse)
  }


  private [this] def MemHeapUsedM ={
    val result:Seq[Double]={
      for(me<- metrics) yield (me.MemHeapUsedM)
    }
    Json.toJson(result.reverse)
  }


  private [this] def CapacityUsed = {
    val result:Seq[Long]={
      for(me<- metrics) yield (me.CapacityUsed / (BYTE_NUMBER*BYTE_NUMBER*BYTE_NUMBER))
    }
    Json.toJson(result.reverse)
  }

  private [this] def CapacityRemaining ={
    val result:Seq[Long]={
      for(me<- metrics) yield (me.CapacityRemaining  / (BYTE_NUMBER*BYTE_NUMBER*BYTE_NUMBER))
    }
    Json.toJson(result.reverse)
  }

  private [this] def CapacityUsedNonDFS  ={
    val result:Seq[Long]={
      for(me<- metrics) yield (me.CapacityUsedNonDFS / (BYTE_NUMBER*BYTE_NUMBER*BYTE_NUMBER))
    }
    Json.toJson(result.reverse)
  }



  private[this] def SentInterval  ={
    val result:Seq[Long]={
      for(i<- 0 until metrics.length)
        yield if(i==metrics.length-1){
          0
        }else{
          (metrics(i).sentBytes-metrics(i+1).sentBytes)/BYTE_NUMBER
        }
    }.dropRight(1)
    Json.toJson(result.reverse)
  }


  private[this] def ReceivedInterval ={
    val result:Seq[Long]={
      for(i<- 0 until metrics.length)
        yield if(i==metrics.length-1){
          0
        }else{
          (metrics(i).receivedBytes-metrics(i+1).receivedBytes)/BYTE_NUMBER
        }
    }.dropRight(1)
    Json.toJson(result.reverse)
  }












}
