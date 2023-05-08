package com.genesiswtech.lkwb.ui.newPassword.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.newPassword.model.NewPasswordResponse
import com.genesiswtech.lkwb.ui.newPassword.view.INewPasswordView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class NewPasswordPresenter(
    var newPasswordView: INewPasswordView,
    var applicationComponent: Application
) :
    INewPasswordPresenter, BasePresenter<INewPasswordView>(newPasswordView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postNewPassword(context: Context, password: String, confirm_password: String) {
        if (TextUtils.isEmpty(password)) {
            newPasswordView.onPasswordError()
        } else
            if (TextUtils.isEmpty(confirm_password)) {
                newPasswordView.onConfirmPasswordError()
            } else if (confirm_password != password) {
                newPasswordView.onPasswordMismatchError()
            } else
                if (InternetConnection.checkForInternet(context)) {
                    newPasswordView.onShowProgressBar(true)
                    mNetworkApi.postNewPassword(password, confirm_password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response: Response<NewPasswordResponse>? ->
                            newPasswordView.onShowProgressBar(false)
                            if (response!!.isSuccessful) {
                                response.body()?.let { newPasswordView.onSuccess(it) }
                            } else {
                                newPasswordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                            }

                        }, { error ->
                            newPasswordView.onShowProgressBar(false)
                            newPasswordView.onFailure(error.localizedMessage)
                        })

                } else {
                    newPasswordView.onShowProgressBar(false)
                    newPasswordView.showSnackBar(context.getString(R.string.no_internet))
                }
    }


}


