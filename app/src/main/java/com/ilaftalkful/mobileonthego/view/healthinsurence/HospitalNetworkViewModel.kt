package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.health.hospital.Data
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HospitalNetworkViewModel(application: Application) : IlafBaseViewModel(application) {
    val regionName: ObservableArrayList<String> = ObservableArrayList<String>()
    val regionIDs: ObservableArrayList<Int> = ObservableArrayList<Int>()
    val regionSeletId = MutableLiveData<Int>()
    var searchKey :MutableLiveData<String> = MutableLiveData<String>()
    val hospitalResponse = MutableLiveData<Data>()
    internal var userLiveData: UserLiveUpdate? = null
    var region = MutableLiveData<String>()
    var hospName = MutableLiveData<String>()

    init {
        userLiveData = UserLiveUpdate()
    }

    fun getHospitalNetworks() {
        userLiveData?.processing()
        var corperateService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            corperateService?.GetHospitalNetworks()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({


                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            hospitalResponse.postValue(it.body()?.Data)
                            processResponce(it.body()?.Data)
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

    private fun processResponce(data: Data?) {
        var depatmentName = ArrayList<String>()
        var depatmenId = ArrayList<Int>()
        regionName.clear()
        regionIDs.clear()


        regionName.add("All")
        regionIDs.add(0)
        if (data != null) {
            for (item in data.hospitalRegions) {
                var name = String()
                var id: Int
                name = item.text.toString()
                id = item.id!!.toInt()
                depatmentName.add(name)
                regionIDs.add(id)
            }
        }
        regionName.addAll(depatmentName)
        regionIDs.addAll(depatmenId)
    }
}