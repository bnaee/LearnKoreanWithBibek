package com.genesiswtech.lkwb.ui.ubt.view

import android.view.View
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse

interface IUBTTestView {

    fun initDependencies()

    fun onPackageSuccess(packageData: ArrayList<PackageDataResponse>)

    fun onSuccess(ubtTestResponse: UBTTestResponse)

    fun onFailure(message: String?)

    fun onTimeOut()

    fun showSnackBar(message: String?)

    fun onShowProgressBar(status: Boolean)

    fun onShowBottomProgressBar(status: Boolean)

    fun onChooseProgressBar(page: Int,status: Boolean)
}