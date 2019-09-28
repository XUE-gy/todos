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
  //删除评论,进入别人页面不能删除别人的评论
//  case class DelCommentReq(id: Int)
  //点赞
  case class AddLikeReq(Likes : Int)
  //取消点赞
  case class DelLikeReq(id: Int)
  //preVisit获取关注用户列表
//  case class GetFollowRsp(id: Int)
// 获取被关注者名称
  case class  getFollowedReq(followed: String)


  //转发待补充



  //获得关注者列表
  case class TaskFollow(
                         followed: String,
                         follower: String,
                       )
  case class GetListRsp(
                         list: Option[List[TaskFollow]],
                         errCode: Int = 0,
                         msg: String = "Ok"
                       ) extends CommonRsp

  //获得关注者的微博列表
  case class TaskRecord(
                         id: Int,
                         content: String,
                         time: Long,
                         like: Int
                       )
  case class GetListRsp2(
                         list: Option[List[TaskRecord]],
                         errCode: Int = 0,
                         msg: String = "Ok"
                       ) extends CommonRsp




}
