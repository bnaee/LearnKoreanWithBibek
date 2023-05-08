package com.genesiswtech.lkwb.ui.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: LoginDataResponse? = LoginDataResponse()

)