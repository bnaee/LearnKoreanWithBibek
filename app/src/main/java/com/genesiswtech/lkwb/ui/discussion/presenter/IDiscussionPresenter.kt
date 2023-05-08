package com.genesiswtech.lkwb.ui.discussion.presenter

import android.content.Context

interface IDiscussionPresenter {

    fun getDiscussions(context: Context, page: String, limit: String, sort_order: String)

    fun postNewDiscussion(context: Context, title: String, description: String)
}