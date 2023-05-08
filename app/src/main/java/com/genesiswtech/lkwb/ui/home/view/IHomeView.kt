package com.genesiswtech.lkwb.ui.home.view

import android.view.View
import com.genesiswtech.lkwb.ui.home.model.*

interface IHomeView {

    fun initDependencies()

    fun onGraphViewClick(v: View?)

    fun onMostBuyViewClick(v: View?)

    fun onNewTestViewClick(v: View?)

    fun onHomeBannerClick(v: View?)

    fun onMostBoughtSuccess(mostBoughtResponse: MostBoughtResponse)

    fun onNewTestSuccess(newTestResponse: MostBoughtResponse)

    fun onServicesSuccess(serviceResponse: ServiceResponse)

    fun onBannerSuccess(bannerDataResponse: BannerDataResponse)

    fun onLastTestScoreSuccess(testScoreResponse: TestScoreDataResponse)

    fun onResultHistoryAppSuccess(resultHistoryDataResponse: ArrayList<ResultHistoryDataResponse>)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)
}