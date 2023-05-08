package com.genesiswtech.lkwb.ui.discussionDetail.view

import android.view.View
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.*

interface IDiscussionDetailView {

    fun initDependencies()

    fun onDiscussionEditClick(v: View?)

    fun onDiscussionShareClick(v: View?)

    fun onDiscussionDeleteClick(v: View?)

    fun onAddCommentClick(v: View?)

    fun onTitleError()

    fun onSuccess(discussionDetailResponse: DiscussionDetailResponse)

    fun onDiscussionDeleteSuccess(discussionDetailResponse: DiscussionDetailResponse)

    fun onDiscussionEditSuccess(discussionDetailResponse: DiscussionEditResponse)

    fun onCommentPostSuccess(commentData: CommentDataResponse)

    fun onCommentSuccess(commentDataList: ArrayList<CommentDataResponse>)

    fun onCommentDeleteSuccess(commentData: CommentResponse,id:Int)

    fun onCommentEditSuccess(commentDataResponse: CommentEditDataResponse,id:Int)

    fun onCommentEmpty()

    fun onFailure(message: String?)

    fun onTimeOut()

    fun onNoData(status:Boolean)

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}