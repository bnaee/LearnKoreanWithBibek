package com.genesiswtech.lkwb.ui.comment.presenter

import android.content.Context

interface ICommentPresenter {

    fun getComments(context: Context, id: Int, page: Int, limit: Int)

    fun postEditComment(
        context: Context, id: Int, type: String, body: String
    )

    fun postComment(
        context: Context, id: Int, body: String
    )

    fun postDeleteComment(context: Context, id: Int, type: String)
}