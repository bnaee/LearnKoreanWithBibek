package com.genesiswtech.lkwb.ui.grammar.view

import android.view.View
import com.genesiswtech.lkwb.ui.grammar.model.GrammarCategoryDataResponse
import com.genesiswtech.lkwb.ui.grammar.model.GrammarCategoryResponse
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayDataResponse

interface IGrammarView {

    fun initDependencies()

    fun onGrammarDayClick(v: View?)

    fun onGrammarTitleClick(v: View?)

    fun onGrammarSearchClick(v: View?)

    fun onSuccess(grammarDayDataResponse: GrammarDayDataResponse)
    fun onGrammarCategorySuccess(grammarCategoryDataResponseList: ArrayList<GrammarCategoryDataResponse>)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}