package controllers
import akka.actor.{ActorSelection, Terminated}
import models._
import org.apache.commons.lang3.StringUtils
import play.api.Play.current
import play.api._
import play.api.cache._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc._
import play.libs.Akka

import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.matching.Regex

/**
  * kinge
 */
object Authentication  extends Controller with  Secured{

  val loginForm = Form(
    tuple(
      "email" -> text.verifying("用户不存在,请先注册",User.hasUser(_)).verifying("用户还未激活",User.isActivate(_).isDefined),
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
        case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  /**
   * Logout and clean the session.
   */
  def logout = IsAuthenticated {
    username => implicit request =>
    val messagePool: ActorSelection = Akka.system.actorSelection(s"/user/MessagePool$username")
    messagePool ! Terminated
    Redirect(routes.Authentication.login).withNewSession.flashing(
      "success" -> "成功退出"
    )
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.Application.index()).withSession("email" -> user._1)
    )
  }


  val registForm = Form(
    mapping(
      "email" ->nonEmptyText.verifying("邮箱格式验证错误",validateEmail(_)).verifying("邮箱已存在",!User.findByEmail(_).isDefined),
      "name"-> nonEmptyText.verifying("姓名含有非法字符",validateName(_)),
      "password" -> nonEmptyText,
      "repassword"->nonEmptyText
    )(Registration.apply)(Registration.unapply) verifying ("两次密码不一致", result => result match {
      case  registration => validatePassword(registration)
    })
  )

  def registration =Action{
    Ok(views.html.register(registForm))
  }


  /**
    * 用户鉴权验证
    * @return
    */
  def verifying = Action { implicit request =>
    registForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      registration => {
        val user:User= User.verifying(registration = registration)
        implicit val clusterListWrites = Json.writes[User]
        Redirect("/mail?user="+Json.stringify(Json.toJson(user)))
      }
    )
  }


  /**
    * 验证邮箱
    *
    * @param email
    * @return
    */
  def validateEmail(email: String) :Boolean= {
    val regex =("""^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$""").r
    regex.findAllIn(email).hasNext
  }

  /**
    * 验证用户名
    *
    * @param name
    * @return
    */
  def validateName(name: String) :Boolean= {
    val regex: Regex = ("""^[A-Za-z0-9]+$""").r
    regex.findAllIn(name).hasNext
  }


  /**
    * 验证密码
    * @param registration
    * @return
    */
  def validatePassword(registration:Registration): Boolean ={
    StringUtils.equals(registration.password,registration.repassword)
  }


  /**
    * 发送邮件验证
    *
    * @return
    */
  def sendEmail(user:String): String ={
    val jsValue: JsValue = Json.parse(user)
    val email: Option[String] = jsValue.\("email").asOpt[String]
    val captcha: Option[String] = jsValue.\("captcha").asOpt[String]
    val name: Option[String] = jsValue.\("name").asOpt[String]
    val password: Option[String] = jsValue.\("password").asOpt[String]
    if (email.isDefined)
      if(captcha.isDefined)
        Email.sendHtmlMail(Verify(email.getOrElse(null),captcha.getOrElse(null),name.getOrElse(null)))
      else
        Email.sendHtmlMail(User(email.getOrElse(null),name.getOrElse(null),password.getOrElse(null)))
      else "发送邮件失败,请检查注册信息"
  }



  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  /**
    * 发送邮件
    * @param user
    * @return
    */
  def mail(user:String) = Action.async {
    val future: Future[String] = scala.concurrent.Future { sendEmail(user) }
    future.map(i => Ok("Got result: " + i))
  }


  /**
    * 验证激活
    * @param email
    * @param validateCode
    * @return
    */
  def verifyingmail(email:String,validateCode:String) = Action{
    Email.activateUser((email,validateCode)) match {
      case  e : EmailExecption =>  BadRequest(views.html.registed(e.unapply(e)))
      case  v : VerifyException => BadRequest(views.html.registed(v.unapply(v)))
      case  f : Failure => BadRequest(views.html.registed(f.unapply(f)))
      case  s : Success => Ok(email+" 注册成功 ~ ")
    }
  }


  /**
    * 验证验证码
    *
    * @param captcha1
    * @return
    */
  def verifyCaptcha(captcha1:String,captcha2:String):Boolean={
    Logger.info("输入:"+captcha1+"  生成:"+captcha2)
    StringUtils.equalsIgnoreCase(captcha1,captcha2)
  }


  /**
    * 找回密码
    * @return
    */
  def findpwd=Action{
    implicit request =>
      Ok(views.html.findpwd(findPasswordForm))
  }

  /**
    * 验证码
    * @return
    */
  def captcha =Action{
    implicit request =>
    val verifyCode = CaptchaUtils.generateVerifyCode(4)
      Logger.info(verifyCode)
      Cache.set("captcha",verifyCode)
      val outputImage: Array[Byte] = CaptchaUtils.outputImage(130, 52, verifyCode)
      Ok(outputImage).withHeaders("Pragma"->"No-cache","Cache-Control"->"no-cache","Expires"->"0").as("image/jpeg")
  }

  val findPasswordForm = Form(
    tuple(
      "email" -> text.verifying("用户不存在,请检查输入邮箱", User.findByEmail(_).isDefined).verifying("用户还未激活",User.isActivate(_).isDefined),
      "captcha" -> text.verifying("验证码错误",x=>{
        verifyCaptcha(x,Cache.getAs[String]("captcha").get)
      })
    )
  )

  /**
    * 发送重置密码
    * @return
    */
  def resetpwd = Action { implicit request =>
    findPasswordForm.bindFromRequest.fold(
      formWithErrors => {
        formWithErrors.errors.map(x=> Logger.info(x.message))
        formWithErrors.globalError.map(x=> Logger.info(x.message))
        BadRequest(views.html.findpwd(formWithErrors))
      },
      user => {
        val f_name: String =User.findNameByEmail(user._1)
        val name:String =if(StringUtils.isEmpty(f_name)) user._1  else f_name
        val jsObject: JsObject = Json.obj("email"->user._1,"captcha"->user._2,"name"->name)
        Redirect("/mail?user="+Json.stringify(jsObject))
      }
    )
  }


  val setPasswordForm = Form(
    tuple(
      "password" -> nonEmptyText,
      "repassword" -> nonEmptyText) verifying ("两次密码不一致", result => result match {
      case (password, repassword) => StringUtils.equals(password,repassword)
    })
  )

  /**
    * 设置新密码
    * @param email
    * @param pwdToken
    * @return
    */
  def setpwd(email:String,pwdToken:String)=Action{
    Email.activatePWD((email,pwdToken)) match {
      case  e : EmailExecption =>  NotFound
      case  v : VerifyException => NotFound
      case  f : Failure => NotFound
      case  s : Success => Ok(views.html.setpwd(setPasswordForm)).withSession("findpwd"->email)
    }
  }


  /**
    * 更新密码
    * @return
    */
  def updatepwd = Action { implicit request =>
    setPasswordForm.bindFromRequest.fold(
      formWithErrors => {
        formWithErrors.errors.map(x=> Logger.info(x.message))
        formWithErrors.globalError.map(x=> Logger.info(x.message))
        BadRequest(views.html.setpwd(formWithErrors))
      },
      user => {
        val email: String = request.session.get("findpwd").getOrElse(null)
        User.updatePWD(email,user._2)
        Ok(email+"修改密码成功")
      }
    )
  }

}
