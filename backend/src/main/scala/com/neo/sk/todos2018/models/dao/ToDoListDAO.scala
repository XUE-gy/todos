package com.neo.sk.todos2018.models.dao

import org.slf4j.LoggerFactory
import com.neo.sk.todos2018.utils.DBUtil.db
import com.neo.sk.todos2018.models.SlickTables._
import slick.jdbc.PostgresProfile.api._
import sun.security.util.Password

import scala.collection.mutable
import scala.concurrent.duration.Duration

import scala.concurrent.{Await, Future}

//import scala.slick.jdbc.{GetResult, StaticQuery => Q}
//import scala.slick.jdbc.JdbcBackend.Database
//import Q.interpolation

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:17
  * changed by Xu Si-ran, delete user
  * update by Tao 2019-3-23, add Record class and rename list to recordList.
  * 以下用slick操作数据库，若出现找不到表的情况，需要修改配置文件数据库的路径为绝对路径（没有..）
  */
//case class Record(id: Int, author: String, content: String, time: Long)
//
//trait FetchInfoDAOTable{
//  import com.neo.sk.todos2018.utils.DBUtil.driver.api._
//
//  class RecordInfoTable(tag: Tag) extends Table[Record](tag, "RECORD_INFO") {
//    def * = (id, author, content, time) <> (Record.tupled, Record.unapply)
//
//    val id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
//    val author = column[String]("AUTHOR")
//    val content = column[String]("CONTENT")
//    val time = column[Long]("TIME")
//
//  }
//
//  protected val recordInfoTableQuery = TableQuery[RecordInfoTable]
//}


object ToDoListDAO{
  private val log = LoggerFactory.getLogger(this.getClass)

  def addAuthor(author: String, password: String,email: String): Future[Int] = {
    try {
      if (author.length == 0 ) {
        log.error(s"empty author")
        Future.successful(-1)
      } else if (password.length == 0) {
        log.error(s"empty password")
        Future.successful(-1)
      } else if (email.length == 0){
        log.error(s"empty email")
        Future.successful(-1)
      } else {
        db.run(tUserInfo.map(t => (t.author, t.password, t.email)) += (author, password, email))
      }
    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }

  def getAuthorList(author: String): Future[Seq[rUserInfo]] = {
    try {
      db.run(tUserInfo.filter(t => t.author === author).result)
    } catch {
      case e: Throwable =>
        log.error(s"get AuthorList error with error $e")
        Future.successful(Nil)
    }
  }

  def getFollowList(followed: String, follower: String): Future[Seq[rFollowInfo]] = {//获取唯一关注
    try {
//      db.run(tFollowInfo.filter(t => (t.followed,t.follower)==(followed,follower)).result)
      db.run(tFollowInfo.filter(t => t.followed===followed).filter(t=>t.follower===follower).result)
    } catch {
      case e: Throwable =>
        log.error(s"get AuthorList error with error $e")
        Future.successful(Nil)
    }
  }

  def addFollowList(followed: String, follower: String): Future[Int] = {
    try {

        db.run(tFollowInfo.map(t => (t.followed,t.follower)) += (followed,follower))

    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }

  def getFollowList(follower: String): Future[Seq[rFollowInfo]] = {//获取关注列表
    try {
      //      db.run(tFollowInfo.filter(t => (t.followed,t.follower)==(followed,follower)).result)
      db.run(tFollowInfo.filter(t => t.follower===follower).result)
    } catch {
      case e: Throwable =>
        log.error(s"get AuthorList error with error $e")
        Future.successful(Nil)
    }
  }

  def addLike(id: Int,like: Int): Future[Int] = {
    try {
//      db.run(tRecordInfo.map(t => t.id ===id).filter(t => t.))
      db.run(
        tRecordInfo.filter(t => t.id===id).map(_.like).update(like+1)
      )
//      db.run(tFollowInfo.map)
//      sql"""select * from RECORD_INFO where ID = ${id}"""


    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }





  def addRecord(author: String, content: String): Future[Int] = {
    try {
      if (author.length == 0 ) {
        log.error(s"empty author")
        Future.successful(-1)
      } else if (content.length == 0) {
        log.error(s"empty content")
        Future.successful(-1)
      } else {
        db.run(tRecordInfo.map(t => (t.author, t.content, t.time, t.like)) += (author, content, System.currentTimeMillis(),0))
      }
    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }




  def delRecord(id: Int): Future[Int] = {
    try {
      // 待补充
      db.run(tRecordInfo.filter(t =>t.id === id).delete)
//      db.run(sql"""SELECT * FROM RECORD_INFO WHERE id = $id""".as)

      Future.successful(1)
    } catch {
      case e: Throwable =>
        log.error(s"del record error with error $e")
        Future.successful(-1)
    }
  }

  def getRecordList(author: String): Future[Seq[rRecordInfo]] = {//获取了相应author的博客
    try {
      db.run(tRecordInfo.filter(t => t.author === author).result)
    } catch {
      case e: Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }
  def getCommentList(id : Int): Future[Seq[rCommentInfo]] = {//获取了相应id博客的评论
    try {
      db.run(tCommentInfo.filter(t => t.contentId === id).result)


    } catch {
      case e: Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }



  def delComment(id: Int): Future[Int] = {
    try {
      // 待补充
      db.run(tCommentInfo.filter(t =>t.id === id).delete)
      //      db.run(sql"""SELECT * FROM RECORD_INFO WHERE id = $id""".as)

      Future.successful(1)
    } catch {
      case e: Throwable =>
        log.error(s"del record error with error $e")
        Future.successful(-1)
    }
  }


  def addComment(id : Int,author: String, content: String): Future[Int] = {
    try {
      if (author.length == 0 ) {
        log.error(s"empty author")
        Future.successful(-1)
      } else if (content.length == 0) {
        log.error(s"empty content")
        Future.successful(-1)
      } else {
        db.run(tCommentInfo.map(t => (t.contentId, t.commentator,t.comment)) += (id,author,content))
      }
    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }
  def getRecentList(): Future[Seq[rRecordInfo]] = {//获取了相应author的博客
    try {
      db.run(tRecordInfo.filter(t => t.id >= 300).result)
    } catch {
      case e: Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }


}
