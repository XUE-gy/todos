package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, TaskRecord}
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
object TaskList{

  val url = "#/" + "List"

  val taskList = Var(List.empty[TaskRecord])

  def getDeleteButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>deleteRecord(id)}>删除</button>
  def getCommentButton(id: Int) =  <button class={commentButton.htmlClass} onclick={()=>commentRecord(id)}>查看评论</button>

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

  def getList: Unit = {
    Http.getAndParse[GetListRsp](Routes.List.getList).map {
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
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无任务记录</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <table>
        <tr>
          <th class={th.htmlClass}>任务</th>
          <th class={th.htmlClass}>创建时间</th>
          <th class={th.htmlClass}>点赞数</th>
          <th class={th.htmlClass}>操作</th>
          <th class={th.htmlClass}>评论</th>

        </tr>
        {list.map {l =>
        <tr>
          <td class={td.htmlClass}>{l.content}</td>
          <td class={td.htmlClass}>{TimeTool.dateFormatDefault(l.time)}</td>
          <td class={td.htmlClass}>{l.like}</td>
          <td class={td.htmlClass}>{getDeleteButton(l.id)}</td>
          <td class={td.htmlClass}>{getCommentButton(l.id)}</td>
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

  def visitIn(): Unit = {
//          JsFunc.alert("查看其他用户")
          dom.window.location.hash = "/PreVisit"
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
      <button class={visitInButton.htmlClass} onclick={()=>visitIn()}>查看关注用户</button>
      <button class={logoutButton.htmlClass} onclick={()=>logout()}>退出</button></div>
    <div style="margin:30px;font-size:25px;">我的微博</div>
    <div style="margin-left:30px;">
      <input id ="taskInput" class={input.htmlClass}></input>
    <button class={addButton.htmlClass} onclick={()=>addRecord}>+添加</button>
    </div>
    {taskListRx}
  </div>
  }

}
