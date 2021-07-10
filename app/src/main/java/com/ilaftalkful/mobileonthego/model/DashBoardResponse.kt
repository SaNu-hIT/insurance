package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class DashBoardResponse() : Serializable {

    @SerializedName("UserDetails")
     var userDetails: UserDetailsDashBoard? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("DashboardModules")
     var dashboardModules: List<DashboardModule>? = null
        get() = field
        set(value) {
            field = value
        }

}
