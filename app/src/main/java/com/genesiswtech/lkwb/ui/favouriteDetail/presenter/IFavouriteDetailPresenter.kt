package com.genesiswtech.lkwb.ui.favouriteDetail.presenter

import android.content.Context

interface IFavouriteDetailPresenter {
    fun getFavouriteBlog(context: Context)
    fun getFavouriteGrammar(context: Context)
    fun getFavouriteDictionary(context: Context)
}