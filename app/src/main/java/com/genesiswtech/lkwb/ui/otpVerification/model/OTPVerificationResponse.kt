package com.genesiswtech.lkwb.ui.otpVerification.model

import com.google.gson.annotations.SerializedName

data class OTPVerificationResponse (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : OTPVerificationDataResponse?    = OTPVerificationDataResponse()

)