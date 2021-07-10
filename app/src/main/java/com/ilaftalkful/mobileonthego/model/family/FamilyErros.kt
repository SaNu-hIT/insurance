package com.ilaftalkful.mobileonthego.model.family

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ilaftalkful.mobileonthego.BR

data class FamilyErros(val name: String? = null) : BaseObservable() {
    var mDOBNameError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.mDOBNameError)
            }

        }
    var mFamilyMemberIDError : String? = null
    @Bindable
    get() = field
    set(value)
    {
        if (field != value) {
            field = value
            notifyPropertyChanged(BR.mFamilyMemberIDError)
        }

    }
    var mFullNameError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.mFullNameError)
            }

        }
    var mPassportNumberError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.mPassportNumberError)
            }

        }
    var mRelationIDError: String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.mRelationIDError)
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