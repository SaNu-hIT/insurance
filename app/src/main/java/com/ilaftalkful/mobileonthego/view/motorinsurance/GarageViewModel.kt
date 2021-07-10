package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.GarageResponse
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GarageViewModel(application: Application) : IlafBaseViewModel(application) {
    lateinit var userLiveData: UserLiveUpdate
    var garageRespone :MutableLiveData<GarageResponse> = MutableLiveData<GarageResponse>()
    var searchKey :MutableLiveData<String> = MutableLiveData<String>()
    var option1 :MutableLiveData<String> = MutableLiveData<String>()
    var option2 :MutableLiveData<String> = MutableLiveData<String>()
    var option3 :MutableLiveData<String> = MutableLiveData<String>()
    var phoneNoText =  MutableLiveData<String>("")

    fun getGarageList() {
        userLiveData.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.GetGarages()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData.responseSuccess()
                            garageRespone.postValue(it?.body()?.Data)
                        } else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        }
                    } else {

                            userLiveData.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    userLiveData.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }


    lateinit var ilafPreference :IlafSharedPreference
    var selectedOption :MutableLiveData<Int> =MutableLiveData<Int>(0)

    init {
        ilafPreference = IlafSharedPreference(application)
        userLiveData = UserLiveUpdate()
        isENS = MutableLiveData<Boolean>(false)
        if(ilafPreference.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null
            && ilafPreference.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals("AR")){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
    }

}