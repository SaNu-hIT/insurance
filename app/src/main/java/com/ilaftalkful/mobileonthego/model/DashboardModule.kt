package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

data class DashboardModule(val name: String? = null) {
    @SerializedName("ModuleName")
    var moduleName: String? = null
    @SerializedName("IsActive")
    var isActive: String? = null
}
