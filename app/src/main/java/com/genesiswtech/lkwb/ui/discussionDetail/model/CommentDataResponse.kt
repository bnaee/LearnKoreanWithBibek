package com.genesiswtech.lkwb.ui.discussionDetail.model

import com.google.gson.annotations.SerializedName

data class CommentDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("comment_id") var commentId: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("date_time") var dateTime: String? = null,
    @SerializedName("display_date") var displayDate: String? = null,
    @SerializedName("replies") var replies: Int? = null
)