package com.genesiswtech.lkwb.ui.favouriteDetail.view

import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordDataResponse
import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordDataResponse

interface IFavouriteDetailView {

    fun initDependencies()

    fun onFavouriteGrammarSuccess(grammarList: ArrayList<Grammar>)
    fun onFavouriteDictionarySuccess(dictionaryList: ArrayList<DictionarySearchDataResponse>)
    fun onFavouriteBlogSuccess(blogList: ArrayList<BlogDataResponse>)

    fun onFailure(message: String?)
    fun onNoData(size: Int)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}