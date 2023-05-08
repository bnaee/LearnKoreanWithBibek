package com.genesiswtech.lkwb.ui.ubtQuestion.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreResultDataResponse(
    @SerializedName("attempted_questions" ) var attemptedQuestions : String? = null,
    @SerializedName("unsolved_questions"  ) var unsolvedQuestions  : String? = null,
    @SerializedName("time_spent"          ) var timeSpent          : String? = null,
    @SerializedName("correct_answers"     ) var correctAnswers     : String? = null,
    @SerializedName("score"               ) var score              : String? = null,
    @SerializedName("set_user_id"         ) var setUserId          : Int?    = null,
    @SerializedName("exam_id"             ) var examId             : String? = null,
    @SerializedName("user_id"             ) var userId             : Int?    = null,
    @SerializedName("updated_at"          ) var updatedAt          : String? = null,
    @SerializedName("created_at"          ) var createdAt          : String? = null,
    @SerializedName("id"                  ) var id                 : Int?    = null,
    @SerializedName("set"                 ) var set                : String? = null,
    @SerializedName("audio"               ) var audio              : String? = null

): Serializable
