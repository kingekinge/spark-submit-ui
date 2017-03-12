package controllers


import com.google.inject.Inject
import models.utils.{Config}
import play.api.mvc._

import scala.io.Source

/**
  * Created by kinge
  */
class YarnList @Inject()(conf:Config) extends Controller {

  def getYarnInfo=Action{
    Ok(Source.fromURL("http://"+conf.getString("hadoop.yarn.host")+"/ws/v1/cluster/apps").mkString)
  }

  def yarnlist=Action{
    Ok(views.html.yarnlist())
  }

}


