package com.genesiswtech.lkwb.ui.beginTest.model

import com.google.gson.annotations.SerializedName

data class BeginTestResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: BeginTestDataResponse? = BeginTestDataResponse()
)