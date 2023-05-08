package com.genesiswtech.lkwb.ui.dictionaryWord.presenter

import android.content.Context

interface IDictionaryWordPresenter {

    fun getSingleDictionary(context: Context, id: Int)
    fun postAddFavouriteDictionary(context: Context, id: Int, type: String)
    fun postRemoveFavouriteDictionary(context: Context, id: Int, type: String)
}