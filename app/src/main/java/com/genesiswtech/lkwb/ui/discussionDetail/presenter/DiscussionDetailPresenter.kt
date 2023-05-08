package com.genesiswtech.lkwb.ui.discussionDetail.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.DiscussionDetailResponse
import com.genesiswtech.lkwb.ui.discussionDetail.view.IDiscussionDetailView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

//class DiscussionDetailPresenter(
//    private var discussionDetailView: IDiscussionDetailView,
//    var applicationComponent: Application
//) :
//    IDiscussionDetailPresenter, BasePresenter<IDiscussionDetailView>(discussionDetailView) {
//
//    @Inject
//    lateinit var mNetworkApi: INetworkApi
//
//    init {
//        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
//    }
//
//    override fun getSingleDiscussion(context: Context, id: Int) {
//        if (InternetConnection.checkForInternet(context)) {
//            discussionDetailView.onShowProgressBar(true)
//            mNetworkApi.getSingleDiscussion(id).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response: Response<DiscussionResponse>? ->
//                    discussionDetailView.onShowProgressBar(false)
//                    if (response!!.isSuccessful) {
//                        response.body()!!.data.let { discussionDetailView.onSuccess(response.body()!!) }
//                    } else {
//                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
//                    }
//                }, { error ->
//                    discussionDetailView.onShowProgressBar(false)
//                    discussionDetailView.onFailure(error.localizedMessage)
//                })
//
//        } else {
//            discussionDetailView.onShowProgressBar(false)
//            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
//        }
//    }
//
//
//    override fun postEditDiscussion(
//        context: Context,
//        id: Int,
//        type: String,
//        title: String,
//        description: String
//    ) {
//        if (InternetConnection.checkForInternet(context)) {
//            discussionDetailView.onShowProgressBar(true)
//            mNetworkApi.postUpdateDiscussion(id, type, title, description)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response: Response<DiscussionDetailResponse>? ->
//                    discussionDetailView.onShowProgressBar(false)
//                    if (response!!.isSuccessful) {
//                        response.body()?.let { discussionDetailView.onDiscussionEditSuccess(it) }
//                    } else {
//                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
//                    }
//                }, { error ->
//                    discussionDetailView.onShowProgressBar(false)
//                    discussionDetailView.onFailure(error.localizedMessage)
//                })
//
//        } else {
//            discussionDetailView.onShowProgressBar(false)
//            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
//        }
//    }
//
//    override fun postDeleteDiscussion(context: Context, id: Int, type: String) {
//        if (InternetConnection.checkForInternet(context)) {
//            discussionDetailView.onShowProgressBar(true)
//            mNetworkApi.postDeleteDiscussion(id, type).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response: Response<DiscussionDetailResponse>? ->
//                    discussionDetailView.onShowProgressBar(false)
//                    if (response!!.isSuccessful) {
//                        response.body()?.let { discussionDetailView.onDiscussionDeleteSuccess(it) }
//                    } else {
//                        discussionDetailView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
//                    }
//                }, { error ->
//                    discussionDetailView.onShowProgressBar(false)
//                    discussionDetailView.onFailure(error.localizedMessage)
//                })
//
//        } else {
//            discussionDetailView.onShowProgressBar(false)
//            discussionDetailView.showSnackBar(context.getString(R.string.no_internet))
//        }
//    }
//
//}