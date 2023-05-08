package com.genesiswtech.lkwb.firebase.data

import com.google.gson.annotations.SerializedName

data class StorePushTokenResponse(
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("device_id") var deviceId: String? = null
)