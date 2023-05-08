package com.genesiswtech.lkwb.ui.forgotPassword.view

import android.view.View
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import retrofit2.Response

interface IForgotPasswordView {

    fun initDependencies()

    fun onSendCodeClick(v: View?)

    fun onEmailError()

    fun onInvalidEmailError()

    fun onSuccess(forgotPasswordResponse: Response<ForgotPasswordResponse>?)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}