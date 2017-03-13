package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB

case class User(email: String, name: String, password: String,status:Int =0,audit:Int=0)
case class Registration(email:String,name:String,password:String,repassword:String)
case class Verify(email:String,captcha:String,name:String)
case class UserList(all:Seq[User])

object User {

  /**
    * Parse a User from a ResultSet
    */
  val simple = {
      get[String]("user.email") ~
      get[String]("user.name") ~
      get[String]("user.password")~
      get[Int]("user.status") map {
      case email ~ name ~ password ~ status  => User(email, name, password,status)
    }
  }



  /**
    * Retrieve all users.
    */
  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      play.api.db.DB.withConnection { implicit connection =>
        SQL("select * from user").as(User.simple *)
      }
    }
  }

  /**
    * Authenticate a User.
    */
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      play.api.db.DB.withConnection { implicit connection =>
        SQL(
          """
         select * from user where 
         email = {email} and password = {password}
        """).on(
          'email -> email,
          'password -> MD5Utils.encode2hex(password)).as(User.simple.singleOpt)
    }
  }
    }



  /**
    * Authenticate a User.
    */
  def hasUser(email: String): Boolean = {
    DB.withConnection { implicit connection =>
      play.api.db.DB.withConnection { implicit connection =>
        SQL(
          """
         select * from user where
         email = {email}
          """).on(
          'email -> email
          ).as(User.simple.singleOpt)
      }
    }.isDefined
  }


  def isActivate(email: String): Option[User] ={
    play.api.db.DB.withConnection { implicit connection =>
      SQL(
        """
         select * from user where
         email = {email} and status = 1
        """).on(
        'email -> email
        ).
        as(User.simple.singleOpt)
    }
  }



  def verifying(registration : Registration): User={
      val newUser = User(registration.email, registration.name,registration.password)
      create(newUser)
  }

  /**
    * Retrieve a User from email.
    */
  def findByEmail(email: String): Option[User] = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select * from user where email = {email}").on(
        'email -> email).as(User.simple.singleOpt)
    }
  }

  def findNameByEmail(email: String): String= {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select name from user where email = {email}").on(
        'email -> email).as(SqlParser.scalar[String].single)
    }
  }


  def findByStatusByEmail(email: String): Int = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL("select status from user where email = {email}").on(
        'email -> email).as(SqlParser.scalar[Int].single)
    }
  }


  def updateStatus(email:String): Int ={
    play.api.db.DB.withConnection { implicit connection =>
      SQL(
        """
          update  user set status=1 where email={email}
        """).on(
        'email -> email
        ).executeUpdate()
    }
  }


  def updatePWD(email: String, password: String): Int={
    play.api.db.DB.withConnection { implicit connection =>
      SQL(
        """
         update  user set password={password} where
         email = {email}
        """).on(
        'email -> email,
        'password -> password).executeUpdate()
    }
  }

  /**
   *
    *Create a User.
   */
  def create(user: User): User = {
    play.api.db.DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user values (
            {email}, {name}, {password},{status}
          )
        """).on(
        'email -> user.email,
        'name -> user.name,
        'password -> MD5Utils.encode2hex(user.password),
        'status->user.status
        ).executeUpdate()
      user
    }
  }





}


