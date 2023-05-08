package com.genesiswtech.lkwb.ui.favouriteDetail.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.favouriteDetail.model.FavouriteDetailBlogResponse
import com.genesiswtech.lkwb.ui.favouriteDetail.model.FavouriteDetailDictionaryResponse
import com.genesiswtech.lkwb.ui.favouriteDetail.model.FavouriteDetailGrammarResponse
import com.genesiswtech.lkwb.ui.favouriteDetail.view.IFavouriteDetailView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class FavouriteDetailPresenter(
    private var favouriteDetailView: IFavouriteDetailView,
    var applicationComponent: Application
) :
    IFavouriteDetailPresenter, BasePresenter<IFavouriteDetailView>(favouriteDetailView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getFavouriteBlog(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            favouriteDetailView.onShowProgressBar(true)
            mNetworkApi.getFavouriteBlog().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteDetailBlogResponse>? ->
                    favouriteDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data.let {
                            favouriteDetailView.onFavouriteBlogSuccess(it)
                        }
                    } else {
                        favouriteDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    favouriteDetailView.onShowProgressBar(false)
                    favouriteDetailView.onFailure(error.localizedMessage)
                })

        } else {
            favouriteDetailView.onShowProgressBar(false)
            favouriteDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getFavouriteGrammar(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            favouriteDetailView.onShowProgressBar(true)
            mNetworkApi.getFavouriteGrammar().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteDetailGrammarResponse>? ->
                    favouriteDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data.let {
                            favouriteDetailView.onFavouriteGrammarSuccess(it)
                        }
                    } else {
                        favouriteDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    favouriteDetailView.onShowProgressBar(false)
                    favouriteDetailView.onFailure(error.localizedMessage)
                })

        } else {
            favouriteDetailView.onShowProgressBar(false)
            favouriteDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getFavouriteDictionary(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            favouriteDetailView.onShowProgressBar(true)
            mNetworkApi.getFavouriteDictionary().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteDetailDictionaryResponse>? ->
                    favouriteDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data.let {
                            favouriteDetailView.onFavouriteDictionarySuccess(it)
                        }
                    } else {
                        favouriteDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    favouriteDetailView.onShowProgressBar(false)
                    favouriteDetailView.onFailure(error.localizedMessage)
                })

        } else {
            favouriteDetailView.onShowProgressBar(false)
            favouriteDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }
}