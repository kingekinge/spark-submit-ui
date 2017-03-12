package controllers

import play.api.mvc.Controller
import play.api.libs.json._



object UserName extends Controller with Secured {

  def userName = IsAuthenticated { username => implicit request =>
    var shortName = username
    if (username.contains("@")){
      shortName = username.split("@")(0)
    }
    Ok(Json.obj("user"->username,"shortName"->shortName))

  }
}
