package models

import akka.actor.{ActorRef, AllForOneStrategy, PoisonPill, Props, Terminated}
import akka.actor.SupervisorStrategy._
import models.actor.InstrumentedActor



sealed  trait State
object RUNNING extends State
object FINISHED extends State
object KILLED extends State
object FAILED extends State
object NEW extends  State



/**
  * Created by kinge on 16/8/31.
  */
object MessagePool {
  def props(webSocketChannel: ActorRef): Props = Props(new MessagePool(webSocketChannel))
}


class MessagePool(webSocketChannel: ActorRef) extends InstrumentedActor {

  override val supervisorStrategy = AllForOneStrategy() {
    case anyException => Stop
  }
  override def preStart()={
    context.watch(webSocketChannel)
  }



  override def wrappedReceive: Receive = {
        case RUNNING => webSocketChannel !  Send("Job is running!")
        case FINISHED => webSocketChannel ! Send("Job finish!")
        case KILLED  => webSocketChannel ! Send("Job is killed");
        case FAILED => webSocketChannel ! Send("Job failure!")


        case t: Terminated => self ! PoisonPill

 }
}