package com.genesiswtech.lkwb.ui.ubt.presenter

import android.content.Context

interface IUBTTestPresenter {
    fun getUBTTest(context: Context,page:String,limit:String,type:String,sort:String)
    fun getAllPackages(context: Context)
}