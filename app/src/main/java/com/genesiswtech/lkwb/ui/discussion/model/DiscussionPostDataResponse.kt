package com.genesiswtech.lkwb.ui.discussion.model

import com.google.gson.annotations.SerializedName

data class DiscussionPostDataResponse (
    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("title"       ) var title       : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("user"        ) var user        : String? = null,
    @SerializedName("user_image"  ) var userImage   : String? = null,
    @SerializedName("created_at"  ) var createdAt   : String? = null
        )