package com.genesiswtech.lkwb.ui.profile.view

import android.view.View
import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.profile.model.ProfileDataResponse
import com.genesiswtech.lkwb.ui.profile.model.UpdateProfileResponse

interface IProfileView {


    fun initDependencies()
    fun onSettingCLClick(v: View?)
    fun onEditFullNameClick(v: View?)
    fun onEditPasswordClick(v: View?)
    fun onDeleteAccountClick(v: View?)
    fun onLogOutClick(v: View?)

    fun onSuccess(profileData: LoginDataResponse?)

    fun onLogOutSuccess(logOutResponse: LogOutResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onProfileUpdateSuccess(updateProfileDataResponse: UpdateProfileResponse)
}