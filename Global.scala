import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent.Future
import play.api.Play.current
import com.google.inject._
import models._

object Global extends GlobalSettings {

  private lazy val injector = {
    Play.isProd match {
      case _ => {
         Guice.createInjector(new Depend)
      }
    }
  }
 

  // 500 - internal server error
  override def onError(request: RequestHeader,ex: Throwable) = {
   Future.successful(
            InternalServerError(
                views.html.errors.fzf()
            )
   )
  }
 
  // 404 - page not found error
  override def onHandlerNotFound(request: RequestHeader)= {
   Future.successful(
           NotFound(
               views.html.errors.fzf()
               )
   )
  }

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }

}
