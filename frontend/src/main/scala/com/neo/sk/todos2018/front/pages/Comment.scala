package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
//import com.neo.sk.todos2018.shared.ptcl.CommentProtocol.
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
//import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.CommentProtocol.{AddCommentReq, DelCommentReq, GetListRsp, CommentRecord,getCommentReq}
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import com.neo.sk.todos2018.front.styles.ListStyles._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by XueGanYuan on 2019/9/25.
  *
  */
case class Comment(contentId : Int){

  val url = "#/" + "Comment"

  val taskList = Var(List.empty[CommentRecord])
  def getDeleteButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>deleteComment(id)}>删除</button>

  def addComment: Unit = {
    val data = dom.document.getElementById("commentInput").asInstanceOf[Input].value
    if (data == ""){
      JsFunc.alert("输入框不能为空！")
    }
    else{
      Http.postJsonAndParse[SuccessRsp](Routes.Comment.addComment, AddCommentReq(contentId,data).asJson.noSpaces).map {
        case Right(rsp) =>
          if(rsp.errCode == 0) {
            JsFunc.alert("添加成功！")
            getList
          } else {
            JsFunc.alert("添加失败！")
            println(rsp.msg)
          }

        case Left(error) =>
          println(s"parse error,$error")
      }
    }
  }

  def deleteComment(id: Int): Unit = {
    val data = DelCommentReq(id).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.Comment.delComment, data).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("删除成功！")
          getList
        } else {
          JsFunc.alert("删除失败！")
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"parse error,$error")
    }
  }


//  def getList: Unit = {
//    Http.getAndParse[GetListRsp](Routes.Comment.getList).map {
//      case Right(rsp) =>
//        if(rsp.errCode == 0){
//          taskList := rsp.list.get
//        } else {
//          JsFunc.alert(rsp.msg)
//          dom.window.location.hash = s"#/Login"
//          println(rsp.msg)
//        }
//      case Left(error) =>
//        println(s"get task list error,$error")
//    }
//  }

  def getList: Unit = {JsFunc.alert("获取评论区")
    Http.postJsonAndParse[GetListRsp](Routes.Comment.getList, getCommentReq(contentId).asJson.noSpaces).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          taskList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }
  }



  val taskListRx = taskList.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无评论记录</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <table>
        <tr>
          <th class={th.htmlClass}>评论人</th>
          <th class={th.htmlClass}>评论</th>
          <th class={th.htmlClass}>操作</th>

        </tr>
        {list.map {l =>
        <tr>
          <td class={td.htmlClass}>{l.commentator}</td>
          <td class={td.htmlClass}>{l.comment}</td>
          <td class={td.htmlClass}>{getDeleteButton(l.id)}</td>
        </tr>
      }
        }

      </table>

    </div>
  }

  def logout(): Unit = {
    Http.getAndParse[SuccessRsp](Routes.Login.userLogout).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          JsFunc.alert("退出成功")
          taskList := Nil
          dom.window.location.hash = "/Login"
        }
        else{
          JsFunc.alert(s"退出失败：${rsp.msg}")
        }
      case Left(error) =>
        JsFunc.alert(s"parse error,$error")
    }
  }

//  def visitIn(): Unit = {
//    JsFunc.alert("查看其他用户")
//    dom.window.location.hash = "/Visit"
//  }

  def commentOut(): Unit = {
    JsFunc.alert("退出评论区")
    dom.window.location.hash = "/List"
  }

  def app: xml.Node = {
    getList
    <div>
      <div>
        <button class={visitInButton.htmlClass} onclick={()=>commentOut()}>退出评论区</button></div>
      <div style="margin:30px;font-size:25px;">评论区</div>
      <div style="margin-left:30px;">
        <input id ="commentInput" class={input.htmlClass}></input>
        <button class={addButton.htmlClass} onclick={()=>addComment}>+评论自己</button>
      </div>
      {taskListRx}
    </div>
  }

}
