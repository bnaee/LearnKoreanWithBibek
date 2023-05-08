package com.genesiswtech.lkwb.ui.login.model

import com.google.gson.annotations.SerializedName

data class LoginDataResponse(
    @SerializedName("token") var token: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("rewardPoint") var rewardPoint: Double? = null,
    @SerializedName("refCode") var refCode: String? = null,
    @SerializedName("email_verification") var emailVerification: String? = null,
    @SerializedName("isNewUser") var isNewUser: Boolean? = null,
    @SerializedName("provider") var provider: String? = null
)