package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.logout
import com.neo.sk.todos2018.front.styles.ListStyles.logoutButton
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.{CommonRsp, SuccessRsp}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.{UserLoginReq, UserLoginRsp}
import com.neo.sk.todos2018.shared.ptcl.RegisterProtocol.UserRegisterReq
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.xml.Node
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: XueGanYuan
  * Date: 2019/9/22
  * Time: 11:49
  */
object Register{

  val url = "#/" + "Register"

//  private def userRegister(): Unit ={
//    val userName = dom.document.getElementById("userName").asInstanceOf[Input].value
//    val password = dom.document.getElementById("userPassword").asInstanceOf[Input].value
//    val email = dom.document.getElementById("userEmail").asInstanceOf[Input].value
//    Http.postJsonAndParse[SuccessRsp](Routes.Register.userRegister, UserRegisterReq(userName, password, email).asJson.noSpaces).map{
//      case Right(rsp) =>
//        if(rsp.errCode == 0){
//          JsFunc.alert("注册成功")
//          dom.window.location.hash = "/Login"
//        }
//        else{
//          JsFunc.alert(s"注册失败：${rsp.msg}")
//        }
//      case Left(error) =>
//        JsFunc.alert(s"parse error,$error")
//    }
//  }


  private def userRegister(): Unit = {
    val userName = dom.document.getElementById("userName").asInstanceOf[Input].value
    val password = dom.document.getElementById("userPassword").asInstanceOf[Input].value
    val email = dom.document.getElementById("userEmail").asInstanceOf[Input].value
    Http.postJsonAndParse[SuccessRsp](Routes.Register.userRegister, UserRegisterReq(userName, password, email).asJson.noSpaces).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          JsFunc.alert("注册成功")
          dom.window.location.hash = "/Login"
        }
        else {
          JsFunc.alert(s"注册失败：${rsp.msg}")
        }
      case Left(error) =>
        JsFunc.alert(s"parse error,$error")
    }
  }

  private def userRegisterOut(): Unit ={
          JsFunc.alert("返回登陆页面")
          dom.window.location.hash = "/Login"
  }


//  <span>邮箱</span>
//    <input id = "userPassword" type = "password"></input>


  def app: Node =
    <div>
      <div>
      <button class={logoutButton.htmlClass} onclick={()=>userRegisterOut()}>退出</button></div>
      <div class = "LoginForm">
        <h2>欢迎注册</h2>
        <div class = "inputContent">
          <span>用户名</span>
          <input id = "userName"></input>
        </div>
        <div class = "inputContent">
          <span>密码</span>
          <input id = "userPassword" type = "password"></input>
        </div>
        <div class = "inputContent">
          <span>邮箱</span>
          <input id = "userEmail"></input>
        </div>
        <button onclick = {()=> userRegister()}>注册</button>

      </div>
    </div>
}

