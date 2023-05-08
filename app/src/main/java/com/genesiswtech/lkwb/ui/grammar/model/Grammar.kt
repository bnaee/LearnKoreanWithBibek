package com.genesiswtech.lkwb.ui.grammar.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Grammar (
    @SerializedName("id"          ) var id          : Int?     = null,
    @SerializedName("word"        ) var word        : String?  = null,
    @SerializedName("slug"        ) var slug        : String?  = null,
    @SerializedName("description" ) var description : String?  = null,
    @SerializedName("excerpt"     ) var excerpt     : String?  = null,
    @SerializedName("share_link"  ) var shareLink   : String?  = null,
    @SerializedName("isFavorite"  ) var isFavorite  : Boolean? = null

): Serializable
