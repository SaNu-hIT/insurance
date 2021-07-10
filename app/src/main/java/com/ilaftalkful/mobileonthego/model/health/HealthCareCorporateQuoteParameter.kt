package com.ilaftalkful.mobileonthego.model.health

import com.google.gson.annotations.SerializedName




//{
//    "CompanyName": "string",
//    "ContactPersonName": "string",
//    "ContactPersonEmail": "string",
//    "ContactPersonMobile": "string",
//    "DepartmentID": 0,
//    "MessageDetail": "string"
//}
open class HealthCareCorporateQuoteParameter {
    @SerializedName("CompanyName")
    var companyName : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("ContactPersonName")
    var contactPersonName : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("ContactPersonEmail")
    var contactPersonEmail : String? = null
        get() = field
        set(value) {
            field = value
        }

    @SerializedName("ContactPersonMobile")
    var contactPersonMobile : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("DepartmentID")
    var departmentID : String? = null
        get() = field
        set(value) {
            field = value
        }
    @SerializedName("MessageDetail")
    var messageDetail : String? = null
        get() = field
        set(value) {
            field = value
        }

}