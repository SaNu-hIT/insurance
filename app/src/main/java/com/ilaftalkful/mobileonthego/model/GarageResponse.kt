package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class GarageResponse(val name:String = "Garage") : BaseObservable(){
     @SerializedName("Garages")
     var garages: List<Garage>? = null
     @SerializedName("GarageTypes")
     var garageTypes: List<GarageType>? = null
}
