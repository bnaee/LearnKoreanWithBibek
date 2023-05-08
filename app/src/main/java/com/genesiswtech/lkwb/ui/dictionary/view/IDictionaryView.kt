package com.genesiswtech.lkwb.ui.dictionary.view

import android.view.View
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayDataResponse

interface IDictionaryView {

    fun onDictionarySearchClick(v: View?)

    fun onDictionaryKERelClick(v: View?)

    fun onDictionaryKNRelClick(v: View?)

    fun initDependencies()

    fun onWordDayClick(v: View?)
    fun onWordTitleClick(v: View?)

    fun onSuccess(grammarDayDataResponse: GrammarDayDataResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}