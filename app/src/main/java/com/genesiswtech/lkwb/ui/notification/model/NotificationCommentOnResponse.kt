package com.genesiswtech.lkwb.ui.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationCommentOnResponse(
    @SerializedName("model") var model: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("title") var title: String? = null
)
