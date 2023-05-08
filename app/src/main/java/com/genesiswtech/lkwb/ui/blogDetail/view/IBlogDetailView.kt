package com.genesiswtech.lkwb.ui.blogDetail.view

import android.view.View
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.ui.blogDetail.model.BlogDetailDataResponse

interface IBlogDetailView {

    fun onBlogShareClick(v: View?)
    fun onBlogFavouriteClick(v: View?)
    fun onCommentCountClick(v: View?)

    fun initDependencies()

    fun onSuccess(blogDetailDataResponse: BlogDetailDataResponse)

    fun onAddFavouriteSuccess(blogFavouriteResponse: FavouriteResponse)

    fun onRemoveFavouriteSuccess(blogFavouriteResponse: FavouriteResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

}