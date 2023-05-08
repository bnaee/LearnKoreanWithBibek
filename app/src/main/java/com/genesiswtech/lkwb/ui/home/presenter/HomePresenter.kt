package com.genesiswtech.lkwb.ui.home.presenter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.firebase.data.StorePushTokenResponse
import com.genesiswtech.lkwb.ui.home.model.*
import com.genesiswtech.lkwb.ui.home.view.IHomeView
import com.genesiswtech.lkwb.ui.otpVerification.model.AccountDeleteResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class HomePresenter(private var homeView: IHomeView, var applicationComponent: Application) :
    IHomePresenter, BasePresenter<IHomeView>(homeView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getMostBought(context: Context, page: String, limit: String) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.getMostBought(page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<MostBoughtResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { homeView.onMostBoughtSuccess(it) }
                    } else {
                        homeView.onShowProgressBar(false)
                        homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })

        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getNewTest(context: Context, page: String, limit: String) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.getNewTest(page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<MostBoughtResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { homeView.onNewTestSuccess(it) }
                    } else {
                        if (response.code() == 422) {
                            AppUtils.logOutCall(context as Activity)
                        } else {
                            homeView.onShowProgressBar(false)
                            homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }

                    }
                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })

        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun getServices(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.getServices().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ServiceResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { homeView.onServicesSuccess(it) }
                    } else {
                        homeView.onShowProgressBar(false)
                        homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })

        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getResultHistoryApp(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.getResultHistoryApp().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ResultHistoryResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { homeView.onResultHistoryAppSuccess(it.data) }
                    } else {
                        homeView.onShowProgressBar(false)
                        homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })

        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun getBanner(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.getBanner().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BannerResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { homeView.onBannerSuccess(it) }
                    } else {
                        homeView.onShowProgressBar(false)
                        homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })

        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getLastTestScore(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.getLastTestScore().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<TestScoreResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { homeView.onLastTestScoreSuccess(it) }
                    } else {
                        homeView.onShowProgressBar(false)
                        homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })

        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postPushToken(context: Context, deviceId: String, deviceType: String) {
        if (InternetConnection.checkForInternet(context)) {
            homeView.onShowProgressBar(true)
            mNetworkApi.postDeviceToken(deviceId, deviceType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<StorePushTokenResponse>? ->
                    homeView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        LKWBPreferencesManager.putString(
                            LKWBConstants.PUSH_TOKEN,
                            response.body()!!.deviceId.toString()
                        )
                    } else {
                        homeView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    homeView.onShowProgressBar(false)
                    homeView.onFailure(error.localizedMessage)
                })
        } else {
            homeView.onShowProgressBar(false)
            homeView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}