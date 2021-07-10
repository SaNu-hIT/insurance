package com.ilaftalkful.mobileonthego.model.health

import com.google.gson.annotations.SerializedName

//{
//    "Id": 1,
//    "Text": "Box",
//    "TextAr": null
//},
open class Department {
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

}