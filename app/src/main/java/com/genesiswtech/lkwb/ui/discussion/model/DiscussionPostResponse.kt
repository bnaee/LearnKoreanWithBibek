package com.genesiswtech.lkwb.ui.discussion.model

import com.google.gson.annotations.SerializedName

data class DiscussionPostResponse (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : DiscussionPostDataResponse?    = DiscussionPostDataResponse()
        )