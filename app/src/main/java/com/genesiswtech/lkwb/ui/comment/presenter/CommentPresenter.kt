package com.genesiswtech.lkwb.ui.comment.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.comment.view.ICommentView
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentEditResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentPostResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class CommentPresenter(
    private var commentView: ICommentView,
    var applicationComponent: Application
) :
    ICommentPresenter, BasePresenter<ICommentView>(commentView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getComments(context: Context, id: Int, page: Int, limit: Int) {
        if (InternetConnection.checkForInternet(context)) {
            commentView.onChooseProgressBar(page, true)
            mNetworkApi.getBlogComments(id, page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentResponse>? ->
                    commentView.onChooseProgressBar(page, false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data.size > 0) {
                            commentView.onSuccess(response.body()!!.data)
                            commentView.onNoData(false)
                        } else {
                            if (page == 1)
                                commentView.onNoData(true)
                        }
                    } else {
                        commentView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    commentView.onChooseProgressBar(page, false)
                    commentView.onFailure(error.localizedMessage)
                })

        } else {
            commentView.onChooseProgressBar(page, false)
            commentView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postEditComment(
        context: Context,
        id: Int,
        type: String,
        body: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            commentView.onShowProgressBar(true)
            mNetworkApi.postUpdateComment(id, type, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentEditResponse>? ->
                    commentView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let {
                            it.data?.let { it1 ->
                                commentView.onCommentEditSuccess(
                                    it1
                                )
                            }
                        }
                    } else {
                        commentView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    commentView.onShowProgressBar(false)
                    commentView.onFailure(error.localizedMessage)
                })

        } else {
            commentView.onShowProgressBar(false)
            commentView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postDeleteComment(context: Context, id: Int, type: String) {
        if (InternetConnection.checkForInternet(context)) {
            commentView.onShowProgressBar(true)
            mNetworkApi.postDeleteComment(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommentResponse>? ->
                    commentView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { commentView.onCommentDeleteSuccess(it) }
                    } else {
                        commentView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    commentView.onShowProgressBar(false)
                    commentView.onFailure(error.localizedMessage)
                })

        } else {
            commentView.onShowProgressBar(false)
            commentView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postComment(
        context: Context,
        id: Int,
        body: String
    ) {
        if (body.isEmpty()) {
            commentView.onCommentEmpty()
        } else
            if (InternetConnection.checkForInternet(context)) {
                commentView.onShowProgressBar(true)
                mNetworkApi.postBlogComment(id, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<CommentPostResponse>? ->
                        commentView.onShowProgressBar(false)
                        if (response!!.isSuccessful) {
                            response.body()?.let {
                                it.data?.let { it1 ->
                                    commentView.onCommentPostSuccess(
                                        it1
                                    )
                                }
                            }
                        } else {
                            commentView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                        }
                    }, { error ->
                        commentView.onShowProgressBar(false)
                        commentView.onFailure(error.localizedMessage)
                    })

            } else {
                commentView.onShowProgressBar(false)
                commentView.showSnackBar(context.getString(R.string.no_internet))
            }
    }
}