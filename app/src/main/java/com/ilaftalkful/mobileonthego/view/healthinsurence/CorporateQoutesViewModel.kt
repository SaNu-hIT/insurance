package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.health.Department
import com.ilaftalkful.mobileonthego.model.health.HealthCareCorporateQuoteParameter
import com.ilaftalkful.mobileonthego.model.health.HelthErros
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CorporateQoutesViewModel(application: Application) : IlafBaseViewModel(application) {
    val departmentName: ObservableArrayList<String> = ObservableArrayList<String>()
    val departmentIDs: ObservableArrayList<Int> = ObservableArrayList<Int>()
    val depatSeletId = MutableLiveData<Int>()
    val department = MutableLiveData<ArrayList<Department>>()

    var ContactPersonName = MutableLiveData<String>("")
    var CompanyName = MutableLiveData<String>("")
    var ContactPersonEmail = MutableLiveData<String>("")
    var ContactPersonMobile = MutableLiveData<String>("")
    var DepartmentID = MutableLiveData<String>("")
    var MessageDetail = MutableLiveData<String>("")

    internal var userLiveData: UserLiveUpdate? = null

    init {
        userLiveData = UserLiveUpdate()
    }

    fun getDeprtments() {
        userLiveData?.processing()
        var corperateService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            corperateService?.getDepartmentsDropdownList()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            processResult(it.body()?.Data)
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


    fun submitQoute(errors: HelthErros) {


        if (IlafValidator.isNullOrEmpty(errors.contactPersonNameError)
            && IlafValidator.isNullOrEmpty(errors.companyNameError) && IlafValidator.isNullOrEmpty(
                errors.contactPersonEmailError
            )
            && IlafValidator.isNullOrEmpty(errors.contactPersonMobileError)
            && IlafValidator.isNullOrEmpty(errors.departmentIDError)
            && IlafValidator.isNullOrEmpty(errors.messageDetailError)
        ) {
            errors.uiUpdate = true
            var qoute: HealthCareCorporateQuoteParameter = HealthCareCorporateQuoteParameter()
            qoute.contactPersonName = ContactPersonName.value
            qoute.companyName = CompanyName.value
            qoute.contactPersonEmail = ContactPersonEmail.value
            qoute.departmentID = departmentIDs[depatSeletId.value!!].toString()
            qoute.contactPersonMobile = ContactPersonMobile.value
            qoute.messageDetail = MessageDetail.value
            userLiveData?.processing()
            var corperateService = UserService.create(getApplication<Application>(), true)
            val subscribe =
                corperateService?.HealthCareCorporateQuote(qoute)?.observeOn(
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
                                userLiveData?.CorporatteQouteSuccess(it.body()?.messageStatus)

                            } else {
                                userLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        } else {
                            if (it.code() == Constants.UNAUTH_ERROR) {
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
                                userLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        }
                    }, { error ->
                        userLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                        error.printStackTrace()
                    })

        }


    }


    private fun processResult(data: ArrayList<Department>?) {
        var depatmentName = ArrayList<String>()
        var depatmenId = ArrayList<Int>()
        departmentName.clear()
        departmentIDs.clear()
        if (data != null) {
            for (item in data) {
                var year = String()
                var id: Int
                year = item.text.toString()
                id = item.id!!.toInt()
                depatmentName.add(year)
                departmentIDs.add(id)
            }
        }
        departmentName.addAll(depatmentName)
        departmentIDs.addAll(depatmenId)
    }

    fun clearAll() {
        ContactPersonName.value = ""
        CompanyName.value = ""
        ContactPersonEmail.value = ""
        ContactPersonMobile.value = ""
        depatSeletId.value = 0
        MessageDetail.value = ""
    }


    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(CompanyName) {
            value = it.toString().isNotEmpty() && ContactPersonName.value.toString()
                .isNotEmpty() ?: false
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty() ?: false
            DepartmentID.value.toString().isNotEmpty() && MessageDetail.value.toString()
                .isNotEmpty() ?: false
        }
        addSource(ContactPersonName) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty() ?: false
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty() ?: false
            DepartmentID.value.toString().isNotEmpty() && MessageDetail.value.toString()
                .isNotEmpty() ?: false
        }
        addSource(ContactPersonEmail) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty() ?: false
            ContactPersonName.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty() ?: false
            DepartmentID.value.toString().isNotEmpty() && MessageDetail.value.toString()
                .isNotEmpty() ?: false
        }
        addSource(ContactPersonMobile) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty() ?: false
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonName.value.toString()
                .isNotEmpty() ?: false
            DepartmentID.value.toString().isNotEmpty() && MessageDetail.value.toString()
                .isNotEmpty() ?: false
        }
        addSource(DepartmentID) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty() ?: false
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonName.value.toString()
                .isNotEmpty() ?: false
            ContactPersonMobile.value.toString().isNotEmpty() && MessageDetail.value.toString()
                .isNotEmpty() ?: false
        }
        addSource(MessageDetail) {
            value = it.toString().isNotEmpty() && CompanyName.value.toString().isNotEmpty() ?: false
            ContactPersonEmail.value.toString().isNotEmpty() && ContactPersonMobile.value.toString()
                .isNotEmpty() ?: false
            DepartmentID.value.toString().isNotEmpty() && ContactPersonName.value.toString()
                .isNotEmpty() ?: false
        }

    }

}