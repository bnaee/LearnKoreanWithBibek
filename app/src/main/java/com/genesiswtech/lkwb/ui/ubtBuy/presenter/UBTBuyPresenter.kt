package com.genesiswtech.lkwb.ui.ubtBuy.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.ubtBuy.model.UBTBuyResponse
import com.genesiswtech.lkwb.ui.ubtBuy.view.IUBTBuyView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class UBTBuyPresenter(
    private var ubtBuyView: IUBTBuyView,
    var applicationComponent: Application
) :
    IUBTBuyPresenter, BasePresenter<IUBTBuyView>(ubtBuyView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun postUBTBuy(
        context: Context,
        sets: String,
        id: Int,
        type: String,
        oid: String,
        amt: String,
        refId: String, mobile: Boolean
    ) {
        if (InternetConnection.checkForInternet(context)) {
            ubtBuyView.onShowProgressBar(true)
            mNetworkApi.postUBTBuy(sets, id, type, oid, amt, refId, mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UBTBuyResponse>? ->
                    ubtBuyView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { ubtBuyView.onSuccess(it) }
                    } else {
                        ubtBuyView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    ubtBuyView.onShowProgressBar(false)
                    ubtBuyView.onFailure(error.localizedMessage)
                })

        } else {
            ubtBuyView.onShowProgressBar(false)
            ubtBuyView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun postKhaltiUBTBuy(
        context: Context,
        sets: String,
        id: Int,
        type: String,
        amt: String,
        isMobile: Boolean,
        pidx: String,
        poid: String,
        pon: String,
        txn_id: String,
        mobile: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            ubtBuyView.onShowProgressBar(true)
            mNetworkApi.postUBTKhaltiBuy(
                sets,
                id,
                type,
                amt,
                isMobile,
                pidx,
                poid,
                pon,
                txn_id,
                mobile
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UBTBuyResponse>? ->
                    ubtBuyView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { ubtBuyView.onSuccess(it) }
                    } else {
                        ubtBuyView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    ubtBuyView.onShowProgressBar(false)
                    ubtBuyView.onFailure(error.localizedMessage)
                })

        } else {
            ubtBuyView.onShowProgressBar(false)
            ubtBuyView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}