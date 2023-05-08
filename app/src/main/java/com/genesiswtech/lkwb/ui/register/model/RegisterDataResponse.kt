package com.genesiswtech.lkwb.ui.register.model

import com.google.gson.annotations.SerializedName

class RegisterDataResponse(
    @SerializedName("token") var token: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("email_verification") var emailVerification: String? = null

)