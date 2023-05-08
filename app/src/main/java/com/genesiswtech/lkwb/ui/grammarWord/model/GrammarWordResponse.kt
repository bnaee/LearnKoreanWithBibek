package com.genesiswtech.lkwb.ui.grammarWord.model

import com.google.gson.annotations.SerializedName

data class GrammarWordResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : GrammarWordDataResponse?    = GrammarWordDataResponse()
)
