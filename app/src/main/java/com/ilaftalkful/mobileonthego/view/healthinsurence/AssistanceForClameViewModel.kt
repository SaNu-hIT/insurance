package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.health.claimassistance.Datum
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AssistanceForClameViewModel(application: Application) : IlafBaseViewModel(application) {
    val assisitanceDetails = MutableLiveData<ArrayList<Datum>>()
    internal var userLiveData: UserLiveUpdate? = null

    init {
        userLiveData = UserLiveUpdate()
        pref = IlafSharedPreference(application)
    }

    fun getAssistanceForClime() {
        userLiveData?.processing()
        var corperateService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            corperateService?.getHealthClaimAssistance()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            assisitanceDetails.postValue(it.body()?.Data)
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            pref.setBooleanPrefValue(
                                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                false
                            )
                            pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                            userLiveData?.sessionExpired()
                        }
                    }
                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }


}