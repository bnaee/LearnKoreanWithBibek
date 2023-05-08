package com.genesiswtech.lkwb.ui.newPassword.presenter

import android.content.Context

interface INewPasswordPresenter {

    fun postNewPassword(context: Context, password: String, confirm_password:String)
}