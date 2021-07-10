package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.SignInErrors
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResetPasswordViewModel(application: Application) : IlafBaseViewModel(application) {

    internal var userLiveData: UserLiveUpdate? = null
    lateinit var error:SignInErrors
    val username = MutableLiveData<String>("")

    fun resetPassword(errors: SignInErrors) {
        userLiveData?.buttonClicked()

        if(IlafValidator.isNullOrEmpty(error.userNameError)){
            error.uiUpdate = true
            resetPassword(username.value!!.trim(), error)
        }
    }

    private fun resetPassword(userName: String, errorData: SignInErrors) {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), false)
        val subscribe =
                loginService?.resetPassword(userName)?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it?.body()?.isSuccess!!) {
                                errorData.uiUpdate = true
                                    userLiveData?.resetPasswordSuccess(it?.body()?.messageStatus)
                            }else{
                                errorData.uiUpdate = false
                                    userLiveData?.postError(ErrorData(it.code(), it?.body()?.messageStatus))

                            }
                            }, { error ->
                            errorData.uiUpdate = false
                            userLiveData?.postError(ErrorData(100,null))
                            error.printStackTrace()

                        })
    }


    init {
        userLiveData = UserLiveUpdate()
        error= SignInErrors(null)
        pref =  IlafSharedPreference(application)
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null &&
                pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
    }
}