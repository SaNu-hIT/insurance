package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class TravelPolicyType(val anme: String? = null) : BaseObservable() {

    @SerializedName("Id")
    var id: Int? = null
    @SerializedName("Text")
    var text: String? = null
}
