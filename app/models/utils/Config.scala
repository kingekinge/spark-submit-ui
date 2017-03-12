package models.utils

/**
  * Created by kinge on 16/5/2.
  */
trait Config {


  def getString(path:String):String

  def getBoolean(path:String):Boolean

  def getLong(path:String):Long

  def getInt(path:String):Int

  def getDouble(path:String):Double





}
