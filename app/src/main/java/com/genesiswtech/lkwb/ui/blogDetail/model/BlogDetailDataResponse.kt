package com.genesiswtech.lkwb.ui.blogDetail.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BlogDetailDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("published_at") var publishedAt: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("latest_blogs") var latestBlogs: ArrayList<LatestBlogs> = arrayListOf(),
    @SerializedName("isFavorite") var isFavorite: Boolean? = null,
    @SerializedName("total_comments") var total_comments: Int? = null,
    @SerializedName("share_link") var shareLink: String? = null
) : Serializable