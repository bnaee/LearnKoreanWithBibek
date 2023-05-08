package com.genesiswtech.lkwb.ui.register.view

import android.view.View
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.register.model.RegisterResponse

interface IRegisterView {
    fun initDependencies()

    fun onRegisterButtonClick(v: View?)
    fun onFBLoginButtonClick(v: View?)
    fun onGoogleLoginButtonClick(v: View?)
    fun onLoginClick(v: View?)
    fun onRegisterCLClick(v: View?)
    fun hideKeyboard()
    fun onPasswordToggleClick(v: View?)
    fun onConfirmPasswordToggleClick(v: View?)

    fun onNameError()

    fun onEmailError()

    fun onInvalidEmailError()

    fun onConfirmPasswordError()

    fun onPasswordMisMatchError()

    fun onPasswordError()

    fun onImageError()

    fun onSuccess(registerResponse: RegisterResponse)

    fun onLoginSuccess(loginResponse: LoginResponse)

    fun onLoginFailure(message: String?)

    fun onLoginTimeOut()

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}