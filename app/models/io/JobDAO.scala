package models

import org.joda.time.DateTime
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

/**
  * Created by kinge on 16/7/11.
  */
case class JarInfo(userName: String, uploadTime: String, location:String)


trait JobDAO {

  def saveJar(userName: String, uploadTime: DateTime, filePart: FilePart[TemporaryFile]):String


}


