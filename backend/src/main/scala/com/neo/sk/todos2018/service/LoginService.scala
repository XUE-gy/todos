package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.ptcl.UserProtocol.UserBaseInfo
import com.neo.sk.todos2018.service.SessionBase.{SessionKeys, SessionTypeKey, ToDoListSession}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.{GetAuthorListRsp, UserInfo, UserLoginReq}
import com.neo.sk.todos2018.shared.ptcl.RegisterProtocol.UserRegisterReq
//import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: XuSiRan
  * Date: 2019/3/26
  * Time: 19:15
  */
trait LoginService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

//  private val userName = "test"
//  private val userName2 =ToDoListDAO.getAuthorList(user.userName).map { list =>
//    val data = list.map( r => TaskRecord(r.id, r.content, r.time)).toList
//    complete(GetListRsp(Some(data)))
//  }


//  private val userLogin = (path("userLogin") & post){
//    entity(as[Either[Error, UserLoginReq]]){
//      case Left(error) =>
//        log.warn(s"error in userLogin: $error")
//        complete(parseError)
//      case Right(req) =>
//        if(req.userName == userName){
//          val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
//          addSession(session.toSessionMap){
//            complete(SuccessRsp())
//          }
//        }
//        else
//          complete(ErrorRsp(10001, "用户名不正确"))
//    }
//  }

  private val userLogin = (path("userLogin") & post){
    entity(as[Either[Error, UserLoginReq]]) {
      case Left(error) =>
        log.warn(s"error in userLogin: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult {
          ToDoListDAO.getAuthorList(req.userName).map { list =>
            if (list == Vector()) complete(ErrorRsp(10001, "该用户不存在"))
            else if(list(0).password != req.password){complete(ErrorRsp(10001, "密码不正确"))}
            else{
              val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
              addSession(session.toSessionMap) {
                complete(SuccessRsp())
              }
            }
          }
        }
    }
  }


//  private val userLogin = (path("userLogin") & post){
//    entity(as[Either[Error, UserLoginReq]]){
//      case Left(error) =>
//        log.warn(s"error in userLogin: $error")
//        complete(parseError)
//      case Right(req) =>
//        if(ToDoListDAO.getAuthorList(req.userName) != null){
////          ToDoListDAO.getAuthorList(req.userName).map { list =>
////            val data = list.map( r => UserInfo(r.author, r.` password1`, r.email)).toList
////            println(GetAuthorListRsp(Some(data)))
////          }
//          var error = 0
//          ToDoListDAO.getAuthorList(req.userName).map { list =>
//            if(list==Vector()) error=10002
//            else{
//              list.map( r =>
//                if (r.password==req.password) {
////                  println("登陆成功")
//                  error=0
//                }
////                else println("密码错误")
//                else error=10003
//              )
//            }
//
//          }
//
////          println(req.userName)
////          if(ToDoListDAO.getAuthorList(req.userName)==req.password)
////          val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
////          addSession(session.toSessionMap){
////            complete(SuccessRsp())
////          }
//          println(error)
//          error match {
//            case 10002 => complete(ErrorRsp(10001, "该用户不存在"))
//            case 10003 => complete(ErrorRsp(10001, "密码错误"))
//            case _ => {
//              val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
//              addSession(session.toSessionMap){
//                complete(SuccessRsp())
//              }
//            }
//          }
//
//        }
//        else
//          complete(ErrorRsp(10001, "未填写用户名"))
//    }
//  }

//  private val userLogin2 = (path("userLogin") & post){
//
//      entity(as[Either[Error, UserRegisterReq]]){
//        case Left(error) =>
//          log.warn(s"error in userLogin: $error")
//          complete(parseError)
//        case Right(req) =>
////          if(req.userName == userName){//req.userName是网页得到的username，userName将为数据库中的username，如何实现？
////            val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
////            addSession(session.toSessionMap){
////              complete(SuccessRsp())
////            }
////          }
////          else
////            complete(ErrorRsp(10001, "用户名不正确"))
////          getAuthorList(传入名字如果失败就返回用户不存在，否则验证身份名和密码是否匹配)
//        if(ToDoListDAO.getAuthorList(req.userName) != null){
//          val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
//          addSession(session.toSessionMap){
//            complete(SuccessRsp())
//          }
//
//
//        }else{
//            log.warn(s"该用户名已经存在")
//          }
//        }
////          val data = list.map( r => TaskRecord(r.id, r.content, r.time)).toList
////          complete(GetListRsp(Some(data)))
//        }
//
//      }



//  userAuth{ user =>
//    dealFutureResult(
//      ToDoListDAO.getRecordList(user.userName).map { list =>
//        val data = list.map( r => TaskRecord(r.id, r.content, r.time)).toList
//        complete(GetListRsp(Some(data)))
//      }
//    )
//  }

  private  val userLogout = (path("userLogout") & get) {
    userAuth{ _ =>
      val session=Set(SessionTypeKey,SessionKeys.name)
      removeSession(session){ctx =>
        ctx.complete(SuccessRsp())
      }
    }
  }

  val loginRoutes: Route =
    pathPrefix("login") {
      userLogin ~ userLogout
    }

}
