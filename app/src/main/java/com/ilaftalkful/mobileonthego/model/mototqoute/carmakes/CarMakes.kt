package com.ilaftalkful.mobileonthego.model.mototqoute.cartype

import com.google.gson.annotations.SerializedName

open class CarMakes {
    @SerializedName("Id")
    var id : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("Text")
    var text : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("TextAr")
    var textAr : String? = null
        get() = field
        set(value) {
            field = value
        }

}