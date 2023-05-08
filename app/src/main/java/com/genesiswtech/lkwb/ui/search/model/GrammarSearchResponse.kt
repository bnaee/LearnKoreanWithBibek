package com.genesiswtech.lkwb.ui.search.model

import com.google.gson.annotations.SerializedName

data class GrammarSearchResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<GrammarSearchDataResponse> = arrayListOf()
)
