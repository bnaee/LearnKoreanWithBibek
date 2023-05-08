package com.genesiswtech.lkwb.ui.ubtQuestion.model

import com.google.gson.annotations.SerializedName

data class UBTQuestionResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<UBTQuestionDataResponse> = arrayListOf()
)
