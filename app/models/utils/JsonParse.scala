package models.utils

import com.fasterxml.jackson.databind.ObjectMapper
import play.libs.Json

import scala.collection.mutable.ArrayBuffer

/**
 * Created by kinge on 16/8/24.
 */
object JsonParse {

  def jsonParse(jobQueueList :ArrayBuffer[Any]):String = {

    val mapper = new ObjectMapper()
    mapper.registerModule(com.fasterxml.jackson.module.scala.DefaultScalaModule)
    Json.setObjectMapper(mapper)

      val resultCSV = Json.toJson(
        Map[String, Any](
          "jobs" -> jobQueueList
        )
      ).toString
    resultCSV
  }
}
