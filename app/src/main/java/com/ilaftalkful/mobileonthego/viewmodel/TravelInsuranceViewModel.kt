package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.buyinsurance.TravelPlansAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TravelInsuranceViewModel(application: Application) : IlafBaseViewModel(application) {
    var isValidUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    internal var userLiveData: UserLiveUpdate? = null
    val data = MutableLiveData<ArrayList<TravelPlansResponse>>()
    var isDataEmpty = MutableLiveData<Boolean>(false)
    var   travelPlansAdapter: TravelPlansAdapter?=null


    init {
        userLiveData = UserLiveUpdate()
        pref = IlafSharedPreference(application)
        isValidUser.postValue(pref.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))


    }


     fun setAdapter() {
        if (travelPlansAdapter == null) {
            travelPlansAdapter = TravelPlansAdapter()
        } else {
            travelPlansAdapter?.notifyDataSetChanged()
        }
    }

    fun setData(){
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
                dashBoardService?.getTravelPlans()?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                                if(it.body()?.isSuccess!!) {
                                    data.postValue(it.body()?.Data)
                                    isDataEmpty.postValue(true)
                                    userLiveData?.responseSuccess()
                                } else {
                                    userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                                }
                                if(it.code()== Constants.UNAUTH_ERROR){
                                    pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                    pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                    userLiveData?.sessionExpired()
                                }
                             // this will tell you why your api doesnt work most of time

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, null))
                            error.printStackTrace()
                        })


    }

}