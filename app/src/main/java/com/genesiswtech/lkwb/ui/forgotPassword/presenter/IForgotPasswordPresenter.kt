package com.genesiswtech.lkwb.ui.forgotPassword.presenter

import android.content.Context

interface IForgotPasswordPresenter {

    fun postForgotPassword(context: Context,email: String)
}