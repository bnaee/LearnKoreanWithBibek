package com.genesiswtech.lkwb.ui.login.presenter

import android.content.Context


interface ILoginPresenter {

    fun postLogin(context: Context, email: String, password: String)

    fun postSocialLogin(
        context: Context,
        type:String,
        providerId: String,
        email: String,
        name: String,
        photo: String,
        referCode:String
    )
}