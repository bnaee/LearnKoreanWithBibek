package com.genesiswtech.lkwb.ui.home.model

import com.google.gson.annotations.SerializedName

data class ServiceInfo(

    @SerializedName("top_text"    ) var topText     : String? = null,
    @SerializedName("header_text" ) var headerText  : String? = null,
    @SerializedName("description" ) var description : String? = null

)
