package controllers
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.ActorRef
import com.google.inject.Inject
import models._
import models.io.{Message, MessageList, TaskMessage}
import models.metrics.MetricsProvider
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.libs.Akka
import akka.actor._
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import play.api.Logger
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.stm.{Sink, Source}

/**
 * WebSocket and Index
 */
class Application @Inject()(metricsProvider: MetricsProvider)(execute: Execute) extends Controller with Secured {


  def index = IsAuthenticated { username => implicit request =>
    val rpcInfo: RPCInfo = metricsProvider.getMetricMouled[RPCInfo](RPCInfo.getClass)
    val dfsInfo: DFSInfo = metricsProvider.getMetricMouled[DFSInfo](DFSInfo.getClass)
    val memInfo: MEMInfo = metricsProvider.getMetricMouled[MEMInfo](MEMInfo.getClass)
    Ok(views.html.index(rpcInfo,dfsInfo,memInfo,getNowDate))
  }


  def startpush =  WebSocket.using[String] { implicit request =>
    val user: String = request.session.get("email").get
    val (out,channel) = Concurrent.broadcast[String]
    val in = Iteratee.foreach[String] { msg =>
        execute.register(user,request.id.toString,channel)
    }
    (in,out)
  }






  def msglist =IsAuthenticated { username => implicit request =>
    implicit val residentWrites = Json.writes[TaskMessage]
    implicit val clusterListWrites = Json.writes[MessageList]
    val json: JsValue = Json.toJson(MessageList(Message.getMessages(username)))
    Ok(json)
  }

  def read(appId:String)=Action{
    Message.deleteMessage(appId)
    Ok(views.html.tasklist())
  }

  def readall=IsAuthenticated{ username => implicit request =>
    Message.deleteAllMessage(username)
    Ok(views.html.tasklist())
  }

  private[this] def getNowDate:String ={
    val before = 3*60*60*1000
    val currentTime = new Date(System.currentTimeMillis()-before)
    currentTime.setMinutes(0)
    val formatter = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss")
    formatter.format(currentTime)
  }





}
