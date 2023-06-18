package com.genesiswtech.lkwb.ui.menu.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.menu.model.ReclaimResponse
import com.genesiswtech.lkwb.ui.menu.view.IMenuView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class MenuPresenter(private var menuView: IMenuView, var applicationComponent: Application) :
    IMenuPresenter, BasePresenter<IMenuView>(menuView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postLogOut(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            menuView.onShowProgressBar(true)
            mNetworkApi.postLogOut().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<LogOutResponse>? ->
                    menuView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { menuView.onLogOutSuccess(it) }
                    } else {
                        menuView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    menuView.onShowProgressBar(false)
                    menuView.onFailure(error.localizedMessage)
                })

        } else {
            menuView.onShowProgressBar(false)
            menuView.showSnackBar(context.getString(R.string.no_internet))
        }

    }

    override fun postReclaim(context: Context, transaction_id: String, gateway: String) {
        if (InternetConnection.checkForInternet(context)) {
            menuView.onShowProgressBar(true)
            mNetworkApi.postReclaim(transaction_id, gateway).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ReclaimResponse>? ->
                    menuView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { menuView.onReclaimSuccess(it) }
                    } else {
                        menuView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }

                }, { error ->
                    menuView.onShowProgressBar(false)
                    menuView.onFailure(error.localizedMessage)
                })

        } else {
            menuView.onShowProgressBar(false)
            menuView.showSnackBar(context.getString(R.string.no_internet))
        }
    }
}


