package com.genesiswtech.lkwb.ui.blog.view

import com.genesiswtech.lkwb.ui.blog.model.BlogResponse

interface IBlogView {

    fun initDependencies()

    fun onSuccess(blogResponse: BlogResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}