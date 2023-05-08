package com.genesiswtech.lkwb.ui.search.model

import com.google.gson.annotations.SerializedName

data class GrammarSearchDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("word") var word: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("category") var category: GrammarSearchCategory? = GrammarSearchCategory(),
    @SerializedName("isFavorite") var isFavorite: Boolean? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("share_link") var shareLink: String? = null
)