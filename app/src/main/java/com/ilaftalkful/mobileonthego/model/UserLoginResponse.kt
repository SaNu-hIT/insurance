package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName




class UserLoginResponse  {
    @SerializedName("access_token")
    var accessToken: String? = null
        get() = field
        set(value) {
            field = value
        }

    @SerializedName("token_type")
    var tokenType: String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("expires_in")
    var expiresIn: Long? = null
        get() = field
        set(value) {
            field = value
        }

   /* @SerializedName("Content")
     var content: UserContent? = null
        get() = field
        set(value) {
            field = value
        }*/
}
