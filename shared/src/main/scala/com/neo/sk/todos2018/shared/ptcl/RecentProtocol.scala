package com.neo.sk.todos2018.shared.ptcl

object RecentProtocol {


  //添加记录
  case class AddRecentReq(content: String)
 //近期微博不能删
//  case class DelRecordReq(id: Int)
  //  获得评论id
  //  case class CommentRecordReq(id: Int)
//     应该还有一个点赞，一个关注，一个查看评论，先做关注
  //获得列表

  //获得author名,即被关注者
  case class GetAuthorReq(author: String)
  //获得评论id用来点赞
  case class GetAddLike(id: Int, like: Int)

  case class TaskRecord(
                         id: Int,
                         author: String,
                         content: String,
                         time: Long,
                         like: Int
                       )
  case class GetListRsp(
                         list: Option[List[TaskRecord]],
                         errCode: Int = 0,
                         msg: String = "Ok"
                       ) extends CommonRsp

}