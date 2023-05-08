package com.genesiswtech.lkwb.ui.profile.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileDataResponse(
    @SerializedName("email"       ) var email       : String? = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("rewardPoint" ) var rewardPoint : Double? = null,
    @SerializedName("refCode"     ) var refCode     : String? = null,
    @SerializedName("image"       ) var image       : String? = null
)
