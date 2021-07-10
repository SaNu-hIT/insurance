package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference

open class IlafBaseViewModel(application: Application) : AndroidViewModel(application) {
    open var isENS :MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    lateinit var pref :IlafSharedPreference
init {
    pref = IlafSharedPreference(application)
    if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null
        && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
        isENS.postValue(false)
    }else {
        isENS.postValue(true)
    }
}
}
