package models.deploy

import akka.actor.{ActorPath, PoisonPill, Props, Terminated}
import models.actor.InstrumentedActor
import models.deploy.StatusListener.{AppFinish, PushStack}
import models.io.{Message, TaskMessage}
import models.{FAILED, KILLED, _}
import models.utils.Config
import play.api.Logger
import play.libs.Akka

import scala.concurrent.ExecutionContext

/**
  * Created by kinge on 16/7/11.
  * Task status process
  */
object StatusListener{

  var _paths =Map.empty[String,ActorPath]

  case class AppFinish(appId: String)
  case class PushStack(user:String,actorPath: ActorPath)
  case class AppRuning(msg:String)


  def props(config:Config,jobDAO: JobDAO,taskDao: TaskDao): Props = Props(classOf[StatusListener], config,jobDAO,taskDao)

}


class StatusListener(config:Config,jobDAO: JobDAO,taskDao: TaskDao) extends InstrumentedActor{



  override def wrappedReceive: Receive = {
    case AppFinish(appId) => {

      if (appId.startsWith("application")) {
        val user: String = taskDao.findyarnTaskUser(appId)
        val act = context.system.actorSelection(StatusListener._paths(user))
        taskDao.queryYarnState(appId).map {
          info =>
            Message.addMessage(TaskMessage(info.application_id, info.state, user))
            act ! Sealing(info.state);
            Logger.info(s"job finsh,current state==>" + info.state);
        }
      } else {
        val user = taskDao.findTaskUser(appId)
        val act = context.system.actorSelection(StatusListener._paths(user))
        taskDao.queryState(appId).map {
          info =>
            Message.addMessage(TaskMessage(info.app_id, info.state, user));
            act ! Sealing(info.state);
            Logger.info(s"job finsh,current state==>" + info.state);
        }
      }
    }
    case PushStack(user,actorPath) => StatusListener._paths+=(user->actorPath)

    case t: Terminated => self ! PoisonPill
  }


  def Sealing:PartialFunction[String,State] ={
    case m if(m.equals("RUNNING")) => RUNNING
    case m if(m.equals("FINISHED")) => FINISHED
    case m if(m.equals("KILLED")) => KILLED
    case m if(m.equals("FAILED")) => FAILED
    case _  => FINISHED

  }





}
