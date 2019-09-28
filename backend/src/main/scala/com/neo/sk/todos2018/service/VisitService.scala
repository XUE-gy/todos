package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.ToDoListDAO
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq}
import com.neo.sk.todos2018.shared.ptcl.VisitProtocol.{GetListRsp,TaskFollow,GetListRsp2,getFollowedReq,TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:41
  * 2019/3/21 delete session check
  */
trait VisitService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addRecord = (path("addRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, AddRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            val author = user.userName
            println(s"add record: ${req.content}")
            ToDoListDAO.addRecord(author, req.content).map { r =>
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

  private val delRecord = (path("delRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, DelRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            val id = req.id
            println(s"delete record: $id")
            ToDoListDAO.delRecord(id).map { r =>
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

  private val getList = (path("getList") & post) {

    entity(as[Either[Error, getFollowedReq]]) {
      case Left(error) =>
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        dealFutureResult(
          ToDoListDAO.getRecordList(req.followed).map { list =>
            val data = list.map(r => TaskRecord(r.id, r.content, r.time, r.like)).toList
            complete(GetListRsp2(Some(data)))
          }
        )
    }

  }

  private val getFollowList = (path("getFollowList") & get) {
    userAuth{ user =>
      dealFutureResult(
        ToDoListDAO.getFollowList(user.userName).map { list =>
          val data = list.map( r => TaskFollow(r.followed,r.follower)).toList
          complete(GetListRsp(Some(data)))
        }
      )
    }
  }

  val visitRoutes: Route =
    pathPrefix("visit") {
      addRecord ~ delRecord  ~ getFollowList ~ getList
    }

}
