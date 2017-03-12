package models

import anorm.SqlParser._
import anorm.~


/**
  * Created by kinge on 16/8/22.
  */
case class TaskInfo(
                     app_id:String,
                     name:String,
                     cores: Option[Int],
                     memoryperslave:Long,
                     state:String,
                     starttime:Long,
                     duration:Long
                   )

case class YarnTaskInfo(
                         application_id:String,
                         name:String,
                         apptype:String,
                         queue:String,
                         starttime:Long,
                         state:String,
                         finishtime:Long
                       )
case class YarnTaskList(list:Seq[YarnTaskInfo])
case class TaskList(list:Seq[TaskInfo])


trait TaskDao{

  val yarn = {
      get[String]("task_yarn.application_id") ~
      get[String]("task_yarn.name") ~
      get[String]("task_yarn.apptype")~
      get[String]("task_yarn.queue") ~
      get[Long]("task_yarn.starttime")~
      get[String]("task_yarn.state")~
      get[Long]("task_yarn.finishtime") map {
      case application_id ~ name ~ apptype ~ queue ~starttime ~state~finishtime=> YarnTaskInfo(application_id,name,apptype,queue,starttime,state,finishtime)
    }
  }

  val standalone = {
    get[String]("task_standalone.app_id") ~
      get[String]("task_standalone.name") ~
      get[Int]("task_standalone.cores")~
      get[Long]("task_standalone.memoryperslave")~
      get[String]("task_standalone.state") ~
      get[Long]("task_standalone.starttime")~
      get[Long]("task_standalone.duration") map {
      case app_id ~ name ~ cores ~ memoryperslave ~ state ~ starttime ~ duration  => TaskInfo(app_id,name,Some(cores),memoryperslave,state,starttime,duration)
    }
  }


  val args = {
      get[String]("task_args.master") ~
      get[String]("task_args.executeClass") ~
      get[String]("task_args.numExecutors")~
      get[String]("task_args.driverMemory")~
      get[String]("task_args.executorMemory") ~
      get[String]("task_args.total_executor_cores") ~
      get[String]("task_args.jarLocation")~
      get[String]("task_args.args1") map {
      case master ~ executeClass ~ numExecutors ~ driverMemory ~ executorMemory ~ total_executor_cores ~ jarLocation ~ args1 => ExecuteModel(master,executeClass,numExecutors,driverMemory,executorMemory,total_executor_cores,jarLocation,args1)
    }
  }


  def saveTaskArgs(executeModel: ExecuteModel)(appId:String) : ExecuteModel


  def getTaskArgs(appId:String):ExecuteModel


  def saveTask(task: TaskInfo)(user:String): TaskInfo

  def saveYarnTask(yarnTask: YarnTaskInfo)(user:String): YarnTaskInfo

  def getTaskInfoList(user:String): Seq[TaskInfo]

  def getYarnTaskList(user:String): Seq[YarnTaskInfo]

  def updateYarnTaskList(tasks: Seq[YarnTaskInfo])

  def updateTaskList(tasks:Seq[TaskInfo])

  def queryYarnState(appId:String):Option[YarnTaskInfo]

  def queryState(appId:String):Option[TaskInfo]

  def rmYarnTaskInfo(appId:String)

  def rmTaskInfo(appId:String)

  def findTaskUser(appId:String):String

  def findyarnTaskUser(appId:String):String









}