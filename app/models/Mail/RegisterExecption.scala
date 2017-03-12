package models

/**
  * Created by kinge on 16/6/17.
  */
abstract  class EmailStatus(val ex:String) extends Exception(ex){
  def unapply(arg: EmailStatus): Option[String] ={ Some(ex) }
}

case class EmailExecption(name:String) extends EmailStatus(name)
case class VerifyException(name:String) extends  EmailStatus(name)
case class  Success(name:String) extends EmailStatus(name)
case class  Failure(name:String) extends EmailStatus(name)



