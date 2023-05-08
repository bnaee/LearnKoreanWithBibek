package com.genesiswtech.lkwb.ui.ubtQuestion.view

import android.view.View
import com.genesiswtech.lkwb.ui.ubtQuestion.model.StoreResultDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse

interface IUBTQuestionView {

    fun initDependencies()

    fun onPreviousButtonClick(v: View?)
    fun onNextButtonClick(v: View?)
    fun onAllQuestionButtonClick(v: View?)
    fun onSubmitButtonClick(v: View?)

    fun onStoreResultSuccess(storeResultDataResponse: StoreResultDataResponse)

    fun onSuccess(ubtQuestionResponse: UBTQuestionResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}