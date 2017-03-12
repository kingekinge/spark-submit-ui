package models

import play.api.libs.json.JsValue

/**
  * Created by kinge on 2017/2/21.
  */


sealed class  BaseInfo;
/**
  * RPC INFO
  * @param rec_rpc
  * @param sent_rpc
  */
case class RPCInfo(rec_rpc:JsValue,
                   sent_rpc :JsValue
                   ) extends  BaseInfo

/**
  * HDFS
  * @param capacityUsed
  * @param capacityRemaining
  * @param capacityUsedNonDFS
  */
case  class DFSInfo(capacityUsed:JsValue,
                    capacityRemaining:JsValue,
                    capacityUsedNonDFS:JsValue
                    ) extends  BaseInfo

/**
  * MEM
  * @param memHeapUsedM
  * @param memNonHeapUsedM
  */
case class MEMInfo(memHeapUsedM:JsValue,memNonHeapUsedM:JsValue) extends  BaseInfo


case class  BadInfo(msg:String)extends  BaseInfo{

  override def toString: String = this.msg


}




