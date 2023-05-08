package com.genesiswtech.lkwb.ui.dictionary.model

import com.google.gson.annotations.SerializedName

data class Meaning(
    @SerializedName("np_meaning" ) var npMeaning : String? = null,
    @SerializedName("en_meaning" ) var enMeaning : String? = null,
    @SerializedName("kr_meaning" ) var krMeaning : String? = null
)
