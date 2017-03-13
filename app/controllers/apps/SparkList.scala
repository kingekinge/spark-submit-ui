package controllers

import play.api.mvc.{Action, BodyParsers, Controller}
import com.google.inject.Inject
import models.TaskDataProvider.{AppDataObject, SparkTaskList, TaskData, YarnTaskInfoList}
import models.TaskInfo
import models.utils.Config
import play.api.libs.json._

import scala.language.postfixOps
import scala.io.Source

/**
  * Created by kinge
  */
class SparkList @Inject()(conf:Config) extends Controller {


  def getSparkInfo=Action{
    Ok(Source.fromURL("http://"+conf.getString("spark.master.host")+"/json").mkString)
  }

  def sparklist=Action{
    Ok(views.html.sparklist())
  }



  import models.TaskDataProvider.taskListReads
  implicit val taskWrites = Json.writes[TaskInfo]
  implicit val residentWrites = Json.writes[SparkTaskList]


  def getSaprkApps=Action{
   val json= Source.fromURL("http://"+conf.getString("spark.master.host")+"/json").mkString
   val taskData: TaskData = Json.parse(json).as[TaskData]
    val apps: Seq[TaskInfo] = taskData.activeapps ++ taskData.completedapps
    Ok(Json.toJson(SparkTaskList(apps)))
  }




}
