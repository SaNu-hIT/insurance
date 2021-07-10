package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ilaftalkful.mobileonthego.BR

data class SignInErrors(var userName : String?) : BaseObservable() {

    var userNameError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.userNameError)
            }

        }
    var userPasswordError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.userPasswordError)
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