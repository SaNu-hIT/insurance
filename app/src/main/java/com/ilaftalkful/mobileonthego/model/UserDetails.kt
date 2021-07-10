package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName


open class UserDetails {
    @SerializedName("UserName")
    var email : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("Password")
    var password : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("grant_type")
    var grantType : String? = "password"
        get() = field
        set(value) {
            field = value
        }

}