package com.genesiswtech.lkwb.ui.mostPurchase.model

import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.google.gson.annotations.SerializedName

data class PackageListDataResponse(
    @SerializedName("id"         ) var id        : Int?            = null,
    @SerializedName("title"      ) var title     : String?         = null,
    @SerializedName("category"   ) var category  : String?         = null,
    @SerializedName("price"      ) var price     : Int?            = null,
    @SerializedName("base_price" ) var basePrice : Int?            = null,
    @SerializedName("discount"   ) var discount  : Int?            = null,
    @SerializedName("status"     ) var status    : Boolean?        = null,
    @SerializedName("sets"       ) var sets      : ArrayList<UBTTestDataResponse> = arrayListOf()
)
