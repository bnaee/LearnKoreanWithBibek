package com.genesiswtech.lkwb.ui.ubt.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.ubt.model.PackageResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse
import com.genesiswtech.lkwb.ui.ubt.view.IUBTTestView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class UBTTestPresenter(
    private var ubtTestView: IUBTTestView,
    var applicationComponent: Application
) :
    IUBTTestPresenter, BasePresenter<IUBTTestView>(ubtTestView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getUBTTest(
        context: Context,
        page: String,
        limit: String,
        type: String,
        sort: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            ubtTestView.onChooseProgressBar(page.toInt(), true)
            mNetworkApi.getUBTTest(page, limit, type, sort).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UBTTestResponse>? ->
                    ubtTestView.onChooseProgressBar(page.toInt(), false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { ubtTestView.onSuccess(it) }
                    } else {
                        ubtTestView.onChooseProgressBar(page.toInt(), false)
                        ubtTestView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    ubtTestView.onChooseProgressBar(page.toInt(), false)
                    ubtTestView.onFailure(error.localizedMessage)
                })

        } else {
            ubtTestView.onChooseProgressBar(page.toInt(), false)
            ubtTestView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getAllPackages(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            ubtTestView.onShowProgressBar(true)
            mNetworkApi.getAllPackages().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<PackageResponse>? ->
                    ubtTestView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { ubtTestView.onPackageSuccess(it.data) }
                    } else {
                        ubtTestView.onShowProgressBar(false)
                        ubtTestView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    ubtTestView.onShowProgressBar(false)
                    ubtTestView.onFailure(error.localizedMessage)
                })

        } else {
            ubtTestView.onShowProgressBar(false)
            ubtTestView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}