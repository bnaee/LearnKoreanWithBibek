package com.genesiswtech.lkwb.ui.discussion.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionPostResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionResponse
import com.genesiswtech.lkwb.ui.discussion.view.IDiscussionView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class DiscussionPresenter(
    private var discussionView: IDiscussionView,
    var applicationComponent: Application
) :
    IDiscussionPresenter, BasePresenter<IDiscussionView>(discussionView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getDiscussions(context: Context, page: String, limit: String, sort_order: String) {
        if (InternetConnection.checkForInternet(context)) {
            discussionView.onChooseProgressBar(page.toInt(), true)
            mNetworkApi.getDiscussions(page,limit,sort_order).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DiscussionResponse>? ->
                    discussionView.onChooseProgressBar(page.toInt(), false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { discussionView.onSuccess(it.data) }
                    } else {
                        discussionView.onChooseProgressBar(page.toInt(), false)
                        discussionView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionView.onChooseProgressBar(page.toInt(), false)
                    discussionView.onFailure(error.localizedMessage)
                })

        } else {
            discussionView.onChooseProgressBar(page.toInt(), false)
            discussionView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postNewDiscussion(context: Context, title: String, description: String) {
        if (TextUtils.isEmpty(title)) {
            discussionView.onTitleError()
        } else if (InternetConnection.checkForInternet(context)) {
            discussionView.onShowProgressBar(true)
            mNetworkApi.postNewDiscussion(title, description).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<DiscussionPostResponse>? ->
                    discussionView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { discussionView.onNewDiscussionSuccess(it) }
                    } else {
                        discussionView.onShowProgressBar(false)
                        discussionView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    discussionView.onShowProgressBar(false)
                    discussionView.onFailure(error.localizedMessage)
                })

        } else {
            discussionView.onShowProgressBar(false)
            discussionView.showSnackBar(context.getString(R.string.no_internet))
        }
    }


}