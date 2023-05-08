package com.genesiswtech.lkwb.ui.dictionarySearch.model

import com.google.gson.annotations.SerializedName

class Meaning {

    @SerializedName("np_meaning" ) var npMeaning : String? = null
    @SerializedName("en_meaning" ) var enMeaning : String? = null
    @SerializedName("kr_meaning" ) var krMeaning : String? = null
}