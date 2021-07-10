package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OptionMarketingModel(val name: String? = null):BaseObservable(), Serializable {
    @SerializedName("SplashMessageID")
    var splashMessageID: Int? = null
    @SerializedName("SplashMessage")
    var splashMessage: String? = null
    @SerializedName("SplashMessageAR")
    var splashMessageAR: String? = null
    @SerializedName("SplashURL")
    var splashURL: String? = null
    @SerializedName("IsActive")
    var isActive: Int? = null
    @SerializedName("Message")
    var message: Any? = null
    @SerializedName("Status")
    var status: Any? = null
}