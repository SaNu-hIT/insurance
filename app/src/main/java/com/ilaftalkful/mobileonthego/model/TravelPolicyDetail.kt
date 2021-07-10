package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class TravelPolicyDetail(val name: String? = null) : BaseObservable() {
    @SerializedName("TravelPolicyDetailID")
    var travelPolicyDetailID: Int? = null
    @SerializedName("TravelPolicyID")
    var travelPolicyID: Int? = null
    @SerializedName("FamilyMemberID")
    var familyMemberID: Int? = null
}
