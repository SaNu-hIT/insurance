package com.ilaftalkful.mobileonthego.model.mototqoute

import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData

class MotorLiveUpdate : MutableLiveData<MotorLiveData>() {

    var mData = MotorLiveData()

    fun postError(throwable: ErrorData) {
        postValue(mData.error(throwable))
    }

    fun processing() {
        postValue(mData.processing())
    }

    fun buttonClicked() {
        postValue(mData.buttonClicked())
    }


    fun responseSuccess(message: String?=null) {
        postValue(mData.responseSuccess(message))
    }
    fun addFamilyMemberSuccess() {
        postValue(mData.addFamilyMemberSuccess())
    }
    fun paymentSuccess(message: String?=null) {
        postValue(mData.paymentSuccess(message))
    }

    fun quoteSuccess(message: String?=null) {
        postValue(mData.quoteSuccess(message))
    }
    fun claimSuccess(message: String?=null) {
        postValue(mData.claimSuccess(message))
    }
    fun renewSuccess(message: String?=null) {
        postValue(mData.renewSuccess(message))
    }
    fun yearSuccess() {
        postValue(mData.yearSuccess())
    }

    fun yearError() {
        postValue(mData.yearError())
    }
    fun sessionExpired() {
        postValue(mData.sessionExpired())
    }
    fun claimImageUploadSuccess() {
        postValue(mData.claimImageUploadSuccess())
    }
    fun showPayment() {
        postValue(mData.showPayment())
    }

    fun showPoliceReportImageError() {
        postValue(mData.showPoliceReportImageError())
    }
    fun showVehicleRegImageError() {
        postValue(mData.showVehicleRegImageError())
    }
    fun showDriverCivilIdImageError() {
        postValue(mData.showDriverCivilIdImageError())
    }
    fun showCarOwnerIdImageError() {
        postValue(mData.showCarOwnerIdImageError())
    }
    fun showDriverDrivingIdImageError() {
        postValue(mData.showDriverDrivingIdImageError())
    }
    fun showPolicyFirstPageImageError() {
        postValue(mData.showPolicyFirstPageImageError())
    }
    fun showFrontImageError() {
        postValue(mData.showFrontImageError())
    }
    fun showRearSideImageError() {
        postValue(mData.showRearSideImageError())
    }
    fun showDriverSideImageError() {
        postValue(mData.showDriverSideImageError())
    }
    fun showPassengerSideImageError() {
        postValue(mData.showPassengerSideImageError())
    }
    fun showAccidentSiteImageError() {
        postValue(mData.showAccidentSiteImageError())
    }
    fun showFullViewImageError() {
        postValue(mData.showFullViewImageError())
    }
    fun showFrontBumperImageError() {
        postValue(mData.showFrontBumperImageError())
    }
    fun showRearBumperImageError() {
        postValue(mData.showRearBumperImageError())
    }

    fun policyTypeSuccees(message: String?=null) {
        postValue(mData.policyTypeSuccess(message))
    }
}
