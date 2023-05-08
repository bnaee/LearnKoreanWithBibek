package com.genesiswtech.lkwb.ui.otpVerification.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.AccountDeleteResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.OTPVerificationResponse
import com.genesiswtech.lkwb.ui.otpVerification.view.IOTPVerificationView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class OTPVerificationPresenter(
    private var otpVerificationView: IOTPVerificationView,
    var applicationComponent: Application
) :
    IOTPVerificationPresenter, BasePresenter<IOTPVerificationView>(otpVerificationView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postOTPVerification(context: Context, email: String, otp: String) {
        if (otp.length < 4) {
            otpVerificationView.onOTPError()
        } else
            if (InternetConnection.checkForInternet(context)) {
                otpVerificationView.onShowProgressBar(true)
                mNetworkApi.postVerifyOTP(email, otp).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<OTPVerificationResponse>? ->
                        otpVerificationView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            response.body()?.let { otpVerificationView.onSuccess(it) }
                        } else {
                            otpVerificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }

                    }, { error ->
                        otpVerificationView.onShowProgressBar(false)
                        otpVerificationView.onFailure(error.localizedMessage)
                    })

            } else {
                otpVerificationView.onShowProgressBar(false)
                otpVerificationView.showSnackBar(context.getString(R.string.no_internet))
            }
    }

    override fun postNewUserOTPVerification(context: Context, otp: String) {
        if (otp.length < 4) {
            otpVerificationView.onOTPError()
        } else
            if (InternetConnection.checkForInternet(context)) {
                otpVerificationView.onShowProgressBar(true)
                mNetworkApi.postNewUserVerifyOTP(otp).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<OTPVerificationResponse>? ->
                        otpVerificationView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            response.body()?.let { otpVerificationView.onUserVerificationSuccess(it) }
                        } else {
                            otpVerificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }

                    }, { error ->
                        otpVerificationView.onShowProgressBar(false)
                        otpVerificationView.onFailure(error.localizedMessage)
                    })

            } else {
                otpVerificationView.onShowProgressBar(false)
                otpVerificationView.showSnackBar(context.getString(R.string.no_internet))
            }
    }

    override fun postResendEmail(context: Context, email: String) {
        if (InternetConnection.checkForInternet(context)) {
            otpVerificationView.onShowProgressBar(true)
            mNetworkApi.postForgotPassword(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ForgotPasswordResponse>? ->
                    otpVerificationView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        otpVerificationView.onResendEmailSuccess(response)
                    } else {
                        otpVerificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    otpVerificationView.onShowProgressBar(false)
                    otpVerificationView.onFailure(error.localizedMessage)
                })

        } else {
            otpVerificationView.onShowProgressBar(false)
            otpVerificationView.showSnackBar(context.getString(R.string.no_internet))
        }


    }

    override fun postResendOTP(context: Context, email: String) {
        if (InternetConnection.checkForInternet(context)) {
            otpVerificationView.onShowProgressBar(true)
            mNetworkApi.postResendOTP(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ForgotPasswordResponse>? ->
                    otpVerificationView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        otpVerificationView.onResendEmailSuccess(response)
                    } else {
                        otpVerificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    otpVerificationView.onShowProgressBar(false)
                    otpVerificationView.onFailure(error.localizedMessage)
                })

        } else {
            otpVerificationView.onShowProgressBar(false)
            otpVerificationView.showSnackBar(context.getString(R.string.no_internet))
        }


    }

    override fun postDeleteAccount(context: Context,email: String, otp: String) {
        if (otp.length < 4) {
            otpVerificationView.onOTPError()
        } else
            if (InternetConnection.checkForInternet(context)) {
                otpVerificationView.onShowProgressBar(true)
                mNetworkApi.postDeleteAccount("DELETE",email, otp).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<AccountDeleteResponse>? ->
                        otpVerificationView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            response.body()?.let { otpVerificationView.onAccountDeleteSuccess(it) }
                        } else {
                            otpVerificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }

                    }, { error ->
                        otpVerificationView.onShowProgressBar(false)
                        otpVerificationView.onFailure(error.localizedMessage)
                    })

            } else {
                otpVerificationView.onShowProgressBar(false)
                otpVerificationView.showSnackBar(context.getString(R.string.no_internet))
            }
    }
}


