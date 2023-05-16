package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

data class BannerDataResponse(
    @SerializedName("top_text") var topText: String? = null,
    @SerializedName("header_text") var headerText: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("back_image") var backImage: String? = null,
    @SerializedName("display_image") var display_image : Boolean? = null
)