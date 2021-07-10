package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ilaftalkful.mobileonthego.BR

data class UserRegistrationErrors(var userName : String?) : BaseObservable() {

    var nameError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.nameError)
            }

        }

    var dobError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.dobError)
            }

        }
    var userEmailError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.userEmailError)
            }

        }


    var phoneNumberError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.phoneNumberError)
            }

        }
    var civilidError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.civilidError)
            }

        }

    var genderError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.genderError)
            }

        }

    var passwordError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.passwordError)
            }

        }

    var confirmError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.confirmError)
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