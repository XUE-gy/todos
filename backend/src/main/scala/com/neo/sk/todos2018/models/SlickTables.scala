package com.neo.sk.todos2018.models

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.H2Profile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tCommentInfo.schema ++ tRecordInfo.schema ++ tUserInfo.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tCommentInfo
    *  @param contentId Database column CONTENT_ID SqlType(INTEGER)
    *  @param commentator Database column COMMENTATOR SqlType(VARCHAR), Length(63,true)
    *  @param comment Database column COMMENT SqlType(VARCHAR), Length(1023,true)
    *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
  case class rCommentInfo(contentId: Int, commentator: String, comment: String, id: Int)
  /** GetResult implicit for fetching rCommentInfo objects using plain SQL queries */
  implicit def GetResultrCommentInfo(implicit e0: GR[Int], e1: GR[String]): GR[rCommentInfo] = GR{
    prs => import prs._
      rCommentInfo.tupled((<<[Int], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table COMMENT_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tCommentInfo(_tableTag: Tag) extends profile.api.Table[rCommentInfo](_tableTag, "COMMENT_INFO") {
    def * = (contentId, commentator, comment, id) <> (rCommentInfo.tupled, rCommentInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(contentId), Rep.Some(commentator), Rep.Some(comment), Rep.Some(id))).shaped.<>({r=>import r._; _1.map(_=> rCommentInfo.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column CONTENT_ID SqlType(INTEGER) */
    val contentId: Rep[Int] = column[Int]("CONTENT_ID")
    /** Database column COMMENTATOR SqlType(VARCHAR), Length(63,true) */
    val commentator: Rep[String] = column[String]("COMMENTATOR", O.Length(63,varying=true))
    /** Database column COMMENT SqlType(VARCHAR), Length(1023,true) */
    val comment: Rep[String] = column[String]("COMMENT", O.Length(1023,varying=true))
    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table tCommentInfo */
  lazy val tCommentInfo = new TableQuery(tag => new tCommentInfo(tag))

  /** Entity class storing rows of table tRecordInfo
    *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
    *  @param author Database column AUTHOR SqlType(VARCHAR), Length(63,true)
    *  @param content Database column CONTENT SqlType(VARCHAR), Length(1023,true)
    *  @param time Database column TIME SqlType(BIGINT) */
  case class rRecordInfo(id: Int, author: String, content: String, time: Long)
  /** GetResult implicit for fetching rRecordInfo objects using plain SQL queries */
  implicit def GetResultrRecordInfo(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rRecordInfo] = GR{
    prs => import prs._
      rRecordInfo.tupled((<<[Int], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table RECORD_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tRecordInfo(_tableTag: Tag) extends profile.api.Table[rRecordInfo](_tableTag, "RECORD_INFO") {
    def * = (id, author, content, time) <> (rRecordInfo.tupled, rRecordInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(author), Rep.Some(content), Rep.Some(time))).shaped.<>({r=>import r._; _1.map(_=> rRecordInfo.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column AUTHOR SqlType(VARCHAR), Length(63,true) */
    val author: Rep[String] = column[String]("AUTHOR", O.Length(63,varying=true))
    /** Database column CONTENT SqlType(VARCHAR), Length(1023,true) */
    val content: Rep[String] = column[String]("CONTENT", O.Length(1023,varying=true))
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")
  }
  /** Collection-like TableQuery object for table tRecordInfo */
  lazy val tRecordInfo = new TableQuery(tag => new tRecordInfo(tag))

  /** Entity class storing rows of table tUserInfo
    *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
    *  @param author Database column AUTHOR SqlType(VARCHAR), Length(36,true)
    *  @param password Database column PASSWORD SqlType(VARCHAR), Length(19,true)
    *  @param email Database column EMAIL SqlType(VARCHAR), Length(31,true) */
  case class rUserInfo(id: Int, author: String, password: String, email: String)
  /** GetResult implicit for fetching rUserInfo objects using plain SQL queries */
  implicit def GetResultrUserInfo(implicit e0: GR[Int], e1: GR[String]): GR[rUserInfo] = GR{
    prs => import prs._
      rUserInfo.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table USER_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tUserInfo(_tableTag: Tag) extends profile.api.Table[rUserInfo](_tableTag, "USER_INFO") {
    def * = (id, author, password, email) <> (rUserInfo.tupled, rUserInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(author), Rep.Some(password), Rep.Some(email))).shaped.<>({r=>import r._; _1.map(_=> rUserInfo.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column AUTHOR SqlType(VARCHAR), Length(36,true) */
    val author: Rep[String] = column[String]("AUTHOR", O.Length(36,varying=true))
    /** Database column PASSWORD SqlType(VARCHAR), Length(19,true) */
    val password: Rep[String] = column[String]("PASSWORD", O.Length(19,varying=true))
    /** Database column EMAIL SqlType(VARCHAR), Length(31,true) */
    val email: Rep[String] = column[String]("EMAIL", O.Length(31,varying=true))
  }
  /** Collection-like TableQuery object for table tUserInfo */
  lazy val tUserInfo = new TableQuery(tag => new tUserInfo(tag))
}