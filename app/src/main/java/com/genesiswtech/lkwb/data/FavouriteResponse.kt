package com.genesiswtech.lkwb.data

import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.google.gson.annotations.SerializedName

data class FavouriteResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data" ) var data : BlogDataResponse?  = null,
)
