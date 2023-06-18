package com.genesiswtech.lkwb.ui.menu.presenter

import android.content.Context

interface IMenuPresenter {

    fun postLogOut(context: Context)

    fun postReclaim(context: Context,transaction_id: String,gateway:String)
}