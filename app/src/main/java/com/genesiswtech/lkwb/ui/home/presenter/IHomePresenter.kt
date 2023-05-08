package com.genesiswtech.lkwb.ui.home.presenter

import android.content.Context

interface IHomePresenter {

    fun getMostBought(context: Context,page:String,limit:String)
    fun getNewTest(context: Context,page:String,limit:String)
    fun getServices(context: Context)
    fun getResultHistoryApp(context: Context)
    fun getBanner(context: Context)
    fun getLastTestScore(context: Context)
    fun postPushToken(context: Context,deviceId:String,deviceType:String)
}