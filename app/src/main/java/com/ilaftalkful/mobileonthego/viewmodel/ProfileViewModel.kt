package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.BuildConfig
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileViewModel (application: Application) : IlafBaseViewModel(application) {


    var isValidUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
     var ilafPreference: IlafSharedPreference
    var userName: MutableLiveData<String> = MutableLiveData<String>("")
    var civilId: MutableLiveData<String> = MutableLiveData<String>("")
    internal var userLiveData: UserLiveUpdate? = null
    var version = MutableLiveData<String>()
    var isChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isStateChanged:Boolean=false


    init {
        userLiveData = UserLiveUpdate()
        ilafPreference = IlafSharedPreference(application)
        isValidUser.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))
        userName.postValue(ilafPreference.getStringPrefValue(IlafSharedPreference.Constants.USER_NAME))
        civilId.postValue(ilafPreference.getStringPrefValue(IlafSharedPreference.Constants.USER_CIVIL_ID))
        isENS = MutableLiveData<Boolean>(false)
        version.postValue(BuildConfig.VERSION_NAME)
        isChecked.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_FINGERPRINT))
    }
        fun callLogout() {
            userLiveData?.processing()
            var dashBoardService = UserService.create(getApplication<Application>(), true)
            val subscribe =
                dashBoardService?.Logout()?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                    ?.subscribeOn(
                        Schedulers.io()
                    )
                    ?.subscribe({
                        if (it.isSuccessful) {
                            if (it.body()?.isSuccess!!) {
                                ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.logOutSuccess()
                            } else {

                                    userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))

                            }
                        } else {
                            if (it.code() == Constants.UNAUTH_ERROR) {
                                ilafPreference.setBooleanPrefValue(
                                    IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                    false
                                )
                                ilafPreference.setStringPrefValue(
                                    IlafSharedPreference.Constants.TOKEN_KEY,
                                    null
                                )
                                userLiveData?.sessionExpired()
                            }
                        }

                    }, { error ->
                        userLiveData?.postError(ErrorData(100, null))
                    })
        }

    fun setFingerPrintEnable(it: Boolean?) {
        isStateChanged =it?:false
       ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_FINGERPRINT,it?:false)
    }

}
