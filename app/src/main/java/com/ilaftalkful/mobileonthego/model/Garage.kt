package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class Garage(val name: String = "garage") : BaseObservable() {

    @SerializedName("GarageID")
     val garageID: Int? = null
    @SerializedName("GarageTypeID")
     val garageTypeID: Int? = null
    @SerializedName("GarageType")
     val garageType: String? = null
    @SerializedName("GarageName")
     val garageName: String? = null
    @SerializedName("GarageRegion")
     val garageRegion: String? = null
    @SerializedName("PhoneNumber")
     val phoneNumber: String? = null
    @SerializedName("EmailID")
     val emailID: String? = null
    @SerializedName("LogoFilePath")
     val logoFilePath: String? = null
    @SerializedName("LogoFileName")
     val logoFileName: String? = null
    @SerializedName("OrderNo")
     val orderNo: Int? = null

}
