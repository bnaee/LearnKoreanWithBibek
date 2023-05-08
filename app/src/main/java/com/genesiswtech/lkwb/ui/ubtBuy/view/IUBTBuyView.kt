package com.genesiswtech.lkwb.ui.ubtBuy.view

import android.view.View
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestResponse
import com.genesiswtech.lkwb.ui.ubtBuy.model.UBTBuyDataResponse
import com.genesiswtech.lkwb.ui.ubtBuy.model.UBTBuyResponse

interface IUBTBuyView {

//    fun onKhaltiButtonClick(v: View?)
    fun onProceedButtonClick(v: View?)
    fun onBackButtonClick(v: View?)

    fun initDependencies()

    fun onSuccess(ubtBuyResponse: UBTBuyResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}