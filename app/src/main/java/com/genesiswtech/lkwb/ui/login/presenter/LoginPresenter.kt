package com.genesiswtech.lkwb.ui.login.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.login.view.ILoginView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class LoginPresenter(private var loginView: ILoginView, var applicationComponent: Application) :
    ILoginPresenter, BasePresenter<ILoginView>(loginView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postLogin(context: Context, email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            loginView.onEmailError()
        } else
            if (TextUtils.isEmpty(password) || password.length < 6) {
                loginView.onPasswordError()
            } else if (!AppUtils.isValidEmail(email)) {
                loginView.onInvalidEmailError()
            } else
                if (InternetConnection.checkForInternet(context)) {
                    loginView.hideKeyboard()
                    loginView.onShowProgressBar(true)
                    mNetworkApi.postLogin(email, password).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response: Response<LoginResponse>? ->
                            loginView.onShowProgressBar(false)
                            if (response!!.isSuccessful) {
                                response.body()?.let { loginView.onLoginSuccess(it) }
                            } else {
                                loginView.onLoginFailure(AppUtils.getErrorMessage(response.errorBody()))
                            }

                        }, { error ->
                            loginView.onShowProgressBar(false)
                            loginView.onLoginFailure(error.localizedMessage)
                        })

                } else {
                    loginView.onShowProgressBar(false)
                    loginView.showSnackBar(context.getString(R.string.no_internet))
                }
    }

    override fun postSocialLogin(
        context: Context,
        type: String,
        providerId: String,
        email: String,
        name: String,
        photo: String, referCode: String
    ) {
//        "https://graph.facebook.com/" + userID+ "/picture?type=large"
        if (InternetConnection.checkForInternet(context)) {
            loginView.onShowProgressBar(true)
            mNetworkApi.postLoginSocial(type, providerId, email, name, photo, referCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<LoginResponse>? ->
                    loginView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { loginView.onLoginSuccess(it) }
                    } else {
                        loginView.onLoginFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    loginView.onShowProgressBar(false)
                    loginView.onLoginFailure(error.localizedMessage)
                })

        } else {
            loginView.onShowProgressBar(false)
            loginView.showSnackBar(context.getString(R.string.no_internet))
        }


    }
}


