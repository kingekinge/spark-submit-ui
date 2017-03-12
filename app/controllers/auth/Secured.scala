package controllers

import play.api.mvc._

/**
  * Provide security features
  */
trait Secured extends {

  /**
    * Retrieve the connected user's email
    */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
    * Not authorized, forward to login
    */
  private def onUnauthorized(request: RequestHeader) = {
    Results.Redirect(routes.Authentication.login)
  }

  /**
    * Action for authenticated users.
    */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}
