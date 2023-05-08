package com.genesiswtech.lkwb.ui.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationForResponse(
    @SerializedName("type") var type: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("comment_on") var commentOn: NotificationCommentOnResponse? = NotificationCommentOnResponse()
)
