package com.genesiswtech.lkwb.ui.discussion.view

import android.view.View
import android.widget.AdapterView
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionDataResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionPostDataResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionPostResponse

interface IDiscussionView {

    fun initDependencies()

    fun onDiscussionSearchClick(v: View?)

    fun onAddDiscussionClick(v: View?)

    fun onSuccess(discussionDataResponse: ArrayList<DiscussionDataResponse>)

    fun onNewDiscussionSuccess(discussionPostResponse: DiscussionPostResponse)

    fun onTitleError()

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}