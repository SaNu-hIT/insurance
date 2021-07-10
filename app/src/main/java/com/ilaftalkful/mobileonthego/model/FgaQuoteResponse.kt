package com.ilaftalkful.mobileonthego.model

data class FgaQuoteResponse(val name:String?=null) {
    var companyName: String? = null
    var contactPersonName: String? = null
    var contactPersonEmail: String? = null
    var contactPersonMobile: String? = null
    var department: String? = null
    var messageDetail: String? = null
}