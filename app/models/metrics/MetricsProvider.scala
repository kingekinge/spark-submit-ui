package models.metrics

/**
  * Created by kinge on 2016/6/2.
  */
trait MetricsProvider {

  def getMetricMouled[T](clz:Class[_]):T

}
