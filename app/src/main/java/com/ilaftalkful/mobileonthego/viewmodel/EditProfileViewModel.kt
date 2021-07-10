package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.UserRegistrationErrors
import com.ilaftalkful.mobileonthego.model.regster.RegisterUserDetails
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditProfileViewModel(application: Application) : IlafBaseViewModel(application) {
    lateinit var error: UserRegistrationErrors
    internal var userLiveData: UserLiveUpdate? = null
    val name = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val phonenumber = MutableLiveData<String>("")
    val civilid = MutableLiveData<String>("")
    val dob = MutableLiveData<String>("")
    var isMaleChecked = MutableLiveData<String>("0")
    var isMale = MutableLiveData<Boolean>(false)

    init {
        userLiveData = UserLiveUpdate()
        error = UserRegistrationErrors(null)
        pref = IlafSharedPreference(application)
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null
            && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
        name.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_NAME))
        phonenumber.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_MOBILE_NUMBER))
        civilid.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_CIVIL_ID))
        dob.postValue(DateUtil.getStringDateToFromat(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_DOB)))
        email.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL))
        var data= pref.getStringPrefValue(IlafSharedPreference.Constants.IS_MALE)
        isMaleChecked.value=data
        if(isMaleChecked.value.equals("1")){
            isMale.postValue(true)
        }
    }

    fun callRegistration(error: UserRegistrationErrors) {
          var userDetails = RegisterUserDetails()
        userDetails.password = ""
        userDetails.name = name.value
        userDetails.mobilenumber = phonenumber.value
        userDetails.userId = pref.getIntegerPrefValue(IlafSharedPreference.Constants.USER_ID).toString()


        if (IlafValidator.isNullOrEmpty(error.nameError) &&IlafValidator.isNullOrEmpty(error.phoneNumberError)  ) {
            error.uiUpdate = true
            tryRegistration(userDetails, error)
        }
    }

    private fun tryRegistration(userDetails: RegisterUserDetails, errorData: UserRegistrationErrors) {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            loginService?.doRegisterIn(userDetails)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful){
                        if(it.body()?.isSuccess!!){
                            errorData.uiUpdate = true
                            pref.setStringPrefValue(IlafSharedPreference.Constants.USER_MOBILE_NUMBER,userDetails.mobilenumber)
                            pref.setStringPrefValue(IlafSharedPreference.Constants.USER_NAME,userDetails.name)
                            userLiveData?.responseSuccess()
                        }else{
                            errorData.uiUpdate = false
                            userLiveData?.postError(ErrorData(100, it?.body()?.messageStatus))
                        }
                    }else if (it.code() == Constants.UNAUTH_ERROR) {
                        pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                        pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                        userLiveData?.sessionExpired()
                    }else {
                        errorData.uiUpdate = false
                        userLiveData?.postError(ErrorData(100, it.message()))
                    } // this will tell you why your api doesnt work most of time

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