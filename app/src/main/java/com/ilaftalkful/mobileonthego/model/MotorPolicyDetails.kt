package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class MotorPolicyDetails(val name: String? = null) : BaseObservable()
{
    @SerializedName("MotorPolicyID")
    var motorPolicyID: Int? = null
    @SerializedName("ExpiryDate")
    var expiryDate: String? = null
    @SerializedName("CivilIDNumber")
    var civilIDNumber: String? = null
    @SerializedName("PolicyNumber")
    var policyNumber: String? = null
    @SerializedName("PolicyRenewalSl")
    var policyRenewalSl: Int? = null
    @SerializedName("PolicyRenewalPremium")
    var policyRenewalPremium: Double? = null
    @SerializedName("PolicyRenewalTNC")
    var policyRenewalTNC: String? = null
    @SerializedName("PolicyBeneficiary")
    var policyBeneficiary: String? = null
    @SerializedName("PolicyAddress")
    var policyAddress: String? = null
    @SerializedName("AssuranceTelephone")
    var assuranceTelephone: String? = null
    @SerializedName("AssuranceNationality")
    var assuranceNationality: String? = null
    @SerializedName("RegisterNumber")
    var registerNumber: String? = null
    @SerializedName("Make")
    var make: String? = null
    @SerializedName("Model")
    var model: String? = null
    @SerializedName("ManufacturingYear")
    var manufacturingYear: String? = null
    @SerializedName("Chassis")
    var chassis: String? = null
    @SerializedName("NoOfpass")
    var noOfpass: Int? = null
    @SerializedName("Body")
    var body: String? = null
    @SerializedName("Color")
    var color: String? = null
    @SerializedName("Fuel")
    var fuel: String? = null
    @SerializedName("MotorNumber")
    var motorNumber: String? = null
    @SerializedName("Usage")
    var usage: String? = null
    @SerializedName("PolicyAssuranceName")
    var policyAssuranceName: String? = null
    @SerializedName("PloicyFromDate")
    var ploicyFromDate: String? = null
    @SerializedName("PolicyToDate")
    var policyToDate: String? = null
}