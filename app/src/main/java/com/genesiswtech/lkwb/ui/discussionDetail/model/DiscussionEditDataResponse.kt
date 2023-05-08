package com.genesiswtech.lkwb.ui.discussionDetail.model

import com.google.gson.annotations.SerializedName

data class DiscussionEditDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("slug") var slug: String? = null

)