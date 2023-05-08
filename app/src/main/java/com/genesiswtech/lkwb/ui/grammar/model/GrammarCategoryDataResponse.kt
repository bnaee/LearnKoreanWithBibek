package com.genesiswtech.lkwb.ui.grammar.model

import com.google.gson.annotations.SerializedName

data class GrammarCategoryDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("grammars") var grammars: ArrayList<Grammar> = arrayListOf()
)
