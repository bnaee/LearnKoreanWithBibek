package com.genesiswtech.lkwb.ui.profile.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.profile.model.ProfileResponse
import com.genesiswtech.lkwb.ui.profile.model.UpdateProfileResponse
import com.genesiswtech.lkwb.ui.profile.view.IProfileView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class ProfilePresenter(var profileView: IProfileView, var applicationComponent: Application) :
    IProfilePresenter, BasePresenter<IProfileView>(profileView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getProfile(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            profileView.onShowProgressBar(true)
            mNetworkApi.getProfile().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ProfileResponse>? ->
                    profileView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { profileView.onSuccess(it.data) }
                    } else {
                        profileView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    profileView.onShowProgressBar(false)
                    profileView.onFailure(error.localizedMessage)
                })

        } else {
            profileView.onShowProgressBar(false)
            profileView.showSnackBar(context.getString(R.string.no_internet))
        }


    }

    override fun postLogOut(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            profileView.onShowProgressBar(true)
            mNetworkApi.postLogOut().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<LogOutResponse>? ->
                    profileView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { profileView.onLogOutSuccess(it) }
                    } else {
                        profileView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    profileView.onShowProgressBar(false)
                    profileView.onFailure(error.localizedMessage)
                })

        } else {
            profileView.onShowProgressBar(false)
            profileView.showSnackBar(context.getString(R.string.no_internet))
        }


    }

    override fun putUpdateProfile(
        context: Context,
        name: String,
        password: String,
        confirm_password: String,
    ) {
        if (InternetConnection.checkForInternet(context)) {
            profileView.onShowProgressBar(true)
            mNetworkApi.putUpdateProfile(name, "PUT", password, confirm_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UpdateProfileResponse>? ->
                    profileView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { profileView.onProfileUpdateSuccess(it) }
                    } else {
                        profileView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    profileView.onShowProgressBar(false)
                    profileView.onFailure(error.localizedMessage)
                })

        } else {
            profileView.onShowProgressBar(false)
            profileView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun putUpdateFullName(
        context: Context,
        name: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            profileView.onShowProgressBar(true)
            mNetworkApi.putUpdateFullName(name, "PUT")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UpdateProfileResponse>? ->
                    profileView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { profileView.onProfileUpdateSuccess(it) }
                    } else {
                        profileView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    profileView.onShowProgressBar(false)
                    profileView.onFailure(error.localizedMessage)
                })

        } else {
            profileView.onShowProgressBar(false)
            profileView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun putUpdateProfilePicture(
        context: Context,
        filePath: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            profileView.onShowProgressBar(true)
            val file = File(filePath)

//            val requestFile = RequestBody.create(
//                MediaType.parse("image/jpg"),
//                file
//            )
            val requestFile = RequestBody.create(
                "image/jpg".toMediaTypeOrNull(),
                file
            )



            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//            val stringValue: RequestBody =
//                RequestBody.create(MediaType.parse("text/plain"), "PUT")
            val stringValue: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), "PUT")
            mNetworkApi.putUpdateProfilePicture(body, stringValue).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UpdateProfileResponse>? ->
                    profileView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { profileView.onProfileUpdateSuccess(it) }
                    } else {
                        profileView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    profileView.onShowProgressBar(false)
                    if (error.localizedMessage == "timeout")
                        profileView.onTimeOut()
                    else
                        profileView.onFailure(error.localizedMessage)
                })

        } else {
            profileView.onShowProgressBar(false)
            profileView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}


