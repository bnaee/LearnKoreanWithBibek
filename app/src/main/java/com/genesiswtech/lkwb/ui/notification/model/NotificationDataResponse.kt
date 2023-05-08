package com.genesiswtech.lkwb.ui.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("for") var notificationForResponse: NotificationForResponse? = NotificationForResponse(),
    @SerializedName("notification_type") var notificationType: String? = null,
    @SerializedName("by") var notificationByResponse: NotificationByResponse? = NotificationByResponse(),
    @SerializedName("read") var read: Boolean? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("date_time") var dateTime: String? = null,
    @SerializedName("text") var text: String? = null
)