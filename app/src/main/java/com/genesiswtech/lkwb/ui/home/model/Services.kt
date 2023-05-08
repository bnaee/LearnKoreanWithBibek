package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

class Services {
    @SerializedName("id"      ) var id      : Int?    = null
    @SerializedName("title"   ) var title   : String? = null
    @SerializedName("excerpt" ) var excerpt : String? = null
    @SerializedName("icon"    ) var icon    : String? = null
}