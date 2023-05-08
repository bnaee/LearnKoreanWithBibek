package com.genesiswtech.lkwb.ui.blogDetail.model

import com.google.gson.annotations.SerializedName

data class BlogDetailResponse (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : BlogDetailDataResponse?    = BlogDetailDataResponse()
        )
