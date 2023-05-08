package com.genesiswtech.lkwb.ui.dictionarySearch.model

import com.google.gson.annotations.SerializedName

data class DictionarySearchDataResponse(
    @SerializedName("id"         ) var id         : Int?     = null,
    @SerializedName("word"       ) var word       : String?  = null,
    @SerializedName("meaning"    ) var meaning    : Meaning? = Meaning(),
    @SerializedName("examples"   ) var examples   : String?  = null,
    @SerializedName("isFavorite" ) var isFavorite : Boolean? = null,
    @SerializedName("share_link" ) var shareLink  : String?  = null
)
