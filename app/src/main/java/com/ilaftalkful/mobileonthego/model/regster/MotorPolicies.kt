package com.ilaftalkful.mobileonthego.model.regster

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data  class MotorPolicies(val anme: String? = null) : BaseObservable() {

    @SerializedName("PolicyList")
    var policyList: List<PolicyList>? = null
    @SerializedName("ServiceAddonsList")
    var serviceAddonsList: List<ServiceAddonsList>? = null

}
