package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class FacebookResponse(
    @SerializedName("id"      ) var id      : String?  = null,
    @SerializedName("name"    ) var name    : String?  = null,
    @SerializedName("email"   ) var email   : String?  = null,
    @SerializedName("picture" ) var picture : FacebookPictureResponse? = FacebookPictureResponse()
)
