package com.neo.sk.todos2018.shared.ptcl

/**
  * User: Xueganyuan
  * Date: 2019/9/24
  * Time: 11:33
  *
  */
object VisitProtocol {


  //添加评论
  case class AddCommentReq(content: String)
  //删除评论
  case class DelCommentReq(id: Int)
  //点赞
  case class AddLikeReq(Likes : Int)
  //取消点赞
  case class DelLikeReq(id: Int)
  //转发待补充


  //获得其他用户动态列表，另外还有获得动态评论列表
  case class TaskRecord(
                         id: Int,
                         author : String,
                         content: String,
                         time: Long,
                       )
  case class GetListRsp(
                         list: Option[List[TaskRecord]],
                         errCode: Int = 0,
                         msg: String = "Ok"
                       ) extends CommonRsp

}
