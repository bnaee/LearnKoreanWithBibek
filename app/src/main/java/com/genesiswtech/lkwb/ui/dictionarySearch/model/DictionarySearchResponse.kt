package com.genesiswtech.lkwb.ui.dictionarySearch.model

import com.google.gson.annotations.SerializedName

data class DictionarySearchResponse(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("code"    ) var code    : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<DictionarySearchDataResponse> = arrayListOf()
)
