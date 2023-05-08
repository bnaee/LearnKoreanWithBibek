package com.genesiswtech.lkwb.ui.dictionarySearch.presenter

import android.content.Context

interface IDictionarySearchPresenter {

    fun getDictionaries(context: Context,page:Int,limit:Int)
}