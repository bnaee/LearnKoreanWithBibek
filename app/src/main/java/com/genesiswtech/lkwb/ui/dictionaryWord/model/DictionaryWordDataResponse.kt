package com.genesiswtech.lkwb.ui.dictionaryWord.model

import com.google.gson.annotations.SerializedName

class DictionaryWordDataResponse (
    @SerializedName("id"         ) var id         : Int?     = null,
    @SerializedName("word"       ) var word       : String?  = null,
    @SerializedName("slug"       ) var slug       : String?  = null,
    @SerializedName("meaning"    ) var meaning    : Meaning? = Meaning(),
    @SerializedName("examples"   ) var examples   : String?  = null,
    @SerializedName("isFavorite" ) var isFavorite : Boolean? = null,
    @SerializedName("share_link" ) var shareLink  : String?  = null
        )