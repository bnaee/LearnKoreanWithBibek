package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class ESewaMessageResponse (
    @SerializedName("technicalSuccessMessage" ) var technicalSuccessMessage : String? = null,
    @SerializedName("successMessage"          ) var successMessage          : String? = null
        )