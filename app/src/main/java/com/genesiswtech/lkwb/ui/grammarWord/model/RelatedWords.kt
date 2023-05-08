package com.genesiswtech.lkwb.ui.grammarWord.model

import com.google.gson.annotations.SerializedName

data class RelatedWords(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("word") var word: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("share_link") var shareLink: String? = null
)
