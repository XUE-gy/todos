package com.neo.sk.todos2018.shared.ptcl

/**
  * User: XuSiRan
  * Date: 2019/3/26
  * Time: 19:02
  */
object LoginProtocol {

  case class UserLoginReq(
    userName: String,
    password: String,
  )

  case class UserInfo(//
                       userName: String,
                       passWord: String,
                       email : String,
                     )

  case class GetAuthorListRsp(
                         list: Option[List[UserInfo]],
                         errCode: Int = 0,
                         msg: String = "Ok"
                       ) extends CommonRsp


  case class UserLoginRsp(
    errCode: Int,
    msg: String
  ) extends CommonRsp

}
