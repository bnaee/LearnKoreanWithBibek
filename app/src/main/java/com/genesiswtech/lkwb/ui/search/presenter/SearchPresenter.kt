package com.genesiswtech.lkwb.ui.search.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.blog.model.BlogResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionResponse
import com.genesiswtech.lkwb.ui.search.model.GrammarSearchResponse
import com.genesiswtech.lkwb.ui.search.view.ISearchView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class SearchPresenter(
    private var searchView: ISearchView,
    var applicationComponent: Application
) :
    ISearchPresenter, BasePresenter<ISearchView>(searchView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }


    override fun postGrammarSearch(
        context: Context,
        page: Int,
        limit: Int,
        type: String,
        text: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            searchView.onChooseProgressBar(page,true)
            mNetworkApi.postGrammarSearch(page, limit, type, text).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GrammarSearchResponse>? ->
                    searchView.onChooseProgressBar(page,false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data != null) {
                            searchView.onGrammarSearchSuccess(response.body()!!.data)
                            searchView.onNoData(false)
                        } else
                            searchView.onNoData(true)
                    } else {
                        searchView.onChooseProgressBar(page,false)
                        searchView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    searchView.onChooseProgressBar(page,false)
                    searchView.onFailure(error.localizedMessage)
                })

        } else {
            searchView.onChooseProgressBar(page,false)
            searchView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postDictionarySearch(
        context: Context,
        page: Int,
        limit: Int,
        type: String,
        text: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            searchView.onChooseProgressBar(page,true)
            mNetworkApi.postDictionarySearch(page, limit, type, text).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DictionarySearchResponse>? ->
                    searchView.onChooseProgressBar(page,false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data != null) {
                            searchView.onDictionarySearchSuccess(response.body()!!.data)
                            searchView.onNoData(false)
                        } else
                            searchView.onNoData(true)

                    } else {
                        searchView.onChooseProgressBar(page,false)
                        searchView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    searchView.onChooseProgressBar(page,false)
                    searchView.onFailure(error.localizedMessage)
                })

        } else {
            searchView.onChooseProgressBar(page,false)
            searchView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postBlogSearch(
        context: Context,
        page: Int,
        limit: Int,
        type: String,
        text: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            searchView.onChooseProgressBar(page,true)
            mNetworkApi.postBlogSearch(page, limit, type, text).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BlogResponse>? ->
                    searchView.onChooseProgressBar(page,false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data.let { searchView.onBlogSearchSuccess(it) }

                    } else {
                        searchView.onChooseProgressBar(page,false)
                        searchView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    searchView.onChooseProgressBar(page,false)
                    searchView.onFailure(error.localizedMessage)
                })

        } else {
            searchView.onChooseProgressBar(page,false)
            searchView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postDiscussionSearch(
        context: Context,
        page: Int,
        limit: Int,
        type: String,
        text: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            searchView.onChooseProgressBar(page,true)
            mNetworkApi.postDiscussionSearch(page, limit, type, text).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DiscussionResponse>? ->
                    searchView.onChooseProgressBar(page,false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data != null) {
                            searchView.onDiscussionSearchSuccess(response.body()!!.data)
                            searchView.onNoData(false)
                        } else
                            searchView.onNoData(true)
                    } else {
                        searchView.onChooseProgressBar(page,false)
                        searchView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    searchView.onChooseProgressBar(page,false)
                    searchView.onFailure(error.localizedMessage)
                })

        } else {
            searchView.onChooseProgressBar(page,false)
            searchView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}