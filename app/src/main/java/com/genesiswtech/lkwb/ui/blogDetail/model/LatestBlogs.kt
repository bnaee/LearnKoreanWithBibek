package com.genesiswtech.lkwb.ui.blogDetail.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LatestBlogs (
    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("title"        ) var title       : String? = null,
    @SerializedName("category"     ) var category    : String? = null,
    @SerializedName("image"        ) var image       : String? = null,
    @SerializedName("published_at" ) var publishedAt : String? = null
        ): Serializable