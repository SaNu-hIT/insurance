package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference.Constants.Companion.LANGUAGE_RESET_TOKEN
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChangePasswordViewModel(application: Application) : IlafBaseViewModel(application) {
    var password = MutableLiveData<String>()
    var confPassowrd = MutableLiveData<String>()
    internal var userLiveData: UserLiveUpdate? = null
    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(password) {
            value= it.isNotEmpty() && password.value?.isNotEmpty()?:false
        }
        addSource(confPassowrd) {
            value= it.isNotEmpty() && password.value?.isNotEmpty()?:false
        }

    }

    init {
        userLiveData = UserLiveUpdate()
        pref = IlafSharedPreference(application)
        if (pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY) == null && pref.getStringPrefValue(
                IlafSharedPreference.Constants.LANGUAGE_KEY
            ).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)
        ) {
            isENS.postValue(false)
        } else {
            isENS.postValue(true)
        }
    }

    fun callChangePassword(
        errors: UserRegistrationErrors,
        fromReset: Boolean?
    ) {
         if (IlafValidator.isNullOrEmpty(errors.passwordError) && IlafValidator.isNullOrEmpty(errors.confirmError)) {
            errors.uiUpdate = true
            changePassword(password.value?.trim().toString(), errors, fromReset)

        }
    }

    private fun changePassword(
        password: String,
        errorData: UserRegistrationErrors,
        fromReset: Boolean?
    ) {
        userLiveData?.processing()
        var param = PasswordParameter()
        var resetparam = ResetParameter()

        if (fromReset!!) {
            resetparam.setPassword(password)
            var token = pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_RESET_TOKEN)
            if (token != null && !TextUtils.isEmpty(token)) {
                resetparam.setResetToken(token)
            }
        } else {
            param.setPassword(password)
        }
        var loginService = UserService.create(getApplication<Application>(), true)
        var resetService = UserService.create(getApplication<Application>(), false)
        if (fromReset) {
            val subscribe =
                resetService?.changePassword(resetparam)?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                    ?.subscribeOn(
                        Schedulers.io()
                    )
                    ?.subscribe({
                        if (it?.body()?.isSuccess!!) {
                            errorData.uiUpdate = true
                            userLiveData?.resetPasswordSuccess(it?.body()?.messageStatus)
                        } else if (it.code() == Constants.UNAUTH_ERROR) {
                            pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                            pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                            userLiveData?.sessionExpired()
                        }else {
                            errorData.uiUpdate = false
                            userLiveData?.postError(ErrorData(it.code(), it?.body()?.messageStatus))

                        }
                    }, { error ->
                        errorData.uiUpdate = false
                        userLiveData?.postError(
                            ErrorData(
                                500,
                                error.message ?: "Something went wrong"
                            )
                        )
                        error.printStackTrace()

                    })
        } else {
            val subscribe =
                loginService?.updatePassword(param)?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                    ?.subscribeOn(
                        Schedulers.io()
                    )
                    ?.subscribe({
                        if (it?.body()?.isSuccess!!) {
                            errorData.uiUpdate = true
                            userLiveData?.chanegePasswordSuccess(it?.body()?.messageStatus)
                        } else {
                            errorData.uiUpdate = false
                            userLiveData?.postError(ErrorData(it.code(), it?.body()?.messageStatus))

                        }
                    }, { error ->
                        errorData.uiUpdate = false
                        userLiveData?.postError(
                            ErrorData(
                                100,
                                null
                            )
                        )
                        error.printStackTrace()

                    })

        }


    }

    fun clearToken() {
      pref.setStringPrefValue(LANGUAGE_RESET_TOKEN,"")
    }

}