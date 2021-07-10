package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

data  class ManufacturingYears(val anme:String?= null) {

    @SerializedName("ManufacturingYearID")
    var manufacturingYearID:String?=null

    @SerializedName("ManufacturingYear")
    var manufacturingYear:String?=null
}
