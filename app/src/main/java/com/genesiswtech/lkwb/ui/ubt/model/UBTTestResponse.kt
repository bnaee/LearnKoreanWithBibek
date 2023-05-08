package com.genesiswtech.lkwb.ui.ubt.model

import com.google.gson.annotations.SerializedName

data class UBTTestResponse(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("code"    ) var code    : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<UBTTestDataResponse> = arrayListOf()
)
