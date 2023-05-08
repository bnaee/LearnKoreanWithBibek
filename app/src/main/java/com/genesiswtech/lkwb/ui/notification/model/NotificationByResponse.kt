package com.genesiswtech.lkwb.ui.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationByResponse (
    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("avatar" ) var avatar : String? = null
        )