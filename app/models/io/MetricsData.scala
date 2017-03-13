package models

import anorm.SqlParser._
import anorm._
import models.utils.Configuration
import play.api.db.DB
import play.api.Play.current

/**
  * Created by kinge on 2017/2/17.
  */
case class MetricsData(
 receivedBytes:Long,
 sentBytes:Long,
 timestamp:Long,
 host:String,
 CapacityUsed:Long,
 CapacityRemaining:Long,
 CapacityUsedNonDFS:Long,
 MemNonHeapUsedM:Double,
 MemHeapUsedM:Double
                      )

object MetricsData {

  val config: Configuration = new Configuration

  val metricsData ={
      get[Long]("hadoop_metrics.ReceivedBytes")~
      get[Long]("hadoop_metrics.SentBytes") ~
      get[Long]("hadoop_metrics.timestamp") ~
      get[String]("hadoop_metrics.host") ~
      get[Long]("hadoop_metrics.CapacityUsed") ~
      get[Long]("hadoop_metrics.CapacityRemaining") ~
      get[Long]("hadoop_metrics.CapacityUsedNonDFS") ~
      get[Double]("hadoop_metrics.MemNonHeapUsedM") ~
        get[Double]("hadoop_metrics.MemHeapUsedM") map {
      case  receivedBytes ~
        sentBytes ~
        timestamp ~
        host ~
        capacityUsed ~
        capacityRemaining ~
        capacityUsedNonDFS ~
        memNonHeapUsedM ~
        memHeapUsedM
        =>
        MetricsData(receivedBytes,sentBytes,timestamp,host,capacityUsed,capacityRemaining,capacityUsedNonDFS,memNonHeapUsedM,memHeapUsedM)
    }
  }

  def updateMetrics(metricsData: MetricsData)={
    play.api.db.DB.withConnection { implicit connection =>
      SQL(
        """
          insert into hadoop_metrics values (
            {ReceivedBytes},{SentBytes},{host},{CapacityUsed},{CapacityRemaining},{CapacityUsedNonDFS},{MemHeapUsedM},{MemNonHeapUsedM},{timestamp}
          )
        """).on(
        'ReceivedBytes -> metricsData.receivedBytes,
        'SentBytes -> metricsData.sentBytes,
        'host -> metricsData.host,
        'CapacityUsed -> metricsData.CapacityUsed,
        'CapacityRemaining -> metricsData.CapacityRemaining,
        'CapacityUsedNonDFS -> metricsData.CapacityUsedNonDFS,
        'MemNonHeapUsedM -> metricsData.MemNonHeapUsedM,
        'MemHeapUsedM -> metricsData.MemHeapUsedM,
        'timestamp -> metricsData.timestamp
      ).executeUpdate()
    }
    metricsData
  }


    def getMetrics():Seq[MetricsData]={
      DB.withConnection { implicit connection =>
        play.api.db.DB.withConnection { implicit connection =>
          SQL("select * from hadoop_metrics where host = {host} order by timestamp desc  limit 0,36 ")
              .on('host -> config.getString("hadoop.metrics.host")).as(metricsData *)
        }
      }
    }






}
