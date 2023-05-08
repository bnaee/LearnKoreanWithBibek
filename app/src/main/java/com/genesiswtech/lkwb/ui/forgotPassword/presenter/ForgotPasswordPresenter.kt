package com.genesiswtech.lkwb.ui.forgotPassword.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import com.genesiswtech.lkwb.ui.forgotPassword.view.IForgotPasswordView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class ForgotPasswordPresenter(
    private var forgotPasswordView: IForgotPasswordView,
    var applicationComponent: Application
) :
    IForgotPasswordPresenter, BasePresenter<IForgotPasswordView>(forgotPasswordView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postForgotPassword(context: Context, email: String) {
        if (TextUtils.isEmpty(email)) {
            forgotPasswordView.onEmailError()
        } else if (!AppUtils.isValidEmail(email)
        ) {
            forgotPasswordView.onInvalidEmailError()
        } else
            if (InternetConnection.checkForInternet(context)) {
                forgotPasswordView.onShowProgressBar(true)
                mNetworkApi.postForgotPassword(email).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<ForgotPasswordResponse>? ->
                        forgotPasswordView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            forgotPasswordView.onSuccess(response)
                        } else {
                            forgotPasswordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }
                    }, { error ->
                        forgotPasswordView.onShowProgressBar(false)
                        forgotPasswordView.onFailure(error.localizedMessage)
                    })

            } else {
                forgotPasswordView.onShowProgressBar(false)
                forgotPasswordView.showSnackBar(context.getString(R.string.no_internet))
            }
    }


}


