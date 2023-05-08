package com.genesiswtech.lkwb.ui.newPassword.view

import android.view.View
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.newPassword.model.NewPasswordResponse

interface INewPasswordView {

    fun initDependencies()

    fun onResetPasswordClick(v: View?)

    fun onPasswordToggleClick(v: View?)
    fun onConfirmPasswordToggleClick(v: View?)

    fun onConfirmPasswordError()

    fun onPasswordError()

    fun onPasswordMismatchError()

    fun onSuccess(newPasswordResponse: NewPasswordResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}