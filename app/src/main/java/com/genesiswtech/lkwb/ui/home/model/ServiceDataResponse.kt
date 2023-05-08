package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

class ServiceDataResponse {
    @SerializedName("service_info" ) var serviceInfo : ServiceInfo?        = ServiceInfo()
    @SerializedName("services"     ) var services    : ArrayList<Services> = arrayListOf()
}