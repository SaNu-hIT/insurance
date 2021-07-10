package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable

data class TravelPolicyTypes(val name:String?=null) : BaseObservable(){

    var  data: List<TravelPolicyType>? = null

}
