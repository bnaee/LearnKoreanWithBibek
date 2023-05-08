package com.genesiswtech.lkwb.ui.register.model

import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: LoginDataResponse? = LoginDataResponse()
)
