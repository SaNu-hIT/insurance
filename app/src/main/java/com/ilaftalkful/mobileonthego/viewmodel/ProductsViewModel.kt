package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.IlafApplication.Companion.context
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.model.AppSettingsResponse
import com.ilaftalkful.mobileonthego.model.DashBoardResponse
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ProductsViewModel(application: Application) : IlafBaseViewModel(application) {

    var isValidUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var data: MutableLiveData<DashBoardResponse> = MutableLiveData<DashBoardResponse>()
    var toggleView: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var ilafPreference: IlafSharedPreference
    val dashBoardResponse = MutableLiveData<DashBoardResponse>()
    internal var userLiveData: UserLiveUpdate? = null
    var isFgaActive: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isHealthActive: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isTravelActive: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isMarineActive: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var civiliId: MutableLiveData<String> = MutableLiveData<String>("")
    init {
        ilafPreference = IlafSharedPreference(application)
        userLiveData = UserLiveUpdate()
        isValidUser.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null &&
                pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
    }


    fun getDashBoardData() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.getDashBoardDetails()?.observeOn(
                    AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                        Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            dashBoardResponse.postValue(it.body()?.Data)
                            processOnResponse(it.body()?.Data)
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))

                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                            ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                            userLiveData?.sessionExpired()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }

    private fun processOnResponse(body: DashBoardResponse?) {
        ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.USER_NAME, body?.userDetails?.nameFirst)
        ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.USER_CIVIL_ID, body?.userDetails?.civilID)

        pref.setStringPrefValue(IlafSharedPreference.Constants.USER_EMAIL, body?.userDetails?.emailID)
        pref.setStringPrefValue(IlafSharedPreference.Constants.USER_DOB, body?.userDetails?.dOB)
        pref.setStringPrefValue(IlafSharedPreference.Constants.USER_MOBILE_NUMBER, body?.userDetails?.mobileNumber)
        pref.setStringPrefValue(IlafSharedPreference.Constants.USER_NAME, body?.userDetails?.nameFirst)
        pref.setStringPrefValue(IlafSharedPreference.Constants.MAX_AGE, body?.userDetails?.maxAge)
        val gender =context.getString(R.string.male)
        civiliId.postValue(body?.userDetails?.civilID)
        if(body?.userDetails?.gender.toString().toLowerCase().equals(gender.toLowerCase()))
        pref.setStringPrefValue(IlafSharedPreference.Constants.IS_MALE, "1")
        else{
            pref.setStringPrefValue(IlafSharedPreference.Constants.IS_MALE, "2")
        }
        pref.setIntegerPrefValue(IlafSharedPreference.Constants.USER_ID, body?.userDetails?.userID)


        for(item in body?.dashboardModules!!){
            when(item.moduleName){
                Constants.HEALTH -> {
                    isHealthActive.postValue(true)
                }
                Constants.MARINE -> {
                    isMarineActive.postValue(true)
                }
                Constants.FGA -> {
                    isFgaActive.postValue(true)
                }
                Constants.TRAVEL -> {
                    isTravelActive.postValue(true)
                }
            }
        }

    }
    fun getAppSettings() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
                dashBoardService?.getAppSettings()?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                if (it.body()?.isSuccess!!) {
                                    processData(it.body()?.Data)
                                    userLiveData?.appSettingsSuccess()
                                } else {
                                    userLiveData?.appSettingsFailed()
                                }
                            } else {
                                if (it.code() == Constants.UNAUTH_ERROR) {
                                    IlafSharedPreference(getApplication()).setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                    IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                    userLiveData?.sessionExpired()
                                }
                            }

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, null))
                            error.printStackTrace()
                        })    }

    private fun processData(data: ArrayList<AppSettingsResponse>?) {
        if(!data.isNullOrEmpty()){
            for(i in data){
                if(i.moduleName.toString().equals("Travel")){
                    if(i.appSettingsName.toString().equals("TC")){
                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.TRAVEL_TC,i.appSettingsValue)
                    }else if(i.appSettingsName.toString().equals("Max Start Days")){
                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.TRAVEL_MAX_START_DAYS,i.appSettingsValue)
                    }
                }else if(i.moduleName.toString().equals("Motor")){
                    if(i.appSettingsName.toString().equals("TC")){
                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.MOTOR_TC,i.appSettingsValue)
                    }
                } else if(i.moduleName.toString().equals("Health")){
                    if(i.appSettingsName.toString().equals("TC")){
                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.HEALTH_TC,i.appSettingsValue)
                    }
                }else if(i.moduleName.toString().equals("Marine")){
                    if(i.appSettingsName.toString().equals("TC")){
                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.MARINE_TC,i.appSettingsValue)
                    }
                }else if(i.moduleName.toString().equals("FGA")){
                    if(i.appSettingsName.toString().equals("TC")){
                        IlafSharedPreference(getApplication()).setStringPrefValue(IlafSharedPreference.Constants.FGA_TC,i.appSettingsValue)
                    }
                }
            }
        }
    }


}