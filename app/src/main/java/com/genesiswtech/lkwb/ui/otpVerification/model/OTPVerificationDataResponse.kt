package com.genesiswtech.lkwb.ui.otpVerification.model

import com.google.gson.annotations.SerializedName

data class OTPVerificationDataResponse (
    @SerializedName("token" ) var token : String? = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("email" ) var email : String? = null
        )