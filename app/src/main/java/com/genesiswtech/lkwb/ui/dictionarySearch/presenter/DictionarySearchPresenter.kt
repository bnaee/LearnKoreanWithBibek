package com.genesiswtech.lkwb.ui.dictionarySearch.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.view.IDictionarySearchView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class DictionarySearchPresenter(
    private var dictionarySearchView: IDictionarySearchView,
    var applicationComponent: Application
) :
    IDictionarySearchPresenter, BasePresenter<IDictionarySearchView>(dictionarySearchView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getDictionaries(context: Context, page: Int, limit: Int) {
        if (InternetConnection.checkForInternet(context)) {
            dictionarySearchView.onChooseProgressBar(page, true)
            mNetworkApi.getDictionaries(page.toString(), limit.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DictionarySearchResponse>? ->
                    dictionarySearchView.onChooseProgressBar(page, false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data.let { dictionarySearchView.onSuccess(it) }
                    } else {
                        dictionarySearchView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    dictionarySearchView.onChooseProgressBar(page, false)
                    dictionarySearchView.onFailure(error.localizedMessage)
                })

        } else {
            dictionarySearchView.onChooseProgressBar(page, false)
            dictionarySearchView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}