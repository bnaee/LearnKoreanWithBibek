package com.genesiswtech.lkwb.ui.comment.view

import android.view.View
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentDataResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentEditDataResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentResponse

interface ICommentView {

    fun initDependencies()

    fun onAddCommentClick(v: View?)

    fun onCommentPostSuccess(commentData: CommentDataResponse)

    fun onSuccess(commentDataList: ArrayList<CommentDataResponse>)

    fun onCommentDeleteSuccess(commentData: CommentResponse)

    fun onCommentEditSuccess(commentData: CommentEditDataResponse)

    fun onCommentEmpty()

    fun onFailure(message: String?)

    fun onTimeOut()

    fun onNoData(status: Boolean)

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int, status: Boolean)
}