package com.genesiswtech.lkwb.ui.discussionDetail.model

import com.google.gson.annotations.SerializedName

data class DiscussionDetailDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("user") var user: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("total_comments") var totalComments: Int? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("display_date") var displayDate: String? = null,
    @SerializedName("date_time") var dateTime: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("share_link") var shareLink: String? = null
//    @SerializedName("comments"       ) var comments      : ArrayList<CommentDataResponse> = arrayListOf()
)