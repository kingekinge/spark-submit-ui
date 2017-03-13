package models.io

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB

/**
  * Created by kinge on 16/9/1.
  */

case class  TaskMessage(id:String,state:String,user:String)
case class  MessageList(list:Seq[TaskMessage])
object Message {
  val taskmsg = {
    get[String]("task_msg.id") ~
      get[String]("task_msg.state") ~
      get[String]("task_msg.user") map {
      case id ~ state ~ user => TaskMessage(id,state,user)
    }
  }


  def addMessage(taskMessage: TaskMessage): TaskMessage ={
     DB.withConnection { implicit connection =>
      SQL(
        """
          insert into task_msg values (
            {id}, {state}, {user}
          )
        """).on(
        'id -> taskMessage.id,
        'state -> taskMessage.state,
        'user -> taskMessage.user
      ).executeUpdate()
    }
    taskMessage
  }

  def getMessages(user:String):Seq[TaskMessage]={
    DB.withConnection { implicit connection =>
      play.api.db.DB.withConnection { implicit connection =>
        SQL("select * from  task_msg where user={user}").on(
          'user -> user
        ).as(taskmsg *)
      }
    }
  }

  def deleteMessage(appId:String)={
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from task_msg where id={id}
        """).on(
        'id -> appId
      ).executeUpdate()
    }
  }

  def deleteAllMessage(user:String)={
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from task_msg where user={user}
        """).on(
        'user -> user
      ).executeUpdate()
    }
  }




}
