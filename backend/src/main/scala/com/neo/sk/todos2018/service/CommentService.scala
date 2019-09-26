package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.shared.ptcl.CommentProtocol.{AddCommentReq, CommentRecord, GetListRsp, getCommentReq,DelCommentReq}
//import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: XueGanYuan
  * Date: 2019/9/26
  * Time: 0:17
  * 2019/3/21 delete session check
  */
trait CommentService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addComment = (path("addComment") & post) {
    userAuth { user =>
      entity(as[Either[Error, AddCommentReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            val author = user.userName
            println(s"add record: ${req.content}")
            ToDoListDAO.addComment(req.contentId,author, req.content).map { r =>
              if (r > 0) {
                complete(SuccessRsp())
              } else {
                complete(ErrorRsp(1000101, "add record error"))
              }
            }
          }
      }
    }
  }

  private val delComment = (path("delComment") & post) {

    entity(as[Either[Error, DelCommentReq]]) {
      case Left(error) =>
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult {
          val id = req.id
          println(s"delete record: $id")
          ToDoListDAO.delComment(id).map { r =>
            if (r > 0) {
              complete(SuccessRsp())
            } else {
              complete(ErrorRsp(1000101, "add record error"))
            }
          }
        }
    }

  }

  //  private val getList = (path("getList") & get) {
  //
  //      dealFutureResult(
  //        ToDoListDAO.getCommentList(user.userName).map { list =>
  //          val data = list.map( r => TaskRecord(r.id, r.content, r.time)).toList
  //          complete(GetListRsp(Some(data)))
  //        }
  //      )
  //
  //  }

  private val getList = (path("getList") & post) {
    entity(as[Either[Error, getCommentReq]]) {
      case Left(error) =>
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult(
          ToDoListDAO.getCommentList(req.id).map { list =>
            val data = list.map(r => CommentRecord(r.id, r.contentId, r.commentator, r.comment)).toList
            complete(GetListRsp(Some(data)))
          }
        )
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

  val commentRoutes: Route =
    pathPrefix("comment") {
      addComment ~ delComment ~ getList
    }

}
