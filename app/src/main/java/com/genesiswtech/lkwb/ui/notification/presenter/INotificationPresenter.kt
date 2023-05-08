package com.genesiswtech.lkwb.ui.notification.presenter

import android.content.Context

interface INotificationPresenter {

    fun getNotifications(context: Context, page: Int, limit: Int)

    fun readNotification(context: Context, id: Int)
}