package com.genesiswtech.lkwb.ui.ubtQuestion.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Answers(
    @SerializedName("type"    ) var type    : String?            = null,
    @SerializedName("options" ) var options : ArrayList<Options> = arrayListOf()
): Serializable
