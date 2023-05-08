package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
//    @SerializedName("data"    ) var data    : String?  = null
)
