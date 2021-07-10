package com.ilaftalkful.mobileonthego.model

class ErrorData {
    internal var errorCode: Int = 0
    internal var errorMessage: String?=null

    constructor(errorCode: Int, errorMessage: String?) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    fun getErrorCode(): Int {
        return errorCode
    }

    fun getErrorMessage(): String {
        return errorMessage?:"Unknown Error"
    }
}