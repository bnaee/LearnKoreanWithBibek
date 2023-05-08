package com.genesiswtech.lkwb.ui.otpVerification.presenter

import android.content.Context

interface IOTPVerificationPresenter {

    fun postOTPVerification(context: Context, email: String, otp: String)

    fun postNewUserOTPVerification(context: Context, otp: String)

    fun postResendEmail(context: Context, email: String)

    fun postResendOTP(context: Context, email: String)

    fun postDeleteAccount(context: Context, email: String, otp: String)
}