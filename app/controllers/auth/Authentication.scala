package controllers
import models._
import org.apache.commons.lang3.StringUtils
import play.api.Play.current
import play.api._
import play.api.cache._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc._
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.matching.Regex

/**
  * kinge
 */
object Authentication  extends Controller with  Secured{

  val loginForm = Form(
    tuple(
      "email" -> text.verifying("User does not exist, please register first",User.hasUser(_)).verifying("User has not been activated",User.isActivate(_).isDefined),
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
    Redirect(routes.Authentication.login).withNewSession.flashing(
      "success" -> "Successful exit"
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
      "email" ->nonEmptyText.verifying("Email address  validation errors",validateEmail(_)).verifying("email has been in existence",!User.findByEmail(_).isDefined),
      "name"-> nonEmptyText.verifying("Name contains illegal characters",validateName(_)),
      "password" -> nonEmptyText,
      "repassword"->nonEmptyText
    )(Registration.apply)(Registration.unapply) verifying ("Passwords don't match", result => result match {
      case  registration => validatePassword(registration)
    })
  )

  def registration =Action{
    Ok(views.html.register(registForm))
  }


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

  def validateEmail(email: String) :Boolean= {
    val regex =("""^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$""").r
    regex.findAllIn(email).hasNext
  }

  def validateName(name: String) :Boolean= {
    val regex: Regex = ("""^[A-Za-z0-9]+$""").r
    regex.findAllIn(name).hasNext
  }


  def validatePassword(registration:Registration): Boolean ={
    StringUtils.equals(registration.password,registration.repassword)
  }


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
      else "Send mail failed, please check the registration information"
  }



  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def mail(user:String) = Action.async {
    val future: Future[String] = scala.concurrent.Future { sendEmail(user) }
    future.map(i => Ok(views.html.registed(Some("Got result: " + i))))
  }


  def verifyingmail(email:String,validateCode:String) = Action{
    Email.activateUser((email,validateCode)) match {
      case  e : EmailExecption =>  BadRequest(views.html.registed(e.unapply(e)))
      case  v : VerifyException => BadRequest(views.html.registed(v.unapply(v)))
      case  f : Failure => BadRequest(views.html.registed(f.unapply(f)))
      case  s : Success => Ok(views.html.registed(Some(email+" Registered successfully ~ ")))
    }
  }


  def verifyCaptcha(captcha1:String,captcha2:String):Boolean={
    StringUtils.equalsIgnoreCase(captcha1,captcha2)
  }


  def findpwd=Action{
    implicit request =>
      Ok(views.html.findpwd(findPasswordForm))
  }

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
      "email" -> text.verifying("User does not exist, please check the input mailbox", User.findByEmail(_).isDefined).verifying("User has not been activated",User.isActivate(_).isDefined),
      "captcha" -> text.verifying("verification code error",x=>{
        verifyCaptcha(x,Cache.getAs[String]("captcha").get)
      })
    )
  )

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
      "repassword" -> nonEmptyText) verifying ("Passwords don't match", result => result match {
      case (password, repassword) => StringUtils.equals(password,repassword)
    })
  )

  def setpwd(email:String,pwdToken:String)=Action{
    Email.activatePWD((email,pwdToken)) match {
      case  e : EmailExecption =>  NotFound
      case  v : VerifyException => NotFound
      case  f : Failure => NotFound
      case  s : Success => Ok(views.html.setpwd(setPasswordForm)).withSession("findpwd"->email)
    }
  }


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
        Ok(views.html.nothing(email+"Change the password successfully"))
      }
    )
  }

}
