package com.genesiswtech.lkwb.ui.dictionaryWord.model

import com.google.gson.annotations.SerializedName

data class DictionaryWordResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : DictionaryWordDataResponse?    = DictionaryWordDataResponse()
)
