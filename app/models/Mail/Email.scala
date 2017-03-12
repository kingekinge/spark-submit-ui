package models

import java.text.SimpleDateFormat
import java.util
import java.util.Date
import javax.mail.internet.InternetAddress

import models.utils.Configuration
import org.apache.commons.lang3.StringUtils
import org.apache.commons.mail.{DefaultAuthenticator, HtmlEmail, SimpleEmail}
import play.api.Logger

import scala.collection.mutable.ArrayBuffer

/**
  * Created by kinge on 16/6/16.
  */
object Email {
  val config = new Configuration

  val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd H:m")


    /**
    * send Html Mail
    * @return
    */
  def sendHtmlMail[T](user:T):String={

      val email = new HtmlEmail()
      email.setSSLOnConnect(true)
      email.setSSLCheckServerIdentity(true)
      email.setSSL(true)
      email.setHostName(config.getString("email.server.host"))
      email.setSmtpPort(config.getInt("email.default.port"))
      email.setAuthenticator(new DefaultAuthenticator(config.getString("email.default.username"),config.getString("email.default.password")))
      email.setSSLOnConnect(true)
      email.setFrom(config.getString("email.default.username"), "&spark-submit-ui")
      email.setCharset("UTF-8")
      user match {
        case u : User => {u.asInstanceOf[User];
          email.setSubject("Registered account activation");
          email.addTo(u.email) ;
          email.setHtmlMsg(makeRegisterEmail(u))}
        case v : Verify=>{v.asInstanceOf[Verify];
          email.setSubject("Reset password")
          email.addTo(v.email)
          email.setHtmlMsg(makeFindPasswordEmail(v))
        }


      }
      email.send()
      "Email activation is successful, please check the email to the mailbox"
    }


  /**
    * find password email
    * @param verify
    * @return
    */
     def makeFindPasswordEmail(verify:Verify): String ={
       val time: String = dateFormat.format(new Date(System.currentTimeMillis()))
       val validateCode=MD5Utils.encode2hex(verify.email)
       val email=verify.email
       val username=verify.name
        val hostname="http://"+config.getString("email.default.host")
       "\n<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n   " +
         " <meta charset=\"UTF-8\">\n  " +
         "  <title>Find password result</title>\n</head>\n<body>\n" +
         "<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
         "<table cellpadding=\"0\" align=\"center\" style=\"width: 600px; margin: 0px auto; " +
         "text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse;" +
         " background-position: initial initial; background-repeat: initial initial;background:#fff;\">\n<tbody>\n<tr>\n<th valign=\"middle\" style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #C46200; background-color: #FEA138; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n<font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">Spark-submit-ui</font>\n</th>\n</tr>\n<tr>\n<td>\n" +
         "<div style=\"padding:25px 35px 40px; background-color:#fff;\">\n" +
         "<h2 style=\"margin: 5px 0px; \"><font color=\"#333333\" style=\"line-height: 20px; \"><font style=\"line-height: 22px; \" size=\"4\">Dear:"+username+"</font></font></h2>\n<p>\nYou receive this email, it is because in spark-submit-ui for the new user registration, or the user to change the password to use this email address.\n" +
         "\n<p>You can use the following link to reset your password:<br/>\n <a href="+hostname+"/mail/setpwd?email="+email+"&pwdToken="+validateCode+">"+hostname+"/mail/setpwd?email="+email+"&pwdToken="+validateCode+"</a></p>\n<p align=\"right\"> spark-submit-ui</p>" +
         "\n<p align=\"right\">"+time+"</p>\n</div>\n</td>\n</tr>\n</tbody>\n</table>\n</div>\n\n\n</body>\n</html>"
     }


    /**
    * Registration activation email
    * @param user
    * @return
    */
    def makeRegisterEmail(user: User): String ={
      val validateCode=MD5Utils.encode2hex(user.email)
      val time: String = dateFormat.format(new Date(System.currentTimeMillis()))
      val email=user.email
      val username=user.name
      val adminemail=config.getString("email.default.admin")
      val hostname="http://"+config.getString("email.default.host")

      "\n<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n   " +
        " <meta charset=\"UTF-8\">\n  " +
        "  <title>Registration result</title>\n</head>\n<body>\n" +
        "<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
        "<table cellpadding=\"0\" align=\"center\" style=\"width: 600px; margin: 0px auto; " +
        "text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse;" +
        " background-position: initial initial; background-repeat: initial initial;background:#fff;\">\n<tbody>\n<tr>\n<th valign=\"middle\" style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #C46200; background-color: #FEA138; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n<font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">Spark-submit-ui</font>\n</th>\n</tr>\n<tr>\n<td>\n" +
        "<div style=\"padding:25px 35px 40px; background-color:#fff;\">\n" +
        "<h2 style=\"margin: 5px 0px; \"><font color=\"#333333\" style=\"line-height: 20px; \"><font style=\"line-height: 22px; \" size=\"4\">Dear:"+username+"</font></font></h2>\n<p>\nYou receive this email, it is because in spark-submit-ui for the new user registration, or the user to change the password to use this email address.\n\n" +
        "<p>\n\n<h2 style=\"margin: 5px 0px; \"><font color=\"#333333\" style=\"line-height: 20px; \"><font style=\"line-height: 22px; \" size=\"4\">Account activation instructions</font></font></h2>\n<p>Need to check your address validityYou can simply click on the links below to activate your account\nYou can simply click on the links below to activate your account<br/>\n <a href="+hostname+"/mail/verifyingmail?email="+email+"&validateCode="+validateCode+">"+hostname+"/mail/verifyingmail?email="+email+"&validateCode="+validateCode+"</a></p>\n\n<p/>"+"</p>\n<p align=\"right\"> spark-submit-ui</p>" +
        "\n<p align=\"right\">"+time+"</p>\n</div>\n</td>\n</tr>\n</tbody>\n</table>\n</div>\n\n\n</body>\n</html>"

    }






  /**
    * Verify the user activation information
    *
    * @return
    */
  def activateUser : PartialFunction[(String,String),EmailStatus]={
      case user if User.findByEmail(user._1).isEmpty =>  EmailExecption("User email address does not exist")
      case user if User.findByStatusByEmail(user._1) !=0 =>  VerifyException("User has been activated, and don't need to be repeated activation")
      case user if (StringUtils.equals(MD5Utils.encode2hex(User.findByEmail(user._1).get.email),user._2))
        =>{
            Logger.info(user._1+"####"+user._2)
          if(User.updateStatus(user._1)>0){
            Success("Activate the success")
          }else  Failure("Activation failed")
        }
      case _ => Failure("Activation failed, please check the registration information")
    }


  /**
    * Verify the user password information
    * @return
    */
  def activatePWD : PartialFunction[(String,String),EmailStatus]={
    case user if User.findByEmail(user._1).isEmpty =>  EmailExecption("User email address does not exist")
    case user if User.findByStatusByEmail(user._1) ==0 =>  VerifyException("User not active, need to activate")
    case user if(StringUtils.equals(MD5Utils.encode2hex(User.findByEmail(user._1).get.email),user._2))
    =>{
      Success("Authentication is successful")
    }
    case _ => Failure("Validation fails")
  }

}
