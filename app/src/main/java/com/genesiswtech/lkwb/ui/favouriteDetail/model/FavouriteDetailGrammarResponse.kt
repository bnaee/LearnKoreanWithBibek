package com.genesiswtech.lkwb.ui.favouriteDetail.model

import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import com.google.gson.annotations.SerializedName

data class FavouriteDetailGrammarResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Grammar> = arrayListOf(),
)