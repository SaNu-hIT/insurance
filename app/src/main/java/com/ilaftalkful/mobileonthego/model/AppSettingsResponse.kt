package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

data class AppSettingsResponse(val name:String?=null){
    @SerializedName("ModuleName")
    var moduleName:String?=null

    @SerializedName("AppSettingName")
    var appSettingsName:String?=null

    @SerializedName("AppSettingValue")
    var appSettingsValue:String?=null

    @SerializedName("TermsAndConditionValue")
    var termsAndConditionsValue:String?=null
}
