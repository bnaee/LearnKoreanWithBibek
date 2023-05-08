package com.genesiswtech.lkwb.ui.search.view

import android.text.Editable
import android.text.TextWatcher
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionDataResponse
import com.genesiswtech.lkwb.ui.search.model.GrammarSearchDataResponse

interface ISearchView {

    fun initDependencies()

    fun onEditTextWatcher(): TextWatcher

    fun onGrammarSearchSuccess(grammarSearchDataList: ArrayList<GrammarSearchDataResponse>)

    fun onDictionarySearchSuccess(dictionarySearchDataList: ArrayList<DictionarySearchDataResponse>)

    fun onBlogSearchSuccess(blogSearchDataList: ArrayList<BlogDataResponse>)

    fun onDiscussionSearchSuccess(discussionSearchDataList: ArrayList<DiscussionDataResponse>)

    fun onNoData(status:Boolean)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}