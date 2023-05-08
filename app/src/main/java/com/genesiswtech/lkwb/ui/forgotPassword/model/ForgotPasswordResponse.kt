package com.genesiswtech.lkwb.ui.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("success" ) var success : Boolean?          = null,
    @SerializedName("code"    ) var code    : Int?              = null,
    @SerializedName("message" ) var message : String?           = null,
    @SerializedName("data"    ) var data    : ArrayList<String> = arrayListOf()
)
