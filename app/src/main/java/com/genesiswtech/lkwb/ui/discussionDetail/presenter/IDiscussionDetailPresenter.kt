package com.genesiswtech.lkwb.ui.discussionDetail.presenter

import android.content.Context

interface IDiscussionDetailPresenter {

    fun getDiscussionSingle(context: Context, id: Int)

    fun postEditDiscussion(
        context: Context, id: Int, type: String, title: String, description: String
    )

    fun postDeleteDiscussion(context: Context, id: Int, type: String)

    fun getComments(context: Context, id: Int, page: Int, limit: Int)

    fun postEditComment(
        context: Context, id: Int, type: String, body: String
    )

    fun postComment(
        context: Context, id: Int, body: String
    )

    fun postDeleteComment(context: Context, id: Int, type: String)
}