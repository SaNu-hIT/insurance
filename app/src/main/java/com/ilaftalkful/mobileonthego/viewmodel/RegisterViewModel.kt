package com.ilaftalkful.mobileonthego.viewmodel
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.UserRegistrationErrors
import com.ilaftalkful.mobileonthego.model.regster.RegisterUserDetails
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.crypto.Cipher
class RegisterViewModel(application: Application) : IlafBaseViewModel(application) {
    lateinit var error: UserRegistrationErrors
    internal var userLiveData: UserLiveUpdate? = null
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confPassword = MutableLiveData<String>()
    val phonenumber = MutableLiveData<String>()
    val civilid = MutableLiveData<String>()
    val dd = MutableLiveData<String>()
    var isMaleChecked = MutableLiveData<String>()
    lateinit var cipher: Cipher
    var isChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isStateChanged: Boolean = false

    init {
        userLiveData = UserLiveUpdate()
        error = UserRegistrationErrors(null)
        pref = IlafSharedPreference(application)
        if (pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY) == null
            && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)
        ) {
            isENS.postValue(false)
        } else {
            isENS.postValue(true)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun callRegistration(error: UserRegistrationErrors) {

        var userDetails = RegisterUserDetails()
        userDetails.email = email.value?.trim()
        userDetails.password = password.value?.trim()
        userDetails.name = name.value?.trim()
        userDetails.mobilenumber = phonenumber.value?.trim()
        userDetails.gender = isMaleChecked.value?.trim()
        userDetails.civilId = civilid.value?.trim()
        userDetails.dob = DateUtil.getDateTorequestFromat(dd.value)


        if (IlafValidator.isNullOrEmpty(error.nameError)
            && IlafValidator.isNullOrEmpty(error.userEmailError)
            && IlafValidator.isNullOrEmpty(
                error.phoneNumberError
            ) && IlafValidator.isNullOrEmpty(
                error.civilidError
            ) && IlafValidator.isNullOrEmpty(error.civilidError)
            && IlafValidator.isNullOrEmpty(error.genderError) && IlafValidator.isNullOrEmpty(error.passwordError) && IlafValidator.isNullOrEmpty(
                error.confirmError
            )
        ) {
            error.uiUpdate = true
            tryRegistration(userDetails, error)
        }
    }

    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(name) {
            value = it.isNotEmpty() && email.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false && civilid.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
        }
        addSource(email) {
            value = it.isNotEmpty() && name.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false && civilid.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
        }

        addSource(phonenumber) {
            value = it.isNotEmpty() && name.value?.isNotEmpty() ?: false
                    && email.value?.isNotEmpty() ?: false && civilid.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
        }
        addSource(civilid) {
            value = it.isNotEmpty() && email.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
        }
        addSource(password) {
            value = it.isNotEmpty() && email.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && civilid.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
        }
        addSource(confPassword) {
            value = it.isNotEmpty() && email.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && civilid.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
        }
        addSource(dd) {
            value = it.isNotEmpty() && email.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false && civilid.value?.isNotEmpty() ?: false
                    && name.value?.isNotEmpty() ?: false
                    && isMaleChecked.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
        }


        addSource(isMaleChecked) {
            value = it.isNotEmpty() && email.value?.isNotEmpty() ?: false
                    && phonenumber.value?.isNotEmpty() ?: false
                    && civilid.value?.isNotEmpty() ?: false
                    && dd.value?.isNotEmpty() ?: false
                    && password.value?.isNotEmpty() ?: false
                    && confPassword.value?.isNotEmpty() ?: false
                    && name.value?.isNotEmpty() ?: false

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun tryRegistration(userDetails: RegisterUserDetails, errorData: UserRegistrationErrors) {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), false)
        val subscribe =
            loginService?.doRegisterIn(userDetails)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            errorData.uiUpdate = true
                            pref.setStringPrefValue(
                                IlafSharedPreference.Constants.USER_EMAIL,
                                userDetails.email
                            )
                            pref.setStringPrefValue(
                                IlafSharedPreference.Constants.USER_DOB,
                                userDetails.dob
                            )
                            pref.setStringPrefValue(
                                IlafSharedPreference.Constants.USER_PHONE_CODE,
                                userDetails.email
                            )
                            pref.setStringPrefValue(
                                IlafSharedPreference.Constants.USER_MOBILE_NUMBER,
                                userDetails.mobilenumber
                            )
                            pref.setStringPrefValue(
                                IlafSharedPreference.Constants.USER_NAME,
                                userDetails.name
                            )
                            pref.setStringPrefValue(
                                IlafSharedPreference.Constants.IS_MALE,
                                userDetails.gender
                            )
                            Utility.saveUserPin(password.value!!, getApplication())
                            userLiveData?.userRegistrationSuccess(it?.body()?.messageStatus)
                        } else {
                            if (it.code() == Constants.UNAUTH_ERROR) {
                                error.uiUpdate = false
                                pref.setBooleanPrefValue(
                                    IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                    false
                                )
                                pref.setStringPrefValue(
                                    IlafSharedPreference.Constants.TOKEN_KEY,
                                    null
                                )
                                userLiveData?.sessionExpired()
                            } else {
                                error.uiUpdate = false
                                userLiveData?.postError(ErrorData(it.code(), it?.body()?.messageStatus))
                            }
                        }
                    } else {
                        error.uiUpdate = false
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            error.uiUpdate = false
                            pref.setBooleanPrefValue(
                                    IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                    false
                            )
                            pref.setStringPrefValue(
                                    IlafSharedPreference.Constants.TOKEN_KEY,
                                    null
                            )
                            userLiveData?.sessionExpired()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it?.body()?.messageStatus))
                        }
                        errorData.uiUpdate = false
                    }
                    // this will tell you why your api doesn't work most of time

                }, { error ->
                    errorData.uiUpdate = false
                    userLiveData?.postError(
                        ErrorData(100, null)
                    )
                    error.printStackTrace()

                })
    }

    fun setFingerPrintEnable(it: Boolean?) {
        isStateChanged = it ?: false
        pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_FINGERPRINT, it ?: false)
    }
}