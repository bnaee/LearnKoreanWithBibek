package com.genesiswtech.lkwb.ui.favouriteDetail.model

import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordResponse
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordResponse
import com.google.gson.annotations.SerializedName

data class FavouriteDetailBlogResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<BlogDataResponse> = arrayListOf(),
    )
