package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class TravelClaimAttachmentModel(val name: String? = null) : BaseObservable() {
    @SerializedName("ClaimFileName")
    var claimFileName: String? = null
    @SerializedName("ClaimFilePath")
    var claimFilePath: String? = null
}
