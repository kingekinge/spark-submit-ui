package models

import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.{ActorPath, ActorRef, ActorSelection, ActorSystem, Props}
import akka.util.Timeout
import models.JobManagerActor.{Initializ, StoreJar, SubmitJob}
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import akka.pattern.ask
import models.deploy.StatusListener.PushStack
import models.deploy.{CreateBatchRequest, StatusListener}
import models.utils.{Config, Configuration}
import play.api.libs.iteratee.Concurrent

import scala.concurrent.duration.Duration
import scala.concurrent._
/**
  * Created by kinge on 16/4/21.
  */
class Execute {

  private[this] var _actorSystem :ActorSystem= _
  private[this] var _jobMange:ActorRef= _
  private[this] val _config:Config =new Configuration
  private[this] val _task_dao :TaskDao =new TaskInfoDao
  private[this] val _dao: JobFileDAO = new JobFileDAO(_config)
  private[this] var _statusLister :ActorRef =_

  def makeSystem ={
    _actorSystem = ActorSystem("jobSystem")
    _statusLister=_actorSystem.actorOf(StatusListener.props(_config,_dao,_task_dao),"StatusLinster")
    _jobMange=_actorSystem.actorOf((JobManagerActor.props(_config,_dao,_task_dao,_statusLister)), "JobManger")
    _actorSystem.registerOnTermination(System.exit(0))
  }

  makeSystem


  def register(user:String,id:String,channel: Concurrent.Channel[String])={
          val webSocketChannel = _actorSystem.actorOf(WebSocketChannel.props(channel))
          val actorPath: ActorPath = _actorSystem.actorOf(MessagePool.props(webSocketChannel),s"UserActor_$id").path
         _statusLister ! PushStack(user,actorPath)
  }


  private def getRequest(executeModel: ExecuteModel): CreateBatchRequest ={
    val timeoutSecs: Long = _config.getLong("job.request.timeout.seconds")
    Await.result( (_jobMange ? Initializ(executeModel))(Timeout(timeoutSecs,TimeUnit.SECONDS)) ,
      new Timeout(Duration.create(timeoutSecs,"seconds")).duration).asInstanceOf[CreateBatchRequest]
  }


   def main(executeModel: ExecuteModel)={
    val timeoutSecs: Long = _config.getLong("job.submit.timeout.seconds")
      Await.result(
      (_jobMange ? SubmitJob(getRequest(executeModel)))
      (Timeout(timeoutSecs,TimeUnit.SECONDS)),
      new Timeout(Duration.create(timeoutSecs,"seconds")).duration)
}


  def storeJar(userName:String,filePart: FilePart[TemporaryFile]):Any = {
    val timeoutSecs: Long = _config.getLong("job.upload.timeout.seconds")
    Await.result( (_jobMange ? StoreJar(userName,filePart))
      (Timeout(timeoutSecs,TimeUnit.SECONDS)),
      new Timeout(Duration.create(timeoutSecs,"seconds")).duration);
  }



}