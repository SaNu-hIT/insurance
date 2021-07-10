package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference

class DashboardViewModel(application: Application) : IlafBaseViewModel(application) {
    internal var userLiveData: UserLiveUpdate? = null

    init {
        userLiveData = UserLiveUpdate()
    }

    init {
        pref = IlafSharedPreference(application)
    }



}