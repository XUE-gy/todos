package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.Protocols.parseError
//import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.RecentProtocol.{GetListRsp, TaskRecord, GetAuthorReq,GetAddLike}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:41
  * 2019/3/21 delete session check
  */
trait RecentService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  //  private val log = LoggerFactory.getLogger(getClass)


  //  private val addRecord = (path("addRecord") & post) {
  //    userAuth{ user =>
  //      entity(as[Either[Error, AddRecordReq]]) {
  //        case Left(error) =>
  //          log.warn(s"some error: $error")
  //          complete(parseError)
  //        case Right(req) =>
  //          dealFutureResult {
  //            val author = user.userName
  //            println(s"add record: ${req.content}")
  //            ToDoListDAO.addRecord(author, req.content).map { r =>
  //              if (r > 0) {
  //                complete(SuccessRsp())
  //              } else {
  //                complete(ErrorRsp(1000101, "add record error"))
  //              }
  //            }
  //          }
  //      }
  //    }
  //  }

  //  private val delRecord = (path("delRecord") & post) {
  //    userAuth{ user =>
  //      entity(as[Either[Error, DelRecordReq]]) {
  //        case Left(error) =>
  //          log.warn(s"some error: $error")
  //          complete(parseError)
  //        case Right(req) =>
  //          dealFutureResult {
  //            val id = req.id
  //            println(s"delete record: $id")
  //            ToDoListDAO.delRecord(id).map { r =>
  //              if (r > 0) {
  //                complete(SuccessRsp())
  //              } else {
  //                complete(ErrorRsp(1000101, "add record error"))
  //
  //              }
  //            }
  //          }
  //      }
  //    }
  //  }

  private val getRecentList = (path("getRecentList") & get) {
    dealFutureResult(
      ToDoListDAO.getRecentList.map { list =>
        val data = list.map(r => TaskRecord(r.id, r.author, r.content, r.time, r.like)).toList
        complete(GetListRsp(Some(data)))
      }
    )
  }

  private val addFollow = (path("addFollow") & post) {
    userAuth { user =>
      entity(as[Either[Error, GetAuthorReq]]) {
        case Left(error) =>
          log.warn(s"error in userLogin: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            ToDoListDAO.getFollowList(req.author,user.userName).map { list =>
              if (list != Vector()) {
                  complete(ErrorRsp(10001, "已关注"))
              }
              else {
                ToDoListDAO.addFollowList(req.author,user.userName)
                complete(SuccessRsp())
              }
            }
          }
      }
    }

  }

  private val addLike = (path("addLike") & post) {

      entity(as[Either[Error,GetAddLike]]) {
        case Left(error) =>
          log.warn(s"error in addLike: $error")
          complete(parseError)
        case Right(req) =>
            ToDoListDAO.addLike(req.id, req.like)
            complete(SuccessRsp())

      }


  }


  //  private val commentRecord = (path("commentRecord") & post){
  //
  //    entity(as[Either[Error, CommentRecordReq]]) {
  //      case Left(error) =>
  //        log.warn(s"error in userLogin: $error")
  //        complete(parseError)
  //      case Right(req) =>
  //        dealFutureResult {
  //          ToDoListDAO.getCommentList(req.id).map { list =>
  ////            if (list == Vector()) complete(ErrorRsp(10001, "该用户不存在"))
  ////            else if(list(0).id != req.id){complete(ErrorRsp(10001, "密码不正确"))}
  ////            else{
  //              val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())
  //              addSession(session.toSessionMap) {
  //                complete(SuccessRsp())
  //              }
  ////            }
  //          }
  //        }
  //    }
  //  }

  val recentRoutes: Route =
    pathPrefix("recent") {
      getRecentList ~ addFollow ~ addLike
    }

}
