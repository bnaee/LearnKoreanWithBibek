package com.genesiswtech.lkwb.ui.grammarWord.model

import com.google.gson.annotations.SerializedName

data class GrammarWordDataResponse (
    @SerializedName("id"          ) var id          : Int?      = null,
    @SerializedName("word"        ) var word        : String?   = null,
    @SerializedName("description" ) var description : String?   = null,
    @SerializedName("meanings"    ) var meanings    : Meanings? = Meanings(),
    @SerializedName("rules"       ) var rules       : Rules?    = Rules(),
    @SerializedName("examples"    ) var examples    : String?   = null,
    @SerializedName("isFavorite"  ) var isFavorite  : Boolean?  = null,
    @SerializedName("relatedWords" ) var relatedWords : ArrayList<RelatedWords> = arrayListOf(),
    @SerializedName("share_link" ) var shareLink  : String?  = null
        )
