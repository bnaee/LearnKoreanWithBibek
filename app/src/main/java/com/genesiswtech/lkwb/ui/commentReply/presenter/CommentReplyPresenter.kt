package com.genesiswtech.lkwb.ui.commentReply.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyResponse
import com.genesiswtech.lkwb.ui.commentReply.model.ReplyEditResponse
import com.genesiswtech.lkwb.ui.commentReply.view.ICommentReplyView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class CommentReplyPresenter(
    private var replyView: ICommentReplyView,
    var applicationComponent: Application
) :
    ICommentReplyPresenter, BasePresenter<ICommentReplyView>(replyView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getReplies(context: Context, id: Int, page: Int, limit: Int) {
        if (InternetConnection.checkForInternet(context)) {
            replyView.onChooseProgressBar(page, true)
            mNetworkApi.getCommentReplies(id, page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentReplyResponse>? ->
                    replyView.onChooseProgressBar(page, false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data != null) {
                            replyView.onReplySuccess(response.body()!!.data)
                            replyView.onNoData(false)
                        } else {
                            if (page == 1)
                                replyView.onNoData(true)
                        }
                    } else {
                        replyView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    replyView.onChooseProgressBar(page, false)
                    replyView.onFailure(error.localizedMessage)
                })

        } else {
            replyView.onChooseProgressBar(page, false)
            replyView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postEditReply(
        context: Context,
        id: Int,
        type: String,
        body: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            replyView.onShowProgressBar(true)
            mNetworkApi.postUpdateReply(id, type, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ReplyEditResponse>? ->
                    replyView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let {
                            it.data?.let { it1 ->
                                replyView.onReplyEditSuccess(
                                    it1
                                )
                            }
                        }
                    } else {
                        replyView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    replyView.onShowProgressBar(false)
                    replyView.onFailure(error.localizedMessage)
                })

        } else {
            replyView.onShowProgressBar(false)
            replyView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postDeleteReply(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            replyView.onShowProgressBar(true)
            mNetworkApi.postDeleteReply(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentReplyResponse>? ->
                    replyView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { replyView.onReplyDeleteSuccess(it) }
                    } else {
                        replyView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    replyView.onShowProgressBar(false)
                    replyView.onFailure(error.localizedMessage)
                })

        } else {
            replyView.onShowProgressBar(false)
            replyView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postReply(
        context: Context,
        id: Int,
        body: String
    ) {
        if (body.isEmpty()) {
            replyView.onReplyEmpty()
        } else
            if (InternetConnection.checkForInternet(context)) {
                replyView.onShowProgressBar(true)
                mNetworkApi.postReply(id, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<ReplyEditResponse>? ->
                        replyView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            response.body()?.let {
                                it.data?.let { it1 ->
                                    replyView.onReplyPostSuccess(
                                        it1
                                    )
                                }
                            }
                        } else {
                            replyView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }
                    }, { error ->
                        replyView.onShowProgressBar(false)
                        replyView.onFailure(error.localizedMessage)
                    })

            } else {
                replyView.onShowProgressBar(false)
                replyView.showSnackBar(context.getString(R.string.no_internet))
            }
    }
}