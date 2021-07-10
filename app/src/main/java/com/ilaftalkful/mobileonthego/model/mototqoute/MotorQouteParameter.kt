package com.ilaftalkful.mobileonthego.model.mototqoute

import com.google.gson.annotations.SerializedName



//{
//    "MotorQuoteID": 0,
//    "ManufacturingYearID": 0,
//    "CarTypeID": 0,
//    "CarMakeID": 0,
//    "CarModelID": 0,
//    "SumInsured": 0,
//    "CreatedBy": 0
//}
open class MotorQouteParameter {
    @SerializedName("MotorQuoteID")
    var motorQuoteId : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("ManufacturingYearID")
    var manufacturingYearID : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("CarTypeID")
    var carTypeID : String? = null
        get() = field
        set(value) {
            field = value
        }

    @SerializedName("CarMakeID")
    var carMakeID : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("CarModelID")
    var carModelID : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("SumInsured")
    var sumInsured : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("CreatedBy")
    var gender : String? = null
        get() = field
        set(value) {
            field = value
        }

}