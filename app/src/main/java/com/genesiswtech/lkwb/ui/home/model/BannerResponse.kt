package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : BannerDataResponse?    = BannerDataResponse()
)
