package com.genesiswtech.lkwb.ui.favouriteDetail.model

import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.google.gson.annotations.SerializedName

data class FavouriteDetailDictionaryResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<DictionarySearchDataResponse> = arrayListOf()
)