package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class TravelPlansResponse(val name: String? = null) : BaseObservable(){
     @SerializedName("TravelPlanDescription")
     var travelPlanDescription: String? = null
     @SerializedName("PlatinumPlanValue")
     var platinumPlanValue: String ? = null
     @SerializedName("GoldPlanValue")
     var goldPlanValue: String ? = null
     @SerializedName("SilverPlanValue")
     var silverPlanValue: String ? = null
}
