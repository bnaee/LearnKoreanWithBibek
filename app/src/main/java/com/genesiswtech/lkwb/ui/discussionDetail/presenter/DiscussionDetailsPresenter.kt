package com.genesiswtech.lkwb.ui.discussionDetail.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.blogDetail.model.BlogDetailResponse
import com.genesiswtech.lkwb.ui.blogDetail.presenter.IBlogDetailPresenter
import com.genesiswtech.lkwb.ui.blogDetail.view.IBlogDetailView
import com.genesiswtech.lkwb.ui.discussionDetail.model.*
import com.genesiswtech.lkwb.ui.discussionDetail.view.IDiscussionDetailView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class DiscussionDetailsPresenter(
    private var discussionDetailView: IDiscussionDetailView,
    var applicationComponent: Application
) :
    IDiscussionDetailPresenter, BasePresenter<IDiscussionDetailView>(discussionDetailView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getDiscussionSingle(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            discussionDetailView.onShowProgressBar(true)
            mNetworkApi.getDiscussionSingle(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DiscussionDetailResponse>? ->
                    discussionDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { discussionDetailView.onSuccess(it) }
                    } else {
                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionDetailView.onShowProgressBar(false)
                    discussionDetailView.onFailure(error.localizedMessage)
                })

        } else {
            discussionDetailView.onShowProgressBar(false)
            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postEditDiscussion(
        context: Context,
        id: Int,
        type: String,
        title: String,
        description: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            discussionDetailView.onShowProgressBar(true)
            mNetworkApi.postUpdateDiscussion(id, type, title, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DiscussionEditResponse>? ->
                    discussionDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { discussionDetailView.onDiscussionEditSuccess(it) }
                    } else {
                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionDetailView.onShowProgressBar(false)
                    discussionDetailView.onFailure(error.localizedMessage)
                })

        } else {
            discussionDetailView.onShowProgressBar(false)
            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postDeleteDiscussion(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            discussionDetailView.onShowProgressBar(true)
            mNetworkApi.postDeleteDiscussion(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DiscussionDetailResponse>? ->
                    discussionDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { discussionDetailView.onDiscussionDeleteSuccess(it) }
                    } else {
                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionDetailView.onShowProgressBar(false)
                    discussionDetailView.onFailure(error.localizedMessage)
                })

        } else {
            discussionDetailView.onShowProgressBar(false)
            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun getComments(context: Context, id: Int, page: Int, limit: Int) {
        if (InternetConnection.checkForInternet(context)) {
            discussionDetailView.onChooseProgressBar(page, true)
            mNetworkApi.getComments(id, page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentResponse>? ->
                    discussionDetailView.onChooseProgressBar(page, false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data != null) {
                            discussionDetailView.onCommentSuccess(response.body()!!.data)
                            discussionDetailView.onNoData(false)
                        } else {
                            if (page == 1)
                                discussionDetailView.onNoData(true)
                        }
                    } else {
                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionDetailView.onChooseProgressBar(page, false)
                    discussionDetailView.onFailure(error.localizedMessage)
                })

        } else {
            discussionDetailView.onChooseProgressBar(page, false)
            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postEditComment(
        context: Context,
        id: Int,
        type: String,
        body: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            discussionDetailView.onShowProgressBar(true)
            mNetworkApi.postUpdateComment(id, type, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentEditResponse>? ->
                    discussionDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let {
                            it.data?.let { it1 ->
                                discussionDetailView.onCommentEditSuccess(
                                    it1, id
                                )
                            }
                        }
                    } else {
                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionDetailView.onShowProgressBar(false)
                    discussionDetailView.onFailure(error.localizedMessage)
                })

        } else {
            discussionDetailView.onShowProgressBar(false)
            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postDeleteComment(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            discussionDetailView.onShowProgressBar(true)
            mNetworkApi.postDeleteComment(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentResponse>? ->
                    discussionDetailView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { discussionDetailView.onCommentDeleteSuccess(it, id) }
                    } else {
                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionDetailView.onShowProgressBar(false)
                    discussionDetailView.onFailure(error.localizedMessage)
                })

        } else {
            discussionDetailView.onShowProgressBar(false)
            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postComment(
        context: Context,
        id: Int,
        body: String
    ) {
        if (body.isEmpty()) {
            discussionDetailView.onCommentEmpty()
        } else
            if (InternetConnection.checkForInternet(context)) {
                discussionDetailView.onShowProgressBar(true)
                mNetworkApi.postComment(id, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<CommentPostResponse>? ->
                        discussionDetailView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            response.body()?.let {
                                it.data?.let { it1 ->
                                    discussionDetailView.onCommentPostSuccess(
                                        it1
                                    )
                                }
                            }
                        } else {
                            discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }
                    }, { error ->
                        discussionDetailView.onShowProgressBar(false)
                        discussionDetailView.onFailure(error.localizedMessage)
                    })

            } else {
                discussionDetailView.onShowProgressBar(false)
                discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
            }
    }


}