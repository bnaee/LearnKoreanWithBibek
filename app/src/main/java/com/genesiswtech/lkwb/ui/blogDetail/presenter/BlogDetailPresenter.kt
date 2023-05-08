package com.genesiswtech.lkwb.ui.blogDetail.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.ui.blogDetail.model.BlogDetailResponse
import com.genesiswtech.lkwb.ui.blogDetail.view.IBlogDetailView
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentEditResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentPostResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class BlogDetailPresenter(
    private var blogDetailView: IBlogDetailView,
    var applicationComponent: Application
) :
    IBlogDetailPresenter, BasePresenter<IBlogDetailView>(blogDetailView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getSingleBlog(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            blogDetailView.onShowProgressBar(true)
            mNetworkApi.getSingleBlog(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<BlogDetailResponse>? ->
                    blogDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { blogDetailView.onSuccess(it) }
                    } else {
                        blogDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    blogDetailView.onShowProgressBar(false)
                    blogDetailView.onFailure(error.localizedMessage)
                })

        } else {
            blogDetailView.onShowProgressBar(false)
            blogDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postAddFavouriteBlog(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            blogDetailView.onShowProgressBar(true)
            mNetworkApi.postAddFavourite(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteResponse>? ->
                    blogDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { blogDetailView.onAddFavouriteSuccess(it) }
                    } else {
                        blogDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    blogDetailView.onShowProgressBar(false)
                    blogDetailView.onFailure(error.localizedMessage)
                })

        } else {
            blogDetailView.onShowProgressBar(false)
            blogDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postRemoveFavouriteBlog(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            blogDetailView.onShowProgressBar(true)
            mNetworkApi.postRemoveFavourite(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<FavouriteResponse>? ->
                    blogDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { blogDetailView.onRemoveFavouriteSuccess(it) }
                    } else {
                        blogDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    blogDetailView.onShowProgressBar(false)
                    blogDetailView.onFailure(error.localizedMessage)
                })

        } else {
            blogDetailView.onShowProgressBar(false)
            blogDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}