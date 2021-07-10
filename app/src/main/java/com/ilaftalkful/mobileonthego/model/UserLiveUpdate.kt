package com.ilaftalkful.mobileonthego.model

import androidx.lifecycle.MutableLiveData

class UserLiveUpdate : MutableLiveData<UserData>() {

    var mData = UserData()

    fun postError(throwable: ErrorData) {
        postValue(mData.error(throwable))
    }

    fun userLoginSuccess() {
        postValue(mData.loginSuccess())
    }

    fun userExistSuccess(messageStatus: String?,isSuccess :Boolean) {
        postValue(mData.userExistSUccess(messageStatus,isSuccess))
    }

    fun userExistFailed() {
        postValue(mData.userExistFailed())
    }


    fun userRegistrationSuccess(messageStatus: String?) {
        postValue(mData.registrationSuccess(messageStatus))
    }


    fun processing() {
        postValue(mData.processing())
    }

    fun buttonClicked() {
        postValue(mData.buttonClicked())
    }


    fun responseSuccess() {
        postValue(mData.responseSuccess())
    }
    fun familyListSUccess() {
        postValue(mData.familyListSuccess())
    }
    fun logOutSuccess() {
        postValue(mData.logOutSuccess())
    }

    fun logOutFailed() {
        postValue(mData.logOutFailed())
    }

    fun sessionExpired() {
        postValue(mData.sessionExpired())
    }

    fun CorporatteQouteSuccess(mMessage: String?) {
        postValue(mData.corporateQouteSuccess(mMessage))
    }
    fun CorporatteQouteFail() {
        postValue(mData.corporateQouteFail())
    }
    fun familyRefresh() {
        postValue(mData.familyRefresh())
    }
    fun resetPasswordSuccess(message:String?) {
        postValue(mData.resetPasswordSuccess(message))
    }
    fun chanegePasswordSuccess(message:String?) {
        postValue(mData.changePasswordSuccess(message))
    }

    fun fingerPrintLoginFailed() {
        postValue(mData.fingerPrintLoginFailed())
    }

    fun userLoginFailed() {
        postValue(mData.userLoginFailed())
    }

    fun appSettingsSuccess() {
        postValue(mData.appSettingsSuccess())
    }
    fun appSettingsFailed() {
        postValue(mData.appSettingsFailed())
    }

    fun editFamilySuccess() {
        postValue(mData.editFamilySuccess())
    }

    fun addFamilySuccess() {
        postValue(mData.addFamilySuccess())
    }

    fun pdfdownloadSuccess() {
        postValue(mData.pdfDownloadSuccess())
    }

    fun pdfDownloadFailed(msg:String?=null) {
        postValue(mData.pdfDownloadFailed(msg))
    }

}
