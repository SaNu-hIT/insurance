package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class PaymentRequest(val name: String? = null) : BaseObservable(){
    @SerializedName("TravelPolicyID")
    var travelPolicyID: Int? = null
    @SerializedName("NameFirst")
    var nameFirst: String? = null
    @SerializedName("NameSecond")
    var nameSecond: String? = null
    @SerializedName("NameFamily")
    var nameFamily: String? = null
    @SerializedName("MobileNumber")
    var mobileNumber: String? = null
    @SerializedName("EmailID")
    var emailID: String? = null
    @SerializedName("DOB")
    var dOB: String? = null
    @SerializedName("NationalityID")
    var nationalityID: Int? = null
    @SerializedName("PassportNumber")
    var passportNumber: String? = null
    @SerializedName("PassportFilePath")
    var passportFilePath: String? = null
    @SerializedName("PassportFileName")
    var passportFileName: String? = null
    @SerializedName("CivilIDNumber")
    var civilIDNumber: String? = null
    @SerializedName("CivilIDFilePath")
    var civilIDFilePath: String? = null
    @SerializedName("CivilIDFileName")
    var civilIDFileName: String? = null
    @SerializedName("PolicyTypeID")
    var policyTypeID: Int? = null
    @SerializedName("PolicyOptionID")
    var policyOptionID: Int? = null
    @SerializedName("PolicyPeriodID")
    var policyPeriodID: Int? = null
    @SerializedName("PolicyDateStart")
    var policyDateStart: String? = null
    @SerializedName("PolicyDateEnd")
    var policyDateEnd: String? = null
    @SerializedName("IsActive")
    var isActive: Int? = null
    @SerializedName("IsDraft")
    var isDraft: Boolean? = null
    @SerializedName("TravelPolicyDetails")
    var travelPolicyDetails: List<TravelPolicyDetail>? = null
}
