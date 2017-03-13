package models.utils

import com.typesafe.config.ConfigFactory

/**
  * Created by kinge on 16/5/2.
  */
class Configuration extends Config{

  val configName = "web-site"

  val config = ConfigFactory.load(configName)

  override def getString(path: String): String = config.getString(path)

  override def getDouble(path: String): Double = config.getDouble(path)

  override def getLong(path: String): Long = config.getLong(path)

  override def getBoolean(path: String): Boolean = config.getBoolean(path)

  override def getInt(path: String): Int = config.getInt(path)
}
