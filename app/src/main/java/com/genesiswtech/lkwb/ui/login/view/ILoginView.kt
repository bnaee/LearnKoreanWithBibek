package com.genesiswtech.lkwb.ui.login.view

import android.view.View
import com.genesiswtech.lkwb.ui.login.model.LoginResponse

interface ILoginView {

    fun initDependencies()

    fun onLoginButtonClick(v: View?)
    fun onFBLoginButtonClick(v: View?)
    fun onGoogleLoginButtonClick(v: View?)
    fun onForgotPasswordClick(v: View?)
    fun onRegisterClick(v: View?)
    fun onPasswordToggleClick(v: View?)


    fun hideKeyboard()

    fun onEmailError()

    fun onPasswordError()

    fun onInvalidEmailError()

    fun onLoginSuccess(loginResponse: LoginResponse)

    fun onLoginFailure(message: String?)

    fun onLoginTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}