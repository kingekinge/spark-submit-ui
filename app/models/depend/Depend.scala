package models

import com.tzavellas.sse.guice.ScalaModule
import TaskDataProvider.AppDataObject
import models.metrics.{HadoopMetricsProvider, MetricsProvider}
import models.utils.{Config, Configuration}
/**
  * Created by liangkai on 16/8/22.
  */
class Depend extends ScalaModule{
  override def configure(): Unit = {
    bind[TaskDao].to[TaskInfoDao]
    bind[TaskProvider[AppDataObject]].to[TaskDataProvider]
    bind[Config].to[Configuration]
    bind[MetricsProvider].to[HadoopMetricsProvider]
    bind[Execute].toInstance(new Execute)

  }

}
