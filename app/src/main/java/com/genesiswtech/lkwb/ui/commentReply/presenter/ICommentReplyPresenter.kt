package com.genesiswtech.lkwb.ui.commentReply.presenter

import android.content.Context

interface ICommentReplyPresenter {
    fun getReplies(context: Context, id: Int, page: Int, limit: Int)

    fun postEditReply(
        context: Context, id: Int, type: String, body: String
    )

    fun postReply(
        context: Context, id: Int, body: String
    )

    fun postDeleteReply(context: Context, id: Int, type: String)
}