package com.genesiswtech.lkwb.ui.ubtBuy.presenter

import android.content.Context

interface IUBTBuyPresenter {
    fun postUBTBuy(
        context: Context,
        sets: String,
        id: Int,
        type: String,
        oid: String,
        amt: String,
        refId: String,
        mobile: Boolean
    )

    fun postKhaltiUBTBuy(
        context: Context,
        sets: String,
        id: Int,
        type: String,
        amt: String,
        isMobile: Boolean,
        pidx: String,
        poid: String,
        pon: String,
        txn_id: String,
        mobile: String
    )
}