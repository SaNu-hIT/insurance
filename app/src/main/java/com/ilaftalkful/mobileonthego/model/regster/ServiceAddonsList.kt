package com.ilaftalkful.mobileonthego.model.regster

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class ServiceAddonsList(val name: String? = null) : BaseObservable() {
    @SerializedName("AddonID")
    var addonID: Int? = null
    @SerializedName("AddonDescription")
    var addonDescription: String? = null
    @SerializedName("AddonAmount")
    var addonAmount: Double ? = null
}
