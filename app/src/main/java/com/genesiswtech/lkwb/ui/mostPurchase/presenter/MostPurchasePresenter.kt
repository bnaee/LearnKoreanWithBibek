package com.genesiswtech.lkwb.ui.mostPurchase.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.home.model.MostBoughtResponse
import com.genesiswtech.lkwb.ui.mostPurchase.model.PackageListResponse
import com.genesiswtech.lkwb.ui.mostPurchase.view.IMostPurchaseView
import com.genesiswtech.lkwb.ui.ubt.model.PackageResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class MostPurchasePresenter(
    private var mostPurchaseView: IMostPurchaseView,
    var applicationComponent: Application
) :
    IMostPurchaseInterface, BasePresenter<IMostPurchaseView>(mostPurchaseView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getMostBought(context: Context, page: String, limit: String) {
        if (InternetConnection.checkForInternet(context)) {
            mostPurchaseView.onChooseProgressBar(page.toInt(), true)
            mNetworkApi.getMostBought(page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<MostBoughtResponse>? ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { mostPurchaseView.onMostPurchaseSuccess(it) }
                    } else {
                        mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                        mostPurchaseView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    mostPurchaseView.onFailure(error.localizedMessage)
                })

        } else {
            mostPurchaseView.onChooseProgressBar(page.toInt(), false)
            mostPurchaseView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getNewSets(context: Context, page: String, limit: String) {
        if (InternetConnection.checkForInternet(context)) {
            mostPurchaseView.onChooseProgressBar(page.toInt(), true)
            mNetworkApi.getNewTest(page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<MostBoughtResponse>? ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { mostPurchaseView.onMostPurchaseSuccess(it) }
                    } else {
                        mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                        mostPurchaseView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    mostPurchaseView.onFailure(error.localizedMessage)
                })

        } else {
            mostPurchaseView.onChooseProgressBar(page.toInt(), false)
            mostPurchaseView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getPackageList(context: Context, id: Int, page: String, limit: String) {
        if (InternetConnection.checkForInternet(context)) {
            mostPurchaseView.onChooseProgressBar(page.toInt(), true)
            mNetworkApi.getPackageList(id, "purchased", page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<PackageListResponse>? ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    if (response!!.isSuccessful) {
                        response.body()?.let {
                            it.data?.let { it1 ->
                                mostPurchaseView.onPackageSuccess(
                                    it1
                                )
                            }
                        }
                    } else {
                        mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                        mostPurchaseView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    mostPurchaseView.onFailure(error.localizedMessage)
                })

        } else {
            mostPurchaseView.onChooseProgressBar(page.toInt(), false)
            mostPurchaseView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun getUBTTest(
        context: Context,
        page: String,
        limit: String,
        type: String,
        sort: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            mostPurchaseView.onChooseProgressBar(page.toInt(), true)
            mNetworkApi.getUBTTest(page, limit, type, sort).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UBTTestResponse>? ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { mostPurchaseView.onSuccess(it) }
                    } else {
                        mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                        mostPurchaseView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    mostPurchaseView.onChooseProgressBar(page.toInt(), false)
                    mostPurchaseView.onFailure(error.localizedMessage)
                })

        } else {
            mostPurchaseView.onChooseProgressBar(page.toInt(), false)
            mostPurchaseView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}