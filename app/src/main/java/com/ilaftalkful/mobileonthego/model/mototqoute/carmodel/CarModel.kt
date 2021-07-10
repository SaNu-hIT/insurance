package com.ilaftalkful.mobileonthego.model.mototqoute.carmodel

import com.google.gson.annotations.SerializedName
//"Id": 1,
//"Text": "X5",
//"TextAr": null

open class CarModel {
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
    var textAr : String? = ""
        get() = field
        set(value) {
            field = value
        }

}