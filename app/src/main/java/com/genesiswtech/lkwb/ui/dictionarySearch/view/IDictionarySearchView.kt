package com.genesiswtech.lkwb.ui.dictionarySearch.view

import android.view.View
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse

interface IDictionarySearchView {

    fun onDictionarySearchClick(v: View?)

    fun initDependencies()

    fun onSuccess(dictionarySearchDataResponseList: ArrayList<DictionarySearchDataResponse>)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}