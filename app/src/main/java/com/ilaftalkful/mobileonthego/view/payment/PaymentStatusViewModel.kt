package com.ilaftalkful.mobileonthego.view.payment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel

class PaymentStatusViewModel(application:Application) : IlafBaseViewModel(application) {
var isSuccess = MutableLiveData<Int>(0)
    var msg = MutableLiveData<String>("")
}