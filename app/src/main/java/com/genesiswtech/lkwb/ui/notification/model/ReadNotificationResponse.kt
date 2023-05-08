package com.genesiswtech.lkwb.ui.notification.model

import com.google.gson.annotations.SerializedName

data class ReadNotificationResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null
)
