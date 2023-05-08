package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

data class TestScoreDataResponse(
    @SerializedName("number_of_questions") var numberOfQuestions: Int? = null,
    @SerializedName("attempted_questions") var attemptedQuestions: Int? = null,
    @SerializedName("unsolved_questions") var unsolvedQuestions: Int? = null,
    @SerializedName("correct_answers") var correctAnswers: Int? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("out_of_100") var outOf100: Double? = null,
    @SerializedName("reward_point") var rewardPoint: Double? = null
)