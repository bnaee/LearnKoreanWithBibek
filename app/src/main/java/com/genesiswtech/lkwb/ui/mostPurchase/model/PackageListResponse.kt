package com.genesiswtech.lkwb.ui.mostPurchase.model

import com.google.gson.annotations.SerializedName

data class PackageListResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : PackageListDataResponse?    = PackageListDataResponse()
)
