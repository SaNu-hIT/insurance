package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class RenewPolicyRequest(val name: String? = null): BaseObservable(){
    @SerializedName("MotorPolicyRenewalDetails")
     var motorPolicyRenewalDetails: MotorPolicyRenewalDetails? = null
    @SerializedName("MotorPolicyRenewalPlan")
     var motorPolicyRenewalPlans: List<MotorPolicyRenewalPlan>? = null
}
