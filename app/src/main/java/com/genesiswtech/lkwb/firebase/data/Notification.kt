package com.genesiswtech.lkwb.firebase.data

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("sound") var sound: String? = null,
    @SerializedName("type") var type: String? = null
)