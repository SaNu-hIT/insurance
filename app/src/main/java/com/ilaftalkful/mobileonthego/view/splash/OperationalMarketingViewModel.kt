package com.ilaftalkful.mobileonthego.view.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.OptionMarketingModel
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel

class OperationalMarketingViewModel(application: Application) : IlafBaseViewModel(application) {


    internal var userLiveData: UserLiveUpdate? = null
    var data = MutableLiveData<ArrayList<OptionMarketingModel>>()
    var message = MutableLiveData<String>("")
    var isMoreDetailVisible = MutableLiveData<Boolean>(false)

init {
    pref = IlafSharedPreference(application)
    userLiveData = UserLiveUpdate()

    if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null &&
            pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
        isENS.postValue(false)
    }else {
        isENS.postValue(true)
    }
}
    fun setMessage(mesage: String) {
        message.postValue(mesage)
    }
}