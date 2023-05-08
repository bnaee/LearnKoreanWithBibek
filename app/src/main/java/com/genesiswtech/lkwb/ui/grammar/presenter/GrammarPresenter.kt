package com.genesiswtech.lkwb.ui.grammar.presenter

import android.app.Application
import android.content.Context
import android.util.Log
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.grammar.model.GrammarCategoryResponse
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayResponse
import com.genesiswtech.lkwb.ui.grammar.view.IGrammarView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class GrammarPresenter(private var grammarView: IGrammarView, var applicationComponent: Application) :
    IGrammarPresenter, BasePresenter<IGrammarView>(grammarView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getGrammarDay(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            grammarView.onShowProgressBar(true)
            mNetworkApi.getGrammarDay().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GrammarDayResponse>? ->
                    grammarView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { grammarView.onSuccess(it) }

                    } else {
                        Log.d("TAG","Error: "+response.errorBody())
                        grammarView.onShowProgressBar(false)
                        grammarView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    Log.d("TAG","Error: "+error)
                    grammarView.onShowProgressBar(false)
                    grammarView.onFailure(error.localizedMessage)
                })

        } else {
            grammarView.onShowProgressBar(false)
            grammarView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getGrammarCategory(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            grammarView.onShowProgressBar(true)
            mNetworkApi.getGrammarCategory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GrammarCategoryResponse>? ->
                    grammarView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data.let { grammarView.onGrammarCategorySuccess(it) }

                    } else {
                        grammarView.onShowProgressBar(false)
                        grammarView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    grammarView.onShowProgressBar(false)
                    grammarView.onFailure(error.localizedMessage)
                })

        } else {
            grammarView.onShowProgressBar(false)
            grammarView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}