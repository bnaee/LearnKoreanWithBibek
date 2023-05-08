package com.genesiswtech.lkwb.ui.profile.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : UpdateProfileDataResponse?    = UpdateProfileDataResponse()
        )