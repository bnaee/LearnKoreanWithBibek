package com.genesiswtech.lkwb.data

import com.google.gson.annotations.SerializedName

data class ESewaResponse(
    @SerializedName("productId") var productId: String? = null,
    @SerializedName("productName") var productName: String? = null,
    @SerializedName("totalAmount") var totalAmount: String? = null,
    @SerializedName("environment") var environment: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("merchantName") var merchantName: String? = null,
    @SerializedName("message") var message: ESewaMessageResponse? = ESewaMessageResponse(),
    @SerializedName("transactionDetails") var transactionDetails: ESewaTransactionDetailsResponse? = ESewaTransactionDetailsResponse()
)
