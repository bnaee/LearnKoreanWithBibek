package com.genesiswtech.lkwb.ui.graph.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.graph.view.IGraphView
import com.genesiswtech.lkwb.ui.home.model.ResultHistoryResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class GraphPresenter(private var graphView: IGraphView, var applicationComponent: Application) :
    IGraphPresenter, BasePresenter<IGraphView>(graphView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getResultHistoryApp(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            graphView.onShowProgressBar(true)
            mNetworkApi.getResultHistoryApp().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ResultHistoryResponse>? ->
                    graphView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { graphView.onResultHistoryAppSuccess(it.data) }
                    } else {
                        graphView.onShowProgressBar(false)
                        graphView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    graphView.onShowProgressBar(false)
                    graphView.onFailure(error.localizedMessage)
                })

        } else {
            graphView.onShowProgressBar(false)
            graphView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}