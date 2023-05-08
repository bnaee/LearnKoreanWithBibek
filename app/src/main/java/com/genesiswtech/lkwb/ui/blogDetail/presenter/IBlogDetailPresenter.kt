package com.genesiswtech.lkwb.ui.blogDetail.presenter

import android.content.Context

interface IBlogDetailPresenter {

    fun getSingleBlog(context: Context, id: Int)
    fun postAddFavouriteBlog(context: Context, id: Int, type: String)
    fun postRemoveFavouriteBlog(context: Context, id: Int, type: String)
}