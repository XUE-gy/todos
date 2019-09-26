## demo功能说明

* 用户登录（无密码验证）
* 添加事件（有数据库）
* 删除事件（无操作）
* 获得事件列表

## 工程运行

运行本工程：

* 打开IDEA 下的terminal；
* 输入sbt回车；（如果报错，请确保你的电脑有正确安装sbt，第一次运行需要更新sbt和下载相关依赖，耐心等待）
   * X:\xxx\xxxx\xxx> sbt
   *准确输入D:\sbt\bin\sbt                        run
* 此时你已经进入sbt交互式命令行状态：
     * sbt:todos2018> project backend
     * sbt:todos2018> reStart
     
* 工程默认运行在30330端口，浏览器输入http://localhost:30330/todos2018；
* 在浏览器上使用；
* 退出用sbt:todos2018> exit
* 端口、数据库等数据写在配置文件里，配置文件目录：backend/src/main/resources/application.conf；
* 数据库路径为相对路径，实际操作最好改为绝对路径，即DATA/H2/todos2018文件在自己电脑的绝对路径
* 操作数据库现在使用的是Slick，Slick的代码结构在backend\src\main\scala\com\neo\sk\todos2018\models\SlickTables.scala。
  这是backend\src\main\scala\com\neo\sk\todos2018\utils\MySlickCodeGenerator.scala生成的。
  MySlickCodeGenerator可以右键直接运行

## 3.27添加事项

* 用户的账号写死在了LoginService里面（账号只有‘test’,密码为空），登录后用session存储了账号信息（账号名称）。
* 在ToDoListService里面有验证session的操作，即userAuth函数如何使用。
* 添加代办事项使用了H2数据库，数据库路径、账号、密码写在配置文件里面。
* 数据库表的创建可以在H2的Web Console里进行，这个工程的示例H2数据库的版本是1.4.196。ps：也可以摸索地在IDEA上直接操作数据库

## 3.28

* Login的样式是backend\src\main\resources\css里的css文件写的
* TaskList的样式是front\styles里的scalacss文件写的
* 删除了Index特质

## 6.9

* 修复了输入框会保存以前数据的bug
* 添加了Slick框架自动生成的机制

## 待完成功能tip

* 用户登录校验
  
  将用户账号和密码存放在数据库后，可以读取数据库校验账号和密码

* 多用户用户登入流程

  用户的登录可以使用session。
  session可以认为是放在cookie里的一个参数Map，todos2018的session Map定义在backend\src\main\scala\com\neo\sk\todos2018\service\SessionBase.scala。
  可以参考backend\src\main\scala\com\neo\sk\todos2018\utils\SessionSupport.scala里的函数添加删除session。
  使用SessionBase.scala的userAuth函数检验session。
  
## session实现流程

  首先从网页入手，在login登陆时传入session，在退出TaskLists时销毁session
  详细流程（前端->后端->前端）：    login.scala: Http.postJsonAndParse[SuccessRsp](Routes.Login.userLogin, UserLoginReq(userName, password).asJson.noSpaces).map {}传入
                                  Routes中定义的userLogin通过path传回loginservice
                                  最重要的是Loginprotocol.scala中的UserLoginReq连接了login和loginservice，将登陆时的username和password返回到后端，接着后续session处理
                                  loginservice.scala: val session = ToDoListSession(UserBaseInfo(req.userName), System.currentTimeMillis())传入session
                                  sessionbase：toSessionMap设定了session传入值，接收到session
                                  sessionbase：userAuth将session传出
                                  todolistservice：userAuth得到session，将其返回前端
                                  tasklist：通过taskList := rsp.list.get获取session传来的数据库的list，退出时用taskList := Nil消除
                                  tasklist:而session的消除是通过logout中的Routes.Login.userLogout传到routes再传到loginservice中的userLogout，使用removesession消除
   
## tasklist可以实现删除同一行数据的流程（也可用于接收数据传入session携带进入visit页面）
   tasklist中getDeleteButton（按钮）调用deleteRecord再在其中传(Routes.List.delRecord, data)data是文本的id
   id在前端<div>中用getDeleteButton(l.id)传入
   data=DelRecordReq(id)..  DelRecordReq是todolist中的DelRecordReq，它连接了前后端，传入id并在数据库进行删除
         
## 查看评论区的方法
   获取id，传入session，在另一界面通过session获取数据库所需数据，返回页面
   
   
## 已有：获取用户名和密码与数据库比对（类推）

## 已有：直接根据session获取用户的博客list
         
## 目的：获取id与数据库比对，并返回属于id的数据表


## 前端函数名改完，改post或get中的route，再加route中的路径，再加HttpService中的路径，再加后端最后的Route路径，再调用路径中函数



## 参考链接
* [scala-js](http://www.scala-js.org/doc/tutorial/basic/)
* [akka-http](https://doc.akka.io/docs/akka-http/current/introduction.html)
* [monadic-html](https://github.com/OlivierBlanvillain/monadic-html/blob/master/README.md)
