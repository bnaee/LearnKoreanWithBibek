package com.genesiswtech.lkwb.ui.discussionDetail.model

import com.google.gson.annotations.SerializedName

data class CommentEditDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("commentable_id") var commentableId: Int? = null,
    @SerializedName("commentable_type") var commentableType: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)