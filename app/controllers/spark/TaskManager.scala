package controllers

import com.google.inject.Inject
import models.TaskDataProvider.AppDataObject
import models._
import models.utils.Config
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller, Results}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Created by liangkai on 16/8/18.
  * Management related tasks run time
  */
class TaskManager @Inject() (config: Config,taskProvider:TaskProvider[AppDataObject],taskDao: TaskDao,execute: Execute) extends Controller with Secured{

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

   def tasklist=Action{
     Ok(views.html.tasklist())
   }


   def standaloneInfo =IsAuthenticated{
     username => implicit request =>
       implicit val residentWrites = Json.writes[TaskInfo]
       implicit val clusterWrites = Json.writes[TaskList]
       val json: JsValue = Json.toJson(TaskList(taskDao.getTaskInfoList(username)))
       Ok(json)
   }



  def yarnInfo =IsAuthenticated{
    username => implicit request =>

      implicit val yarnWrites: Writes[YarnTaskInfo] = (
        (__ \ "application_id").write[String] and
          (__ \ "name").write[String] and
          (__ \ "apptype").write[String] and
          (__ \ "queue").write[String] and
          (__ \ "starttime").write[Long] and
          (__ \ "state").write[String] and
          (__ \ "finishtime").write[Long]
        )(unlift(YarnTaskInfo.unapply))

      implicit val clusterWrites = Json.writes[YarnTaskList]
      val json: JsValue = Json.toJson(YarnTaskList(taskDao.getYarnTaskList(username)))
      Ok(json)

  }

  def killTask(appId:String): Unit ={
    Runtime.getRuntime.exec(s"yarn application -kill $appId")
  }


  def kill(appId:String) =IsAuthenticated{
    username => implicit request =>

      if(appId.startsWith("application")){Map("id"->Seq(appId),"terminate"->Seq("true"))
        killTask(appId)
      }else{
        val spark_master = config.getString("spark.master.host")
           WS.url("http://"+spark_master+"/app/kill/").withQueryString(("terminate","true")).withQueryString(("id",appId)).post("content") map{
          response => response.status match {
            case  200 => Some{

            }
            case _ => None
          }
        }
      }
      Ok("KILLED")
  }


  def rerun(appId:String) =IsAuthenticated{
    username => implicit request =>
      val executeModel: ExecuteModel = taskDao.getTaskArgs(appId)
      execute.main(executeModel) match {
        case JobSubmitSuccess(id) =>  {
          Logger.info(s"old Id====> $appId,new id====> $id")
          /**
            *Save the new task parameters  add to the List
            */
          taskDao.saveTaskArgs(executeModel)(id)
          taskProvider.coverTask(appId)
          taskProvider.loadTaskInfo(AppDataObject(id,username));
          Ok(id)
        }
        case JobRunExecption(error) => Ok(error)
        case _ => NotFound
      }
  }





}
