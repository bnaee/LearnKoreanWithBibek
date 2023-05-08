package com.genesiswtech.lkwb.ui.commentReply.model

import com.google.gson.annotations.SerializedName

data class CommentReplyResponse(
    @SerializedName("success" ) var success : Boolean?        = null,
    @SerializedName("code"    ) var code    : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<CommentReplyDataResponse> = arrayListOf()
)
