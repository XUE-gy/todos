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
import com.neo.sk.todos2018.shared.ptcl.RegisterProtocol.UserRegisterReq
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: XueGanYuan
  * Date: 2019/9/22
  * Time: 15:56
  */
trait RegisterService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val userName = "test"

//  private val userRegister = (path("userRegister") & post){//to Register,then get into Login page
//    entity(as[Either[Error, UserRegisterReq]]){
//      case Left(error) =>
//        log.warn(s"error in userRegister: $error")
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

  private val userRegister = (path("userRegister") & post){
    entity(as[Either[Error, UserRegisterReq]]) {
      case Left(error) =>
        log.warn(s"error in userLogin: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult {
          ToDoListDAO.getAuthorList(req.userName).map { list =>
            if (list == Vector()) {
              if(req.password == ""){complete(ErrorRsp(10001, "没有输入密码，不能注册"))}
              else if(req.email == ""){complete(ErrorRsp(10001, "没有输入邮箱，不能注册"))}
              else{
                ToDoListDAO.addAuthor(req.userName,req.password,req.email)
//                println("req.userName="+req.userName+"req.password="+req.password)
                complete(SuccessRsp())
              }
            }
            else {/*println(list);*/complete(ErrorRsp(10001, "数据库已有用户，不能注册"))}
          }
        }
    }
  }









  private val userRegisterIn = (path("userRegisterIn") & post){//get into Register page
    userAuth{ _ =>
      val session=Set(SessionTypeKey,SessionKeys.name)
      removeSession(session){ctx =>
        ctx.complete(SuccessRsp())
      }
    }
  }

  private  val userRegisterOut = (path("userRegisterOut") & get) {//get out Register page,means return to Login page
    userAuth{ _ =>
      val session=Set(SessionTypeKey,SessionKeys.name)
      removeSession(session){ctx =>
        ctx.complete(SuccessRsp())
      }
    }
  }



  val registerRoutes: Route =
    pathPrefix("register") {
      userRegister ~ userRegisterIn ~userRegisterOut
    }

}
