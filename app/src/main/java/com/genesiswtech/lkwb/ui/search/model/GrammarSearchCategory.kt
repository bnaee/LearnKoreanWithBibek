package com.genesiswtech.lkwb.ui.search.model

import com.google.gson.annotations.SerializedName

data class GrammarSearchCategory (
    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("title" ) var title : String? = null
        )