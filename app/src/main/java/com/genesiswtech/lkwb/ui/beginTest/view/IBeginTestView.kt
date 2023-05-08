package com.genesiswtech.lkwb.ui.beginTest.view

import android.view.View
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse

interface IBeginTestView {

    fun initDependencies()
    fun onBeginButtonClick(v: View?)

    fun onSuccess(beginTestResponse: BeginTestResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}