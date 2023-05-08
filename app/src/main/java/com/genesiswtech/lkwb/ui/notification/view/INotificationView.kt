package com.genesiswtech.lkwb.ui.notification.view

import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.notification.model.NotificationDataResponse
import com.genesiswtech.lkwb.ui.notification.model.ReadNotificationResponse

interface INotificationView {
    fun initDependencies()

    fun onSuccess(notificationDataResponse: ArrayList<NotificationDataResponse>)

    fun onReadSuccess(readNotificationResponse: ReadNotificationResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun onNoData(status:Boolean)

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}