package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class FacebookPictureResponse
    (
    @SerializedName("data") var data: FacebookDataResponse? = FacebookDataResponse()
)