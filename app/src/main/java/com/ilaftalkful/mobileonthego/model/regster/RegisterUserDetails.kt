package com.ilaftalkful.mobileonthego.model.regster

import com.google.gson.annotations.SerializedName


//{
//    "UserName": "PRATHISH T P",
//    "EmailID": "prathishtp.tp@gmail.com",
//    "Password": "Prathish@123",
//    "CivilID": "TestCivilId",
//    "MobileNumber": "9747001174",
//    "DOB": "11/21/1990",
//    "GenderID": 1
//}

open class RegisterUserDetails  {
    @SerializedName("UserId")
    var userId : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("UserName")
    var name : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("EmailID")
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

    @SerializedName("CivilID")
    var civilId : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("MobileNumber")
    var mobilenumber : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("DOB")
    var dob : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("GenderID")
    var gender : String? = null
        get() = field
        set(value) {
            field = value
        }

}