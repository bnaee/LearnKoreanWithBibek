package com.genesiswtech.lkwb.ui.ubtQuestion.presenter

import android.content.Context

interface IUBTQuestionPresenter {

    fun getUBTQuestion(context: Context, id: Int)
    fun postStoreResult(
        context: Context,
        attempted_questions: String,
        unsolved_questions: String,
        time_spent: String,
        correct_answers: String,
        score: String,
        set_id: String,
        package_id: String
    )
}