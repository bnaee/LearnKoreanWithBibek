package com.genesiswtech.lkwb.ui.ubtBuy.model

import com.google.gson.annotations.SerializedName

data class UBTBuyResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : UBTBuyDataResponse?    = UBTBuyDataResponse()
)
