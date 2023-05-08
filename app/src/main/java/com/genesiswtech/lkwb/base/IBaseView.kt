package com.genesiswtech.lkwb.base

interface IBaseView {

    abstract fun showLoading()

    abstract fun hideLoading()

    abstract fun loadError(e: Throwable)

    abstract fun loadError(msg: String)


}