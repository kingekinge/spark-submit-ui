package models

import anorm._
import play.api.Play.current
import play.api.db.DB

/**
  * Created by kinge on 16/8/18.
  *
  *
  * "starttime" : 1471494218651,
  * "id" : "app-20160818122338-0000",
  * "name" : "HFDSWordCount",
  * "user" : "kinge",
  * "memoryperslave" : 1024,
  * "starttime" : "Thu Aug 18 12:23:38 HKT 2016",
  * "state" : "RUNNING",
  * "duration" : 17290
  */
class TaskInfoDao  extends  TaskDao{


  override def saveTask(task: TaskInfo)(user:String): TaskInfo = {
      play.api.db.DB.withConnection { implicit connection =>
        SQL(
          """
          insert into task_standalone values (
            {app_id}, {name}, {cores}, {memoryperslave},{state},{starttime},{duration},{user}
          )
          """).on(
          'app_id -> task.app_id,
          'name -> task.name,
          'cores -> task.cores,
          'memoryperslave ->task.memoryperslave,
          'state ->task.state,
          'starttime ->task.starttime,
          'duration ->task.duration,
          'user ->user
        ).executeUpdate()
      }
      task
    }

  override  def getYarnTaskList(user:String): Seq[YarnTaskInfo] = {
        DB.withConnection{
            implicit  connection =>
                SQL(
                    """
                 select * from task_yarn where user={user} order by starttime desc
                    """
                ).on( 'user -> user)
                  .as(yarn *)
        }
    }

  override def saveYarnTask(yarnTask: YarnTaskInfo)(user:String): YarnTaskInfo = {
      play.api.db.DB.withConnection { implicit connection =>
        SQL(
          """
          insert into task_yarn values (
            {application_id}, {name}, {apptype}, {queue},{starttime},{finishtime},{state},{user}
          )
          """).on(
          'application_id -> yarnTask.application_id,
          'name -> yarnTask.name,
          'apptype -> yarnTask.apptype,
          'queue -> yarnTask.queue,
          'starttime -> yarnTask.starttime,
          'finishtime ->yarnTask.finishtime,
          'state ->yarnTask.state,
          'user ->user
        ).executeUpdate()
      }
      yarnTask
    }

  override def getTaskInfoList(user:String): Seq[TaskInfo] = {
        DB.withConnection{
            implicit  connection =>
                SQL(
                    """
                 select * from task_standalone where user={user} order by starttime desc
                    """
                ).on( 'user -> user)
               .as(standalone *)
        }
    }


  override def updateYarnTaskList(tasks: Seq[YarnTaskInfo]): Unit = {
    play.api.db.DB.withConnection { implicit connection =>
      tasks.map{
        info =>
          SQL(
            """
          update task_yarn set finishtime ={finishtime},state={state} where application_id={application_id}
            """).on(
            'state -> info.state,
            'finishtime -> info.finishtime,
            'application_id -> info.application_id
          ).executeUpdate()
      }
    }
  }

  override def updateTaskList(tasks: Seq[TaskInfo]): Unit = {
    play.api.db.DB.withConnection { implicit connection =>
      tasks.map{
        info =>
          SQL(
            """
          update  task_standalone set
             state={state}, duration={duration} where app_id={app_id}
            """).on(
            'state -> info.state,
            'duration -> info.duration,
            'app_id -> info.app_id
          ).executeUpdate()
      }
    }
  }


  override def saveTaskArgs(executeModel: ExecuteModel)(appId: String): ExecuteModel = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL(
        """
          insert into task_args values (
            {id}, {master}, {executeClass}, {numExecutors},{driverMemory},{executorMemory},{total_executor_cores},{jarLocation},{args1}
          )
        """).on(
        'id -> appId,
        'master -> executeModel.master,
        'executeClass -> executeModel.executeClass,
        'numExecutors -> executeModel.numExecutors,
        'driverMemory -> executeModel.driverMemory,
        'executorMemory ->executeModel.executorMemory,
        'total_executor_cores ->executeModel.total_executor_cores,
        'jarLocation -> executeModel.jarLocation,
        'args1 ->executeModel.args1
      ).executeUpdate()
    }
    executeModel
  }

  override def getTaskArgs(appId: String): ExecuteModel = {
    DB.withConnection{
      implicit  connection =>
        SQL(
          """
            select * from task_args where id={id}
          """
        ).on( 'id -> appId)
          .as(args.single)
    }
  }

  override def rmYarnTaskInfo(appId: String): Unit = {
    DB.withConnection{
      implicit  connection =>
        SQL(
          """
            delete  from task_yarn where application_id={application_id}
          """
        ).on('application_id -> appId)
          .executeUpdate()
    }

  }

  override def rmTaskInfo(appId: String): Unit = {
    DB.withConnection{
      implicit  connection =>
        SQL(
          """
            delete from task_standalone where app_id={app_id}
          """
        ).on('app_id -> appId)
          .executeUpdate()
    }
  }

  override def queryState(appId: String): Option[TaskInfo] = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select * from task_standalone where app_id = {app_id}").on(
        'app_id -> appId).as(standalone.singleOpt)
     }
    }


  override def queryYarnState(appId: String): Option[YarnTaskInfo] = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select * from task_yarn where application_id = {application_id}").on(
        'application_id -> appId).as(yarn.singleOpt)
    }
  }

  override def findTaskUser(appId: String): String = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select user from task_standalone where app_id = {app_id}").on(
        'app_id -> appId).as(SqlParser.scalar[String].single)
    }
  }

  override def findyarnTaskUser(appId: String): String = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select user from task_yarn where application_id = {application_id}").on(
        'application_id -> appId).as(SqlParser.scalar[String].single)
    }
  }
}
