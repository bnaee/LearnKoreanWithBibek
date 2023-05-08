package com.genesiswtech.lkwb.ui.beginTest.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BeginTestDataResponse(
    @SerializedName("id"          ) var id          : Int?     = null,
    @SerializedName("title"       ) var title       : String?  = null,
    @SerializedName("slug"        ) var slug        : String?  = null,
    @SerializedName("price"       ) var price       : Int?     = null,
    @SerializedName("status"      ) var status      : Boolean? = null,
    @SerializedName("description" ) var description : String?  = null,
    @SerializedName("listening"   ) var listening   : Int?     = null,
    @SerializedName("reading"     ) var reading     : Int?     = null,
    @SerializedName("time"        ) var time        : String?  = null,
    @SerializedName("full_marks"  ) var fullMarks   : Int?     = null,
    @SerializedName("pass_marks"  ) var passMarks   : Int?     = null
): Serializable
