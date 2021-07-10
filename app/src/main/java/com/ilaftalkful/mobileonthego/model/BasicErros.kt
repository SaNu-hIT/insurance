package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ilaftalkful.mobileonthego.BR

data class BasicErros(val name:String?=null) : BaseObservable() {

    var sumInsuredError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.sumInsuredError)
            }

        }

    var uiUpdate : Boolean? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.uiUpdate)
            }

        }
}