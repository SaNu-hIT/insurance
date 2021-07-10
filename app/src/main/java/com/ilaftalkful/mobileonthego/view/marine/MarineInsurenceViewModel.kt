package com.ilaftalkful.mobileonthego.view.marine

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.health.HelthErros
import com.ilaftalkful.mobileonthego.model.marine.MarineFgaParameter
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MarineInsurenceViewModel(application: Application) : IlafBaseViewModel(application) {
    val message = MutableLiveData<String>()

    var ContactPersonName = MutableLiveData<String>("")
    var CompanyName = MutableLiveData<String>("")
    var ContactPersonEmail = MutableLiveData<String>("")
    var ContactPersonMobile = MutableLiveData<String>("")
    var MessageDetail = MutableLiveData<String>("")
    internal var userLiveData: UserLiveUpdate? = null
    var ilafPreference: IlafSharedPreference
    init {
        ilafPreference = IlafSharedPreference(application)
        userLiveData = UserLiveUpdate()
    }


    fun submitQoute(errors: HelthErros,isFromFga:Boolean) {

        if (IlafValidator.isNullOrEmpty(errors.contactPersonNameError)
            && IlafValidator.isNullOrEmpty(errors.companyNameError) && IlafValidator.isNullOrEmpty(
                errors.contactPersonEmailError
            )
            && IlafValidator.isNullOrEmpty(errors.contactPersonMobileError)
            && IlafValidator.isNullOrEmpty(errors.departmentIDError)
            && IlafValidator.isNullOrEmpty(errors.messageDetailError)
        ) {
            errors.uiUpdate = true
            var qoute = MarineFgaParameter()
            qoute.contactPersonName = ContactPersonName.value
            qoute.companyName = CompanyName.value
            qoute.contactPersonEmail = ContactPersonEmail.value
            qoute.contactPersonMobile = ContactPersonMobile.value
            qoute.messageDetail = MessageDetail.value
            userLiveData?.processing()
            var corperateService = UserService.create(getApplication<Application>(), true)



            if (isFromFga)
            {
                val subscribe =
                    corperateService?.postFgaQoute(qoute)?.observeOn(
                        AndroidSchedulers.mainThread()
                    )
                        ?.subscribeOn(
                            Schedulers.io()
                        )
                        ?.subscribe({
                                if (it.body()?.isSuccess!!) {
                                    errors.uiUpdate = false
                                    userLiveData?.responseSuccess()
                                    userLiveData?.CorporatteQouteSuccess(it.body()?.message)

                                }else {
                                    if(it.code()== Constants.UNAUTH_ERROR){
                                        pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                        pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                        userLiveData?.sessionExpired()
                                    }else {
                                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                                    }
                                }
                        }, { error ->
                            userLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                            error.printStackTrace()
                        })

            }else{
                val subscribe =
                    corperateService?.postMarineQoute(qoute)?.observeOn(
                        AndroidSchedulers.mainThread()
                    )
                        ?.subscribeOn(
                            Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                if (it.body()?.isSuccess!!) {
                                    errors.uiUpdate = false
                                    userLiveData?.responseSuccess()
                                    userLiveData?.CorporatteQouteSuccess(it.body()?.message)

                                } else {
                                    errors.uiUpdate = false
                                    userLiveData?.postError(ErrorData(it.code(), it.message()))
                                }
                            }else {
                                if(it.code()== Constants.UNAUTH_ERROR){
                                    ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                    ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                    userLiveData?.sessionExpired()
                                }else {
                                    userLiveData?.postError(ErrorData(it.code(), it.message()))
                                }
                            }

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                            error.printStackTrace()
                        })
            }
        }
    }


    fun clearAll() {
        ContactPersonName.value = ""
        CompanyName.value = ""
        ContactPersonEmail.value = ""
        ContactPersonMobile.value = ""
        MessageDetail.value = ""
    }


    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(CompanyName) {
            value = it.toString().isNotEmpty() && ContactPersonName.value.toString()
                .isNotEmpty()
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty()
                    && MessageDetail.value.toString().isNotEmpty()
        }
        addSource(ContactPersonName) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty()
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty()
                    && MessageDetail.value.toString().isNotEmpty()
        }
        addSource(ContactPersonEmail) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty()
            ContactPersonName.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty()
                    && MessageDetail.value.toString().isNotEmpty()
        }
        addSource(ContactPersonMobile) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty()
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonName.value.toString()
                .isNotEmpty()
                    && MessageDetail.value.toString().isNotEmpty()
        }

        addSource(MessageDetail) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty()
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty()
                    && ContactPersonName.value.toString().isNotEmpty()
        }

    }


    fun getFgaInsurenceText() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.GetFGAInsuranceDescriptions()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            message.postValue(it.body()?.data)
                        }
                    } else {
                        if(it.code()== Constants.UNAUTH_ERROR){
                            ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                            ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                            userLiveData?.sessionExpired()
                        }else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    userLiveData?.logOutFailed()
                    error.printStackTrace()
                })
    }

    fun getMarineInsurenceText() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.GetMarineInsuranceDescriptions()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            message.postValue(it.body()?.data)
                        } else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(),it.body()?.message))
                            }
                        }
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                })
    }

    fun getHeadertext(fromFga: Boolean) {
        if (fromFga) {
            getFgaInsurenceText()
        } else {
            getMarineInsurenceText()
        }

    }

}