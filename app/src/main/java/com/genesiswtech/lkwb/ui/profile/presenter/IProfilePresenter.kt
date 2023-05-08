package com.genesiswtech.lkwb.ui.profile.presenter

import android.content.Context

interface IProfilePresenter {
    fun postLogOut(context: Context)

    fun putUpdateProfile(context: Context, name: String, password: String, confirm_password: String)

    fun putUpdateFullName(context: Context, name: String)

    fun putUpdateProfilePicture(context: Context, file: String)

    fun getProfile(context: Context)
}