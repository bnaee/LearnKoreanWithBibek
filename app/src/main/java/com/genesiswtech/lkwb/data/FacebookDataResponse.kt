package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class FacebookDataResponse
    (
    @SerializedName("height") var height: Int? = null,
    @SerializedName("is_silhouette") var isSilhouette: Boolean? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: Int? = null
)