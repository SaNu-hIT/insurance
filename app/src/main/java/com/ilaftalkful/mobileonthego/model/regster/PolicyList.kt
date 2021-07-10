package com.ilaftalkful.mobileonthego.model.regster

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class PolicyList(val name: String? = null) : BaseObservable() {
    @SerializedName("MotorPolicyID")
    var motorPolicyID: Int? = null
    @SerializedName("PolicyNumber")
    var policyNumber: String? = null
    @SerializedName("CarType")
    var carType: String? = null
    @SerializedName("ManufacturingYear")
    var manufacturingYear: String? = null
    @SerializedName("Amount")
    var amount: Int? = null
    @SerializedName("ExpiryDate")
    var expiryDate: String? = null
}
