package com.genesiswtech.lkwb.ui.grammarWord.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordResponse
import com.genesiswtech.lkwb.ui.grammarWord.view.IGrammarWordView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class GrammarWordPresenter(
    private var grammarWordView: IGrammarWordView,
    var applicationComponent: Application
) :
    IGrammarWordPresenter, BasePresenter<IGrammarWordView>(grammarWordView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getGrammarWord(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            grammarWordView.onShowProgressBar(true)
            mNetworkApi.getGrammarSingle(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GrammarWordResponse>? ->
                    grammarWordView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { grammarWordView.onSuccess(it) }
                    } else {
                        grammarWordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    grammarWordView.onShowProgressBar(false)
                    grammarWordView.onFailure(error.localizedMessage)
                })

        } else {
            grammarWordView.onShowProgressBar(false)
            grammarWordView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun postAddFavouriteGrammar(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            grammarWordView.onShowProgressBar(true)
            mNetworkApi.postAddFavourite(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteResponse>? ->
                    grammarWordView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { grammarWordView.onAddFavouriteSuccess(it) }
                    } else {
                        grammarWordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    grammarWordView.onShowProgressBar(false)
                    grammarWordView.onFailure(error.localizedMessage)
                })

        } else {
            grammarWordView.onShowProgressBar(false)
            grammarWordView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postRemoveFavouriteGrammar(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            grammarWordView.onShowProgressBar(true)
            mNetworkApi.postRemoveFavourite(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteResponse>? ->
                    grammarWordView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { grammarWordView.onRemoveFavouriteSuccess(it) }
                    } else {
                        grammarWordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    grammarWordView.onShowProgressBar(false)
                    grammarWordView.onFailure(error.localizedMessage)
                })

        } else {
            grammarWordView.onShowProgressBar(false)
            grammarWordView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}