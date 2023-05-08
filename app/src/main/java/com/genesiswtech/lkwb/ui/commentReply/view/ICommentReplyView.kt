package com.genesiswtech.lkwb.ui.commentReply.view

import android.view.View
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyDataResponse
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyResponse
import com.genesiswtech.lkwb.ui.commentReply.model.ReplyEditDataResponse

interface ICommentReplyView {

    fun initDependencies()

    fun onAddReplyClick(v: View?)

    fun onReplyPostSuccess(replyData: CommentReplyDataResponse)

    fun onReplySuccess(replyDataList: ArrayList<CommentReplyDataResponse>)

    fun onReplyDeleteSuccess(replyData: CommentReplyResponse)

    fun onReplyEditSuccess(replyData: CommentReplyDataResponse)

    fun onReplyEmpty()

    fun onFailure(message: String?)

    fun onTimeOut()

    fun onNoData(status:Boolean)

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}