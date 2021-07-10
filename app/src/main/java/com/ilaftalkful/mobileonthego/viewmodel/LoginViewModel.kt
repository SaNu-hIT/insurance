package com.ilaftalkful.mobileonthego.view

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.model.regster.RegisterUserDetails
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LoginViewModel(application: Application) : IlafBaseViewModel(application) {


    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    internal var userLiveData: UserLiveUpdate? = null
    lateinit var error:SignInErrors
    var isFingerPrintEnable:Boolean =false
    val isUsernameEmpty = MutableLiveData<Boolean>(false)


    init {
        userLiveData = UserLiveUpdate()
        error= SignInErrors(null)
        pref = IlafSharedPreference(application)
        isFingerPrintEnable = !pref.getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL).isNullOrEmpty()
        if(!pref.getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL).isNullOrEmpty()){
            username.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL))
            if(username.value?.isNullOrEmpty()?:false){
                isUsernameEmpty.postValue(true)
            }
        }else if(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL).isNullOrBlank()){
            isUsernameEmpty.postValue(true)
        }
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals( IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun callLogin(error: SignInErrors) {
        userLiveData?.buttonClicked()
        var userDetails = UserDetails()
        userDetails.email=username.value?.trim()
        userDetails.password=password.value?.trim()

        if(IlafValidator.isNullOrEmpty(error.userNameError)
            && IlafValidator.isNullOrEmpty(error.userPasswordError)){
            error.uiUpdate = true
           tryLogin(userDetails, error)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun tryLogin(userDetails: UserDetails, errorData: SignInErrors) {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), false)
        val subscribe =
            loginService?.doSignIn(userDetails.email!!, userDetails.password!!)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if(it.code()==200) {
                            errorData.uiUpdate = true
                            IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, it.body()?.accessToken)
                            IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL, userDetails.email)
                            Utility.saveUserPin(userDetails.password!!,getApplication())
                            userLiveData?.userLoginSuccess()
                        }else{
                            errorData.uiUpdate = false
                            userLiveData?.userLoginFailed()
                        }
                    } else {
                        if(it.code()==400) {
                            userLiveData?.userLoginFailed()
                        }
                        errorData.uiUpdate = false
                    } // this will tell you why your api doesnt work most of time

                }, { error ->
                    errorData.uiUpdate = false
                    userLiveData?.userLoginFailed()
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()

                })
    }

    fun isUserExist() {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), false)
        var req = RegisterUserDetails()
        req.email = username.value
        val subscribe =
                loginService?.isUserExist(req)?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.body()?.isSuccess!!) {
                                userLiveData?.userExistSuccess(it.body()?.messageStatus,it.body()?.isSuccess?:false)
                            }else if(!it.body()?.isSuccess!!){
                                userLiveData?.userExistSuccess(it.body()?.messageStatus,it.body()?.isSuccess?:false)
                            } else {
                                userLiveData?.userExistFailed()
                            } // this will tell you why your api doesnt work most of time

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, null))
                            error.printStackTrace()

                        })
    }


    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(username) {
                value= it.isNotEmpty() && password.value?.isNotEmpty()?:false
        }
        addSource(password) {
            value= it.isNotEmpty() && username.value?.isNotEmpty()?:false
        }

    }

}