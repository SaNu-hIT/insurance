package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.log.ModuleDetail
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyLogsViewModel (application: Application) : IlafBaseViewModel(application){
    var isValidUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    lateinit var ilafPreference: IlafSharedPreference
    lateinit var userLiveData: UserLiveUpdate
    val logRespone= MutableLiveData<ArrayList<ModuleDetail>>()
    var isMyLogEmpty:MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var pdfUrl: MutableLiveData<String> = MutableLiveData<String>()
    init {
        ilafPreference = IlafSharedPreference(application)
        isValidUser.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))
        ilafPreference= IlafSharedPreference(application)
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null &&
                pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
        userLiveData = UserLiveUpdate()
    }


    fun getLogList() {
        userLiveData.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.GetLog()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            if(it.body()?.Data.isNullOrEmpty()){
                                isMyLogEmpty.postValue(true)
                            }
                            logRespone.postValue(it?.body()?.Data)
                            userLiveData.responseSuccess()
                        }  else {

                            userLiveData?.postError(ErrorData(it.code(),  it.body()?.messageStatus))
                            isMyLogEmpty.postValue(true)
                        }
                    } else {
                        if(it.code()== Constants.UNAUTH_ERROR){
                            ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                            ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                            userLiveData.sessionExpired()
                        }
                    }

                }, { error ->
                    isMyLogEmpty.postValue(true)
                    userLiveData.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }

    fun getPdfToDownload(policyID: String?) {
        userLiveData.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
                dashBoardService?.getTravelPDF(policyID)?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                                if (it.body()?.isSuccess!!) {
                                    if(it.body()?.Data?.fileURL?.isNotBlank()?:false){
                                        pdfUrl.postValue(it.body()?.Data?.fileURL)
                                        userLiveData.pdfdownloadSuccess()
                                    }
                                }  else {
                                    userLiveData.pdfDownloadFailed(it.body()?.messageStatus)
                                }
                                if(it.code()== Constants.UNAUTH_ERROR) {
                                    ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                    ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                    userLiveData.sessionExpired()
                                }

                        }, { error ->
                            isMyLogEmpty.postValue(true)
                            userLiveData.postError(ErrorData(100, null))
                            error.printStackTrace()
                        })
    }


}