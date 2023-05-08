package com.genesiswtech.lkwb.ui.search.presenter

import android.content.Context

interface ISearchPresenter {

    fun postGrammarSearch(context: Context, page: Int, limit: Int, type: String, text: String)

    fun postDictionarySearch(context: Context, page: Int, limit: Int, type: String, text: String)

    fun postBlogSearch(context: Context, page: Int, limit: Int, type: String, text: String)

    fun postDiscussionSearch(context: Context, page: Int, limit: Int, type: String, text: String)
}