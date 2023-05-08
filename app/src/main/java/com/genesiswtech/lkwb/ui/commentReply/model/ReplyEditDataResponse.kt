package com.genesiswtech.lkwb.ui.commentReply.model

import com.google.gson.annotations.SerializedName

data class ReplyEditDataResponse (
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("comment_id" ) var commentId : String? = null,
    @SerializedName("body"       ) var body      : String? = null,
    @SerializedName("user_name"  ) var userName  : String? = null,
    @SerializedName("user_image" ) var userImage : String? = null,
    @SerializedName("date"       ) var date      : String? = null
        )