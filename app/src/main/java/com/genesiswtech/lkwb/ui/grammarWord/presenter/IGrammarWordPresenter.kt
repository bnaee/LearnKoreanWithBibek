package com.genesiswtech.lkwb.ui.grammarWord.presenter

import android.content.Context

interface IGrammarWordPresenter {
    fun getGrammarWord(context: Context, id: Int)
    fun postAddFavouriteGrammar(context: Context, id: Int, type: String)
    fun postRemoveFavouriteGrammar(context: Context, id: Int, type: String)
}