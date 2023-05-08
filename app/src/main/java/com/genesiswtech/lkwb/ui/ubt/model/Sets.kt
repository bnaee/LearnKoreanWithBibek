package com.genesiswtech.lkwb.ui.ubt.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sets(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("isNew") var isNew: Boolean? = null,
    @SerializedName("base_price") var basePrice: Int? = null,
    @SerializedName("discount") var discount: Int? = null,
    @SerializedName("canAccessSet") var canAccessSet: Boolean? = null
) : Serializable