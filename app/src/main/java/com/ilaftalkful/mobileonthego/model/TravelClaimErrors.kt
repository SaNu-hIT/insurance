package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ilaftalkful.mobileonthego.BR

data class TravelClaimErrors(val name:String?="Travel Claim Erros" ):BaseObservable(){
    var policyNoError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.policyNoError)
            }

        }

    var accidentDateError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.accidentDateError)
            }

        }

    var policeReportError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.policeReportError)
            }

        }

    var policeReportImageError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.policeReportError)
            }

        }

    var vehicleRegError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.vehicleRegError)
            }

        }
    var carOwnerCivilIdError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.carOwnerCivilIdError)
            }

        }
    var carOwnerCivilIdImageError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.carOwnerCivilIdImageError)
            }

        }

    var driverCivilIdError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.driverCivilIdError)
            }

        }
    var driverCivilIdImageError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.driverCivilIdImageError)
            }

        }

    var driverDriverIdError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.driverDriverIdError)
            }

        }
    var driverDriverIdImageError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.driverDriverIdImageError)
            }

        }

    var damageImagesError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.damageImagesError)
            }

        }

    var policyFirstPageError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.policyFirstPageError)
            }

        }

    var termsAndConditionsError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.termsAndConditionsError)
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

    var expiryDateError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.expiryDateError)
            }

        }

    var carTypeError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.carTypeError)
            }

        }

    var manufacturingYearError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.manufacturingYearError)
            }

        }

    var policyTypeError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.policyTypeError)
            }

        }

    var typeOfClaimError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.typeOfClaimError)
            }

        }

    var dateSicknessError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.dateSicknessError)
            }

        }
    var exitDateError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.exitDateError)
            }

        }
    var entryDateError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.entryDateError)
            }

        }



    var briefError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.briefError)
            }

        }

    var claimAmountError : String? = null
        @Bindable
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.claimAmountError)
            }

        }





}
