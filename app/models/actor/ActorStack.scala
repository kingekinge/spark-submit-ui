package models.actor

import akka.actor.Actor

/**
  * Created by kinge on 16/7/4.
  */
trait ActorStack extends Actor {
  def wrappedReceive: Receive

  def receive: Receive = {
    case x => if (wrappedReceive.isDefinedAt(x)) wrappedReceive(x) else unhandled(x)
  }
}
