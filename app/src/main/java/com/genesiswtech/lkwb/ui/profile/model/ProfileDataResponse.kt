package com.genesiswtech.lkwb.ui.profile.model

import com.google.gson.annotations.SerializedName

data class ProfileDataResponse(
    @SerializedName("id"                 ) var id                : Int?           = null,
    @SerializedName("email"              ) var email             : String?        = null,
    @SerializedName("name"               ) var name              : String?        = null,
    @SerializedName("rewardPoint"        ) var rewardPoint       : Int?           = null,
    @SerializedName("refCode"            ) var refCode           : String?        = null,
    @SerializedName("image"              ) var image             : String?        = null,
    @SerializedName("avatar"             ) var avatar            : String?        = null,
    @SerializedName("email_verification" ) var emailVerification : String?        = null,
//    @SerializedName("result_history"     ) var resultHistory     : ResultHistory? = ResultHistory(),
    @SerializedName("provider"           ) var provider          : String?        = null
)
