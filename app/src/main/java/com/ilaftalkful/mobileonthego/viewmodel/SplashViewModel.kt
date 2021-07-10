package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.OptionMarketingModel
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashViewModel(application: Application) : IlafBaseViewModel(application) {
    internal var userLiveData: UserLiveUpdate? = null
    var data = MutableLiveData<ArrayList<OptionMarketingModel>>()
    init {
        userLiveData = UserLiveUpdate()
        pref = IlafSharedPreference(application)
    }
    fun getSplashSceenMessage() {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), false)
        val subscribe =
                loginService?.splashMessage()?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                 var result =(it.body()?.Data)
                                    data.postValue(result)
                                userLiveData?.responseSuccess()

                            } else {
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            } // this will tell you why your api doesnt work most of time

                        }, { error ->
                            userLiveData?.postError(ErrorData(100,null))
                            error.printStackTrace()

                        })
    }

    fun needToShowMarketing(): Boolean {
        for(item in data.value?: emptyList()){
            if(item.isActive==1){
                return true
            }
        }
        return false
    }
}