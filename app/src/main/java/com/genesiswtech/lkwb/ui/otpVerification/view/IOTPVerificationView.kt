package com.genesiswtech.lkwb.ui.otpVerification.view

import android.text.TextWatcher
import android.view.View
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.AccountDeleteResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.OTPVerificationResponse
import retrofit2.Response

interface IOTPVerificationView {

    fun initDependencies()

    fun onOTPError()
    fun onOTPVerifyClick(v: View?)
    fun onOTPResendClick(v: View?)
    fun onFirstOTPTextWatcher(): TextWatcher
    fun onSecondOTPTextWatcher(): TextWatcher
    fun onThirdOTPTextWatcher(): TextWatcher
    fun onFourthOTPTextWatcher(): TextWatcher

    fun onSuccess(otpVerificationResponse: OTPVerificationResponse)

    fun onUserVerificationSuccess(otpVerificationResponse: OTPVerificationResponse)

    fun onAccountDeleteSuccess(accountDeleteResponse: AccountDeleteResponse)

    fun onResendEmailSuccess(forgotPasswordResponse: Response<ForgotPasswordResponse>?)

    fun onResendOTPSuccess(forgotPasswordResponse: Response<ForgotPasswordResponse>?)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}