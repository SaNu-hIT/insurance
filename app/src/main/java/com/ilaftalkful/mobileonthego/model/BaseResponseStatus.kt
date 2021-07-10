package com.ilaftalkful.mobileonthego.model

open class BaseResponseStatus<T>: BaseResponse() {
    var Data: T ?=null
}