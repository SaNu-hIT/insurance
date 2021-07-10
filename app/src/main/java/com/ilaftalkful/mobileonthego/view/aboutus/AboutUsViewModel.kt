package com.ilaftalkful.mobileonthego.view.aboutus

import android.app.Application
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel

class AboutUsViewModel(application: Application) : IlafBaseViewModel(application) {

    init {
        pref = IlafSharedPreference(application)
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals("AR")){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
    }
}