package com.ilaftalkful.mobileonthego.model

import com.google.gson.annotations.SerializedName

data class UserDetailsDashBoard(val name: String? = null) {
     @SerializedName("UserDetailID")
     var userDetailID: Int? = null
     @SerializedName("UserID")
     var userID: Int? = null
     @SerializedName("CivilID")
     var civilID: String? = null
     @SerializedName("NameFirst")
     var nameFirst: String? = null
     @SerializedName("NameLast")
     var nameLast: String? = null
     @SerializedName("EmailID")
     var emailID: String? = null
     @SerializedName("DOB")
     var dOB: String? = null
     @SerializedName("MobileNumber")
     var mobileNumber: String? = null
     @SerializedName("Gender")
     var gender: String? = null
     @SerializedName("MaximumAge")
     var maxAge: String? = null
}
