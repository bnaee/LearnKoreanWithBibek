package com.genesiswtech.lkwb.ui.ubtQuestion.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Options(
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("is_correct" ) var isCorrect : Int?    = null,
    @SerializedName("option"     ) var option    : String? = null
): Serializable
