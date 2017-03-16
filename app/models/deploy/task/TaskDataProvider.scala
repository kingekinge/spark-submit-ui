package models

import com.google.inject.Inject
import models.TaskDataProvider.{AppDataObject, TaskData, YarnTaskInfoList}
import models.utils.Config
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.libs.Akka

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Created by kinge on 16/8/18.
  */
object TaskDataProvider{

  import play.api.libs.functional.syntax._
  import play.api.libs.json.Reads._
  import play.api.libs.json._


  /**
  TaskInfo(
                     app_id:String,
                     name:String,
                     cores: Int,
                     memoryperslave:String,
                     state:String,
                     starttime:String,
                     duration:String
                   )
    */
  implicit val standaloneReads: Reads[TaskInfo] = (
    (__ \ "id").read[String] and
      (__ \ "name").read[String] and
      (__ \ "cores").readNullable[Int] and
      (__ \ "memoryperslave").read[Long] and
      (__ \ "state").read[String] and
      (__ \ "starttime").read[Long] and
      (__ \ "duration").read[Long]
    )(TaskInfo.apply _)



  implicit val taskListReads: Reads[TaskData] = (
    (__ \ "activeapps").read[Seq[TaskInfo]] and
      (__ \ "completedapps").read[Seq[TaskInfo]]
    )(TaskData.apply _)



  /**
    * YarnTaskInfo(
                     application_id:String,
                     name:String,
                     apptype:String,
                     queue:String,
                     starttime:String,
                     finishtime:String,
                     state:String,
                   )
    */
  implicit val yarnReads: Reads[YarnTaskInfo] = (
    (__  \ "id").read[String] and
      (__  \ "name").read[String] and
      (__  \ "applicationType").read[String] and
      (__  \ "queue").read[String] and
      (__  \ "startedTime").read[Long] and
      (__  \ "state").read[String] and
      (__  \ "finishedTime").read[Long]
    )(YarnTaskInfo.apply _)



  implicit  val areads = (__ \ 'apps \ 'app).read[Seq[YarnTaskInfo]].map{ l => YarnTaskInfoList(l) }

  case class SparkTaskList(apps:Seq[TaskInfo])

  case class  TaskData(activeapps:Seq[TaskInfo],completedapps:Seq[TaskInfo])

  case class AppDataObject(var appId:String,var user:String,var url:String = "yarn")

  case class YarnTaskInfoList(app: Seq[YarnTaskInfo])
}

class TaskDataProvider @Inject()(config: Config,taskDao: TaskDao)extends TaskProvider[AppDataObject]{


  scheduleTaskDate

  import TaskDataProvider.standaloneReads
  import TaskDataProvider.taskListReads
  import TaskDataProvider.yarnReads
  import TaskDataProvider.areads



  def loadTaskInfo(app:AppDataObject): Unit ={
    val appId: String = app.appId
    Logger.info(s"user app Id====>$appId")
    MatchEngine.matchURI(appId).map(
      data=>
      data._1 match {
        case m if m.equals("yarn") => app.url=data._2;proTaskOnYarn(app)
        case m if m.equals("standalone") => app.url=data._2;proTaskOnMaster(app)
        //case m if m.equals("local") => saveTaskOnLocal(m,appId)
      }
    )
  }



  override  def proTaskOnMaster(app: AppDataObject): Unit ={
     WS.url(app.url).get() map{
      response => response.status match {
        case  200 => Some{
          response.json .validate[TaskData].fold(
            invalid = {
              fieldErrors => fieldErrors.foreach(x => {
                Logger.error("field: " + x._1 + ", errors: " + x._2)
              })
                None
            },
            valid = {
              tasks => {
                  val runingTask=tasks.activeapps.filter(_.app_id.equals(app.appId))(0)
                  taskDao.saveTask(runingTask)(app.user)
              }
            })
        }
        case _ => None
      }
    }
  }



  override  def proTaskOnYarn(app:AppDataObject): Unit ={
    WS.url(app.url).get() map{
      response => response.status match {
        case  200 => Some{
          response.json .validate[YarnTaskInfoList].fold(
            invalid = {
              fieldErrors => fieldErrors.foreach(x => {
                Logger.error("field: " + x._1 + ", errors: " + x._2)
              })
                None
            },
            valid = {
              tasks => {
                 val runingTask= tasks.app.filter(_.application_id.equals(app.appId))(0)
                  taskDao.saveYarnTask(runingTask)(app.user)
              }
            })
        }
        case _ => None
      }
    }
  }

  import scala.concurrent.duration._

  def scheduleTaskDate={

    Akka.system.scheduler.schedule(0.second, config.getLong("task.data-update.interval-ms") millis, new Runnable {
      override def run(): Unit = {
        WS.url("http://"+config.getString("hadoop.yarn.host")+"/ws/v1/cluster/apps").get() map{
          response => response.status match {
            case  200 => Some{
              response.json .validate[YarnTaskInfoList].fold(
                invalid = {
                  fieldErrors => fieldErrors.foreach(x => {
                    Logger.error("field: " + x._1 + ", errors: " + x._2)
                  })
                    None
                },
                valid = {
                  tasks => {
                      taskDao.updateYarnTaskList(tasks.app)
                  }
                })
            }
            case _ => None
          }
        }

        val url: String = "http://"+config.getString("spark.master.host")+"/json"
        WS.url(url).get() map{
          response => response.status match {
            case  200 => Some{
              response.json .validate[TaskData].fold(
                invalid = {
                  fieldErrors => fieldErrors.foreach(x => {
                    Logger.error("field: " + x._1 + ", errors: " + x._2)
                  })
                    None
                },
                valid = {
                  tasks => {
                    val taskInfoes = tasks.activeapps++tasks.completedapps
                    taskDao.updateTaskList(taskInfoes)

                  }
                })
            }
            case _ => None
          }
        }

      }
    })
  }


  def coverTask(appId:String): Unit ={
    MatchEngine.matchURI(appId).map(
      data=>
        data._1 match {
          case m if m.equals("yarn") => {
            WS.url(s"http://${config.getString("hadoop.yarn.host")}/ws/v1/cluster/apps/${appId}/state").withHeaders("Content-Type"->"application/json").put(Json.obj("state"->"KILLED"))
            taskDao.rmYarnTaskInfo(appId)
          }
          case m if m.equals("standalone") => {
            WS.url("http://"+config.getString("spark.master.host")+"/app/kill/").post(Map("id" -> Seq(appId),"terminate"->Seq("true")))
            taskDao.rmTaskInfo(appId)
          }
        }
    )
  }





}
