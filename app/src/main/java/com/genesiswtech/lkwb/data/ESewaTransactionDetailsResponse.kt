package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class ESewaTransactionDetailsResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("referenceId") var referenceId: String? = null,
    @SerializedName("date") var date: String? = null
)
