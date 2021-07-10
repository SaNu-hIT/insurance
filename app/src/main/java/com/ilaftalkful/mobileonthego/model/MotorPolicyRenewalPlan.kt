package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

data class MotorPolicyRenewalPlan(val name: String?=null) {
    @SerializedName("AddonID")
    var addonID: Int? = null
    @SerializedName("IsOpted")
    var isOpted: Int? = null
}
