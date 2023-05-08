package com.genesiswtech.lkwb.ui.menu.model

import com.google.gson.annotations.SerializedName

data class LogOutResponse (
    @SerializedName("success" ) var success : Boolean?          = null,
    @SerializedName("code"    ) var code    : Int?              = null,
    @SerializedName("message" ) var message : String?           = null,
    @SerializedName("data"    ) var data    : ArrayList<String> = arrayListOf()
        )