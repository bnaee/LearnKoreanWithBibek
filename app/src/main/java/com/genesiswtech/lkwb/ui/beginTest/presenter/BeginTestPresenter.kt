package com.genesiswtech.lkwb.ui.beginTest.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestResponse
import com.genesiswtech.lkwb.ui.beginTest.view.IBeginTestView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class BeginTestPresenter(
    private var beginTestView: IBeginTestView,
    var applicationComponent: Application
) :
    IBeginTestPresenter, BasePresenter<IBeginTestView>(beginTestView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getUBTSet(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            beginTestView.onShowProgressBar(true)
            mNetworkApi.getUBTSet(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BeginTestResponse>? ->
                    beginTestView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { beginTestView.onSuccess(it) }
                    } else {
                        beginTestView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    beginTestView.onShowProgressBar(false)
                    beginTestView.onFailure(error.localizedMessage)
                })

        } else {
            beginTestView.onShowProgressBar(false)
            beginTestView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}