package com.ilaftalkful.mobileonthego.model.health

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ilaftalkful.mobileonthego.BR

data class FamilyErros(val name: String? = null) : BaseObservable() {
    var contactPersonNameError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.contactPersonNameError)
            }

        }
    var companyNameError : String? = null
    @Bindable
    get() = field
    set(value)
    {
        if (field != value) {
            field = value
            notifyPropertyChanged(BR.companyNameError)
        }

    }
    var contactPersonEmailError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.contactPersonEmailError)
            }

        }
    var contactPersonMobileError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.contactPersonMobileError)
            }

        }
    var departmentIDError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.departmentIDError)
            }

        }


    var messageDetailError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.messageDetailError)
            }

        }

    var uiUpdate: Boolean? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.uiUpdate)
            }

        }
}