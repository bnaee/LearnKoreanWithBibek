package com.genesiswtech.lkwb.ui.graph.view

import com.genesiswtech.lkwb.ui.home.model.ResultHistoryDataResponse

interface IGraphView {

    fun initDependencies()

    fun onResultHistoryAppSuccess(resultHistoryDataResponse: ArrayList<ResultHistoryDataResponse>)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}