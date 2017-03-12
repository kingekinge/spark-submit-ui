package models.actor


/**
  * Created by liangkai on 16/7/4.
  */
abstract class InstrumentedActor extends ActorStack with Slf4jLogging  {

  override def preRestart(reason: Throwable, message: Option[Any]) {
    super.preRestart(reason, message)
  }

  override def postStop() { logger.warn(getClass.getName) }
}
