package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

data class MotorPolicyRenewalDetails(val name: String?=null) {
    @SerializedName("MotorRenewalID")
    val motorRenewalID: Int? = null
    @SerializedName("MotorPolicyID")
    var motorPolicyID: Int? = null
    @SerializedName("PolicyNumber")
     var policyNumber: String? = null
    @SerializedName("ExpiryDate")
     var expiryDate: String? = null
    @SerializedName("CarTypeID")
     var carTypeID: Int? = null
    @SerializedName("ManufacturingYear")
     var manufacturingYear: Int? = null
    @SerializedName("RenewalAmountPremium")
     var renewalAmountPremium: Int? = null
    @SerializedName("RenewalAmountFinal")
     var renewalAmountFinal: Int? = null
}
