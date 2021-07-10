package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class GarageType(val anme: String = "Garage Type") : BaseObservable(){
     @SerializedName("GarageTypeID")
     var garageTypeID: Int? = null
     @SerializedName("GarageTypeName")
     var garageTypeName: String? = null

}
