package com.genesiswtech.lkwb.ui.newPassword.model

import com.google.gson.annotations.SerializedName

data class NewPasswordResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,

    )