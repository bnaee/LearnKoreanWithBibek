package com.genesiswtech.lkwb.firebase.data

import com.google.gson.annotations.SerializedName

data class RemoteMessage(
    @SerializedName("to") var to: String? = null,
    @SerializedName("content_available") var contentAvailable: Boolean? = null,
    @SerializedName("notification") var notification: Notification? = Notification()
)