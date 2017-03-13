package models.deploy

/**
  * Created by kinge on 16/7/13.
  */
class CreateBatchRequest {

  var master :Option[String]=None
  var file: String = _
  var proxyUser: Option[String] = None
  var args: List[String] = List()
  var className: Option[String] = None
  var jars: List[String] = List()
  var pyFiles: List[String] = List()
  var files: List[String] = List()
  var driverMemory: Option[String] = None
  var driverCores: Option[String] = None
  var executorMemory: Option[String] = None
  var executorCores: Option[String] = None
  var total_executor_cores:Option[String] =None
  var numExecutors: Option[String] = None
  var jarLocation :Option[String] =None
  var archives: List[String] = List()
  var queue: Option[String] = None
  var name: Option[String] = None
  var conf: Map[String, String] = Map()

}

object CreateBatchRequest{
  def apply(): CreateBatchRequest = new CreateBatchRequest()
}