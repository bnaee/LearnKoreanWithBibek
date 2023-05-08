package com.genesiswtech.lkwb.ui.dictionary.model

import com.genesiswtech.lkwb.ui.dictionaryWord.model.Meaning
import com.google.gson.annotations.SerializedName

data class DictionaryDayResponse(
    @SerializedName("id"       ) var id       : Int?     = null,
    @SerializedName("word"     ) var word     : String?  = null,
    @SerializedName("slug"     ) var slug     : String?  = null,
    @SerializedName("meaning"  ) var meaning  : Meaning? = Meaning(),
    @SerializedName("examples" ) var examples : String?  = null
)
