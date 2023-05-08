package com.genesiswtech.lkwb.ui.dictionaryWord.view

import android.view.View
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordDataResponse

interface IDictionaryWordView {

    fun initDependencies()

    fun onFavouriteButtonClick(v: View?)
    fun onShareButtonClick(v: View?)
    fun onSoundButtonClick(v: View?)

    fun onSuccess(dictionaryWordDataResponse: DictionaryWordDataResponse)

    fun onAddFavouriteSuccess(dictionaryFavouriteResponse: FavouriteResponse)

    fun onRemoveFavouriteSuccess(dictionaryFavouriteResponse: FavouriteResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}