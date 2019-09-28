package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq,TaskRecord}
import com.neo.sk.todos2018.shared.ptcl.VisitProtocol.{AddCommentReq, AddLikeReq,TaskFollow,GetListRsp}
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import com.neo.sk.todos2018.front.styles.ListStyles._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by haoshuhan on 2018/6/4.
  * changed by Xu Si-ran on 2019/3/21
  * update by zhangtao, 2019-3-23: record id.
  */
object PreVisit{

  val url = "#/" + "List"

  val taskList = Var(List.empty[TaskFollow])

  def getDeleteButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>deleteRecord(id)}>删除</button>
  def visitFollowedButton(followed: String) =  <button class={commentButton.htmlClass} onclick={()=>visitRecord(followed)}>他人主页</button>

  def addRecord: Unit = {
    val data = dom.document.getElementById("taskInput").asInstanceOf[Input].value
    if (data == ""){
      JsFunc.alert("输入框不能为空！")
    }
    else{
      Http.postJsonAndParse[SuccessRsp](Routes.List.addRecord, AddRecordReq(data).asJson.noSpaces).map {
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

  def deleteRecord(id: Int): Unit = {
    val data = DelRecordReq(id).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.delRecord, data).map {
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
  def commentRecord(id: Int): Unit = {
    JsFunc.alert("进入评论区")
    dom.window.location.hash = s"/Comment," + id
  }

  def visitRecord(followed:String): Unit = {
    JsFunc.alert("进入评论区")
    dom.window.location.hash = s"/Visit," + followed

  }


  def getList: Unit = {
    Http.getAndParse[GetListRsp](Routes.Visit.getFollowList).map {
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
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无关注人</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <table>
        <tr>
          <th class={th.htmlClass}>关注列表</th>
          <th class={th.htmlClass}>操作</th>

        </tr>
        {list.map {l =>
        <tr>
          <td class={td.htmlClass}>{l.followed}</td>
          <td class={td.htmlClass}>{visitFollowedButton(l.followed)}</td>
        </tr>
      }
        }

      </table>

    </div>
  }

  def PreVisitOut(): Unit = {
    dom.window.location.hash = "/List"
  }


  def RecentIn(): Unit = {
    //    JsFunc.alert("查看其他用户")
    dom.window.location.hash = "/Recent"
  }



  def app: xml.Node = {
    getList
    <div>
      <div>
        <button class={visitInButton.htmlClass} onclick={()=>RecentIn()}>查看近期微博</button>
        <button class={logoutButton.htmlClass} onclick={()=>PreVisitOut()}>返回主页</button></div>
      <div style="margin:30px;font-size:25px;">关注页面</div>
      <div style="margin-left:30px;">

      </div>
      {taskListRx}
    </div>
  }

  //  <input id ="taskInput" class={input.htmlClass}></input>
  //    <button class={addButton.htmlClass} onclick={()=>addRecord}>+添加关注</button>


}