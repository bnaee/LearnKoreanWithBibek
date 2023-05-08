package com.genesiswtech.lkwb.ui.blog.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BlogDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("published_at") var publishedAt: String? = null
): Serializable
