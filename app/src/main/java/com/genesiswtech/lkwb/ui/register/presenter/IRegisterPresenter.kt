package com.genesiswtech.lkwb.ui.register.presenter

import android.content.Context
import java.io.File

interface IRegisterPresenter {
    fun postRegister(
        context: Context,
        name: String,
        email: String,
        password: String,
        confirm_password: String,
        filePath: String,
        referCode: String
    )

    fun postSocialLogin(
        context: Context,
        type: String,
        providerId: String,
        email: String,
        name: String,
        photo: String,
        referCode: String
    )
}