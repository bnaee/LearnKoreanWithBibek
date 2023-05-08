package com.genesiswtech.lkwb.ui.ubtBuy.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UBTBuyDataResponse(
    @SerializedName("invoice_number" ) var invoiceNumber : String? = null,
    @SerializedName("purchase_date"  ) var purchaseDate  : String? = null,
    @SerializedName("payment_method" ) var paymentMethod : String? = null,
    @SerializedName("set_id"         ) var setId         : Int?    = null,
    @SerializedName("set"            ) var set           : String? = null,
    @SerializedName("amount"         ) var amount        : String? = null

): Serializable
