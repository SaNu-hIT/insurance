package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class TravelClaimSubmitRequest(val name: String? = null) : BaseObservable(){
    @SerializedName("TravelPolicyID")
    var travelPolicyID: Int? = null
    @SerializedName("ClaimTypeID")
    var claimTypeID: Int? = null
    @SerializedName("DateSickness")
    var dateSickness: String? = null
    @SerializedName("DateEntry")
    var dateEntry: String? = null
    @SerializedName("DateExit")
    var dateExit: String? = null
    @SerializedName("BriefDescription")
    var briefDescription: String? = null
    @SerializedName("ClaimAmount")
    var claimAmount: Int? = null
    @SerializedName("TravelClaimAttachmentModels")
    var travelClaimAttachmentModels: List<TravelClaimAttachmentModel>? = null
}
