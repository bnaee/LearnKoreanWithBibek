package com.genesiswtech.lkwb.ui.grammarWord.view

import android.view.View
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordDataResponse
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse

interface IGrammarWordView {

    fun initDependencies()

    fun onWordSoundButtonClick(v: View?)
    fun onWordShareButtonClick(v: View?)
    fun onWordFavouriteButtonClick(v: View?)

    fun onSuccess(GrammarWordDataResponse: GrammarWordDataResponse)

    fun onAddFavouriteSuccess(grammarFavouriteResponse: FavouriteResponse)

    fun onRemoveFavouriteSuccess(grammarFavouriteResponse: FavouriteResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}