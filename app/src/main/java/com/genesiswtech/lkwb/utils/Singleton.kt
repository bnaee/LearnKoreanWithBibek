package com.genesiswtech.lkwb.utils

object Singleton {
    // your fields, getter and setter here

    var instance: Singleton? = null
        get() {
            if (field == null) {
                field = Singleton
            }
            return field
        }

    private var data: String? = null
    fun getData(): String? {
        return data
    }

    fun setData(data: String?) {
        this.data = data
    }
}