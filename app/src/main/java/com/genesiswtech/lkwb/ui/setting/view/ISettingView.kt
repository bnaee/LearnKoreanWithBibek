package com.genesiswtech.lkwb.ui.setting.view

import android.view.View
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.profile.model.UpdateProfileResponse

interface ISettingView {

    fun initDependencies()

    fun onLogOutClick(v: View?)

    fun onLogOutSuccess(logOutResponse: LogOutResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

}