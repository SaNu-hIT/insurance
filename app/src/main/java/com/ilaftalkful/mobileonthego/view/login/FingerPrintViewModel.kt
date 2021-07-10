package com.ilaftalkful.mobileonthego.view.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.SignInErrors
import com.ilaftalkful.mobileonthego.model.UserDetails
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FingerPrintViewModel(application: Application) : IlafBaseViewModel(application) {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    internal var userLiveData: UserLiveUpdate? = null
    lateinit var error: SignInErrors

    init {
        userLiveData = UserLiveUpdate()
        pref =IlafSharedPreference(application)
        username.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL))
    }
    fun callLogin() {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), false)
        var userDetails = UserDetails()
        userDetails.email=IlafSharedPreference(getApplication()).getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL)
        if(userDetails.email!=null){
        userDetails.password= Utility.getUserPin(getApplication())
            val subscribe =
                    loginService?.doSignIn(userDetails.email!!, userDetails.password!!)?.observeOn(
                            AndroidSchedulers.mainThread()
                    )
                            ?.subscribeOn(
                                    Schedulers.io()
                            )
                            ?.subscribe({
                                if (it.isSuccessful()) {
                                    if (it.code() == 200) {
                                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, it.body()?.accessToken)
                                        userLiveData?.userLoginSuccess()
                                    } else {
                                        if (it.code() == Constants.UNAUTH_ERROR) {
                                            pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                            pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                            userLiveData?.sessionExpired()
                                        } else {
                                            userLiveData?.postError(ErrorData(it.code(), null))
                                        }
                                    }
                                } else {
                                    userLiveData?.postError(ErrorData(it.code(), it.message()))
                                } // this will tell you why your api doesnt work most of time

                            }, { error ->
                                userLiveData?.postError(ErrorData(100, null))
                                error.printStackTrace()

                            })
        }else{
            userLiveData?.fingerPrintLoginFailed()
        }
    }
}