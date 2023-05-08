package com.genesiswtech.lkwb.ui.mostPurchase.presenter

import android.content.Context

interface IMostPurchaseInterface {

    fun getMostBought(context: Context, page: String, limit: String)

    fun getNewSets(context: Context, page: String, limit: String)

    fun getPackageList(context: Context, id: Int, page: String, limit: String)

    fun getUBTTest(context: Context, page: String, limit: String, type: String, sort: String)
}