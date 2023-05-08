package com.genesiswtech.lkwb.ui.blog.presenter

import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse

interface CustomClickListener {
    fun cardClicked(f: BlogDataResponse?)
}