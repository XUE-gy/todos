package com.neo.sk.todos2018.shared.ptcl

/**
  * User: XueGanYuan
  * Date: 2019/9/22
  * Time: 11:49
  */
object RegisterProtocol {

  case class UserRegisterReq(
                           userName: String,
                           password: String,
                           email :   String
                         )

  case class UserRegisterRsp(
                           errCode: Int,
                           msg: String
                         ) extends CommonRsp

}