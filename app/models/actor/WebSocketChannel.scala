package models

import akka.actor.{ActorPath, ActorRef, Props}
import models.actor.InstrumentedActor
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.{Concurrent, Enumerator}

case class Send(data:String)

/**
	* Created by liangkai on 16/8/18.
	*/
object WebSocketChannel {


	def props(channel: Concurrent.Channel[String]):Props ={
	    Props(new WebSocketChannel(channel))
	}
}

class WebSocketChannel(channel: Concurrent.Channel[String]) extends InstrumentedActor {

	override def wrappedReceive: Receive = {
		case Send(data) => channel.push(data)

	}
}