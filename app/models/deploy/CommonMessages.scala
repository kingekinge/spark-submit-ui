package models

/**
  * Created by kinge on 16/7/11.
  *
  */
sealed trait CommonMessages

/** Task submitted abnormal */
case class JobSubmitExecption(msg:String) extends  RuntimeException(msg) with CommonMessages

/** End of the task to run */
case class JobRunFinish(msg:String) extends  RuntimeException(msg) with CommonMessages

/** Task to run abnormal */
case class JobRunExecption(msg:String) extends  RuntimeException(msg) with CommonMessages

/** Task submitted successfully */
case class JobSubmitSuccess(msg:String) extends  RuntimeException(msg) with CommonMessages


