package models
import java.io.File

import org.joda.time.DateTime
import play.api.db.DB
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import anorm._
import com.google.inject.Inject
import models.utils.Config
import play.api.Play.current

import scala.collection.mutable

/**
  * Created by kinge on 16/7/11.
  */
class JobFileDAO(config: Config) extends JobDAO {


  private val rootDir = config.getString("job.upload.path");
  private val rootDirFile = new File(rootDir)
  private  val  rootOV = true
  private val apps = mutable.HashMap.empty[String, Seq[DateTime]]


  init

  private def init() {
    if (!rootDirFile.exists()) {
      if (!rootDirFile.mkdirs()) {
        throw new RuntimeException("Could not create directory " + rootDir)
      }
    }
  }


  override def saveJar(userName: String, uploadTime: DateTime, filePart: FilePart[TemporaryFile]):String= {
    val jobName:String=filePart.filename
    val file: File = new File(s"$rootDir$jobName")
    filePart.ref.moveTo(file,rootOV)
      addJar(jobName,uploadTime)
    file.getAbsoluteFile.getAbsolutePath
  }

  private def addJar(jobName: String, uploadTime: DateTime) {
    if (apps.contains(jobName)) {
      apps(jobName) = uploadTime +: apps(jobName)
    } else {
      apps(jobName) = Seq(uploadTime)
    }
  }



}
