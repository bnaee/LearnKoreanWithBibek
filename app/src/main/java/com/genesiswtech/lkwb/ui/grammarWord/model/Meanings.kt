package com.genesiswtech.lkwb.ui.grammarWord.model

import com.google.gson.annotations.SerializedName

data class Meanings (
    @SerializedName("np" ) var np : String? = null,
    @SerializedName("en" ) var en : String? = null,
    @SerializedName("kr" ) var kr : String? = null
        )
