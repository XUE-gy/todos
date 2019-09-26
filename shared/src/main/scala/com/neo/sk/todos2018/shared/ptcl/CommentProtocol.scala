package com.neo.sk.todos2018.shared.ptcl

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:45
  *
  * update by zhangtao: 2019-3-23
  *
  */
object CommentProtocol {


  //添加评论
  case class AddCommentReq( contentId : Int,
                            content: String)
  //删除评论,这里的id是主键id
  case class DelCommentReq(id: Int)
  //获取评论id
  case class getCommentReq(id: Int)


  //返回评论列表
  case class CommentRecord(
                         id: Int,
                         contentId : Int,
                         commentator : String,
                         comment : String
                       )
  case class GetListRsp(
                         list: Option[List[CommentRecord]],
                         errCode: Int = 0,
                         msg: String = "Ok"
                       ) extends CommonRsp

}
