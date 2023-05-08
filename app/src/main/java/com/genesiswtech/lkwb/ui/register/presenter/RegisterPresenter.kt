package com.genesiswtech.lkwb.ui.register.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.register.model.RegisterResponse
import com.genesiswtech.lkwb.ui.register.view.IRegisterView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RegisterPresenter(
    private var registerView: IRegisterView,
    var applicationComponent: Application
) :
    IRegisterPresenter, BasePresenter<IRegisterView>(registerView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postRegister(
        context: Context,
        name: String,
        email: String,
        password: String,
        confirm_password: String,
        filePath: String,
        referCode: String
    ) {
        if (TextUtils.isEmpty(name)) {
            registerView.onNameError()
        } else if (TextUtils.isEmpty(email)) {
            registerView.onEmailError()
        } else if (!AppUtils.isValidEmail(email)) {
            registerView.onInvalidEmailError()
        } else if (TextUtils.isEmpty(password)) {
            registerView.onPasswordError()
        } else if (TextUtils.isEmpty(confirm_password)) {
            registerView.onConfirmPasswordError()
        } else if (password != confirm_password) {
            registerView.onPasswordMisMatchError()
        } else if (InternetConnection.checkForInternet(context)) {
            registerView.hideKeyboard()
            registerView.onShowProgressBar(true)
            var body: MultipartBody.Part? = null
            val file = File(filePath)
            if (file.exists()) {
//                val requestFile = RequestBody.create(
//                    MediaType.parse("image/jpg"),
//                    file
//                )
                val requestFile = RequestBody.create(
                    "image/jpg".toMediaTypeOrNull(),
                    file
                )
                body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
//            val nameValue: RequestBody =
//                RequestBody.create(MediaType.parse("text/plain"), name)
//            val emailValue: RequestBody =
//                RequestBody.create(MediaType.parse("text/plain"), email)
//            val passwordValue: RequestBody =
//                RequestBody.create(MediaType.parse("text/plain"), password)
//            val confirmPasswordValue: RequestBody =
//                RequestBody.create(MediaType.parse("text/plain"), confirm_password)
//            val referCodeValue: RequestBody =
//                RequestBody.create(MediaType.parse("text/plain"), referCode)

            val nameValue: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), name)
            val emailValue: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), email)
            val passwordValue: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), password)
            val confirmPasswordValue: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), confirm_password)
            val referCodeValue: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), referCode)
            mNetworkApi.postRegister(
                nameValue,
                emailValue,
                passwordValue,
                confirmPasswordValue,
                body, referCodeValue
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<RegisterResponse>? ->
                    registerView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { registerView.onSuccess(it) }
                    } else {
                        registerView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    registerView.onShowProgressBar(false)
                    registerView.onFailure(error.localizedMessage)
                })

        } else {
            registerView.onShowProgressBar(false)
            registerView.showSnackBar(context.getString(R.string.no_internet))
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
            registerView.onShowProgressBar(true)
            mNetworkApi.postLoginSocial(type, providerId, email, name, photo, referCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<LoginResponse>? ->
                    registerView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { registerView.onLoginSuccess(it) }
                    } else {
                        registerView.onLoginFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    registerView.onShowProgressBar(false)
                    registerView.onLoginFailure(error.localizedMessage)
                })

        } else {
            registerView.onShowProgressBar(false)
            registerView.showSnackBar(context.getString(R.string.no_internet))
        }


    }


}


