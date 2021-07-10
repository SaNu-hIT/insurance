package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("IsSuccess")
    var isSuccess:Boolean = false
    @SerializedName("Message")
    var messageStatus:String?=null

}
