package com.genesiswtech.lkwb.ui.ubtQuestion.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UBTQuestionDataResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("groupTitle") var groupTitle: String? = null,
    @SerializedName("mainTitle") var mainTitle: String? = null,
    @SerializedName("subTitle") var subTitle: String? = null,
    @SerializedName("answers") var answers: Answers? = Answers(),
    @SerializedName("file") var file: String? = null,
    @SerializedName("file_type") var fileType: String? = null
) : Serializable
