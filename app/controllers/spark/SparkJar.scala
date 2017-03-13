package controllers



import com.google.inject.Inject
import models.JobManagerActor.{InvalidJar, JarStored}
import models.TaskDataProvider.AppDataObject
import models._
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._


/**
  * Created by kinge on 16/6/22.
  */
class SparkJar @Inject() (taskDao: TaskDao,taskProvider: TaskProvider[AppDataObject],execute: Execute) extends Controller with Secured {

  val executeForm:Form[ExecuteModel] = Form{
    mapping (
      "master"->text,
      "executeClass"->text,
      "numExecutors"->text,
      "driverMemory"->text,
      "executorMemory"->text,
      "total-executor-cores"->text,
      "jarLocation"->text,
      "args1"->text
      )(ExecuteModel.apply)(ExecuteModel.unapply)
    }


    def upload = Action(parse.multipartFormData) { implicit request =>
      request.body.file("file").map { jobFile =>
         session.get("email").map { user =>
           Logger.info("username=>"+user)
           execute.storeJar(user,jobFile) match {
            case JarStored(uri) => Redirect(routes.SparkJar.executejarpage())
            case InvalidJar(error) => Logger.info(error); BadRequest(error)
            case _ => NotFound
          }
        }.getOrElse {
          Unauthorized("The user does not exist, please login again!")
        }
        }.getOrElse {
          Redirect(routes.SparkJar.errorpage("Upload failed"))
        }
      }

      def executejarpage = Action { implicit request =>
        Ok(views.html.upload(executeForm))
      }

      def executejar = IsAuthenticated { username => implicit request =>
        executeForm.bindFromRequest.fold(
          formWithErrors => {
            formWithErrors.errors.map(x => Logger.info(x.message))
            formWithErrors.globalError.map(x => Logger.info(x.message))
            BadRequest(views.html.error(formWithErrors.toString))
          },
          executeArguments => {
          Logger.info("execution mode=>"+executeArguments.master)
            execute.main(executeArguments)
          match {
              case JobSubmitSuccess(id) =>  {
                taskDao.saveTaskArgs(executeArguments)(id)
                taskProvider.loadTaskInfo(AppDataObject(id,username))
                Ok("Task submitted successfully!")
              }
              case JobRunExecption(error) => Ok(error);
              case _ => NotFound
            }
       }
     )
}


    def errorpage(error:String) =Action {
      Ok(views.html.error(error))
    }


 }






