package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

data class ResultHistoryDataResponse(
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("score" ) var score : String? = null
)
