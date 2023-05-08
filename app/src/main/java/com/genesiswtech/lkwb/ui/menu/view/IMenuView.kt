package com.genesiswtech.lkwb.ui.menu.view

import android.view.View
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse

interface IMenuView {

    fun initDependencies()
    fun onCloseClick(v: View?)
    fun onUsefulLinksClick(v: View?)
    fun onPrivacyPolicyClick(v: View?)
    fun onTermsConditionsClick(v: View?)
    fun onFAQClick(v: View?)
    fun onContactUsClick(v: View?)
    fun onGuideClick(v: View?)
    fun onWordCardClick(v: View?)
    fun onLogOutClick(v: View?)

    fun onLogOutSuccess(logOutResponse: LogOutResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}