package com.genesiswtech.lkwb.ui.dictionary.presenter

import android.app.Application
import android.content.Context
import android.util.Log
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.dictionary.view.IDictionaryView
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class DictionaryPresenter(private var dictionaryView: IDictionaryView, var applicationComponent: Application) :
    IDictionaryPresenter, BasePresenter<IDictionaryView>(dictionaryView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getWordDay(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            dictionaryView.onShowProgressBar(true)
            mNetworkApi.getGrammarDay().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GrammarDayResponse>? ->
                    dictionaryView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { dictionaryView.onSuccess(it) }

                    } else {
                        dictionaryView.onShowProgressBar(false)
                        dictionaryView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    dictionaryView.onShowProgressBar(false)
                    dictionaryView.onFailure(error.localizedMessage)
                })

        } else {
            dictionaryView.onShowProgressBar(false)
            dictionaryView.showSnackBar(context.getString(R.string.no_internet))
        }
    }
}