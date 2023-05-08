package com.genesiswtech.lkwb.ui.setting.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.setting.view.ISettingView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class SettingPresenter(var settingView: ISettingView, var applicationComponent: Application) :
    ISettingPresenter, BasePresenter<ISettingView>(settingView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postLogOut(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            settingView.onShowProgressBar(true)
            mNetworkApi.postLogOut().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<LogOutResponse>? ->
                    settingView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { settingView.onLogOutSuccess(it) }
                    } else {
                        settingView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    settingView.onShowProgressBar(false)
                    settingView.onFailure(error.localizedMessage)
                })

        } else {
            settingView.onShowProgressBar(false)
            settingView.showSnackBar(context.getString(R.string.no_internet))
        }


    }


}


