package com.genesiswtech.lkwb.ui.dictionaryWord.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.view.IDictionaryWordView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class DictionaryWordPresenter(
    private var dictionaryWordView: IDictionaryWordView,
    var applicationComponent: Application
) :
    IDictionaryWordPresenter, BasePresenter<IDictionaryWordView>(dictionaryWordView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getSingleDictionary(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            dictionaryWordView.onShowProgressBar(true)
            mNetworkApi.getSingleDictionary(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DictionaryWordResponse>? ->
                    dictionaryWordView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { dictionaryWordView.onSuccess(it) }
                    } else {
                        dictionaryWordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    dictionaryWordView.onShowProgressBar(false)
                    dictionaryWordView.onFailure(error.localizedMessage)
                })

        } else {
            dictionaryWordView.onShowProgressBar(false)
            dictionaryWordView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


    override fun postAddFavouriteDictionary(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            dictionaryWordView.onShowProgressBar(true)
            mNetworkApi.postAddFavourite(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteResponse>? ->
                    dictionaryWordView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { dictionaryWordView.onAddFavouriteSuccess(it) }
                    } else {
                        dictionaryWordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    dictionaryWordView.onShowProgressBar(false)
                    dictionaryWordView.onFailure(error.localizedMessage)
                })

        } else {
            dictionaryWordView.onShowProgressBar(false)
            dictionaryWordView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postRemoveFavouriteDictionary(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            dictionaryWordView.onShowProgressBar(true)
            mNetworkApi.postRemoveFavourite(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteResponse>? ->
                    dictionaryWordView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { dictionaryWordView.onRemoveFavouriteSuccess(it) }
                    } else {
                        dictionaryWordView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    dictionaryWordView.onShowProgressBar(false)
                    dictionaryWordView.onFailure(error.localizedMessage)
                })

        } else {
            dictionaryWordView.onShowProgressBar(false)
            dictionaryWordView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}