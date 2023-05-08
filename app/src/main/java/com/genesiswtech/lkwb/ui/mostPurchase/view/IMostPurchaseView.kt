package com.genesiswtech.lkwb.ui.mostPurchase.view

import com.genesiswtech.lkwb.ui.home.model.MostBoughtResponse
import com.genesiswtech.lkwb.ui.mostPurchase.model.PackageListDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse

interface IMostPurchaseView {

    fun initDependencies()

    fun onSuccess(ubtTestResponse: UBTTestResponse)

    fun onMostPurchaseSuccess(mostBoughtResponse: MostBoughtResponse)

    fun onPackageSuccess(packageData: PackageListDataResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}