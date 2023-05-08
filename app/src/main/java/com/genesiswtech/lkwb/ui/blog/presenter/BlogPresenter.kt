package com.genesiswtech.lkwb.ui.blog.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.blog.model.BlogResponse
import com.genesiswtech.lkwb.ui.blog.view.IBlogView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class BlogPresenter(private var blogView: IBlogView, var applicationComponent: Application) :
    IBlogPresenter, BasePresenter<IBlogView>(blogView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getBlogs(context: Context) {
        if (InternetConnection.checkForInternet(context)) {
            blogView.onShowProgressBar(true)
            mNetworkApi.getBlogs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BlogResponse>? ->
                    blogView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { blogView.onSuccess(it) }
                    } else {
                        blogView.onShowProgressBar(false)
                        blogView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    blogView.onShowProgressBar(false)
                    blogView.onFailure(error.localizedMessage)
                })

        } else {
            blogView.onShowProgressBar(false)
            blogView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}