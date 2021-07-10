package com.ilaftalkful.mobileonthego.model.mototqoute

import com.ilaftalkful.mobileonthego.model.ErrorData

class MotorLiveData {

    private var status: Int = 0
    private var error: ErrorData? = null
    var statusMessage: String? = null

    init {
        this.error = null
    }

    fun error(error: ErrorData): MotorLiveData {
        this.status = UserStatus.ERROR
        this.error = error
        return this
    }

    fun getStatus(): Int {
        return status
    }

    fun getError(): ErrorData? {
        return error
    }



    fun processing(): MotorLiveData {
        this.status = UserStatus.OPERATION_STARTED
        this.error = null
        return this
    }

    fun buttonClicked(): MotorLiveData {
        this.status = UserStatus.CLICKED
        this.error = null
        return this
    }

    fun responseSuccess(message: String?): MotorLiveData {
        this.status = UserStatus.RESPOSNSE_SUCCESS
        this.error = null
        this.statusMessage =message
        return this
    }

    fun paymentSuccess(message: String?): MotorLiveData {
        this.status = UserStatus.PAYMENT_SUCCESS
        this.error = null
        this.statusMessage =message
        return this
    }

    fun claimSuccess(message: String?): MotorLiveData {
        this.status = UserStatus.CLAIM_SUCCESS
        this.error = null
        this.statusMessage =message
        return this
    }

    fun renewSuccess(message: String?): MotorLiveData {
        this.status = UserStatus.RENEW_SUCCESS
        this.error = null
        this.statusMessage =message
        return this
    }
    fun quoteSuccess(message: String?): MotorLiveData {
        this.status = UserStatus.QUOTE_SUCCESS
        this.error = null
        this.statusMessage =message
        return this
    }
    fun yearSuccess(): MotorLiveData {
        this.status = UserStatus.YEAR_SUCCESS
        this.error = null
        return this
    }

    fun yearError(): MotorLiveData {
        this.status = UserStatus.YEAR_ERROR
        this.error = null
        return this
    }

    fun sessionExpired(): MotorLiveData {
        this.status = UserStatus.SESSION_EXPIRED
        this.error = null
        return this
    }

    fun addFamilyMemberSuccess(): MotorLiveData? {
        this.status = UserStatus.ADD_MEMBER_SUCCESS
        this.error = null
        return this
    }

    fun claimImageUploadSuccess(): MotorLiveData? {
        this.status = UserStatus.CLAIM_IMAGE_UPLOAD_SUCCESS
        this.error = null
        return this
    }

    fun showPayment(): MotorLiveData? {
        this.status = UserStatus.SHOW_PAYMENT
        this.error = null
        return this
    }

    fun showPoliceReportImageError(): MotorLiveData? {
        this.status = UserStatus.POLICE_REPORT_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showVehicleRegImageError(): MotorLiveData? {
        this.status = UserStatus.VEHICLE_REG_IMAGE_ERROR
        this.error = null
        return this
    }
    fun showCarOwnerIdImageError(): MotorLiveData? {
        this.status = UserStatus.CAR_OWNER_ID_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showDriverCivilIdImageError(): MotorLiveData? {
        this.status = UserStatus.DRIVER_CIVIL_ID_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showDriverDrivingIdImageError(): MotorLiveData? {
        this.status = UserStatus.DRIVER_DRIVER_ID_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showPolicyFirstPageImageError(): MotorLiveData? {
        this.status = UserStatus.POLICY_FIRST_PAGE_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showFrontImageError(): MotorLiveData? {
        this.status = UserStatus.FRONT_SIDE_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showRearSideImageError(): MotorLiveData? {
        this.status = UserStatus.REAR_SIDE_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showDriverSideImageError(): MotorLiveData? {
        this.status = UserStatus.DRIVER_SIDE_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showPassengerSideImageError(): MotorLiveData? {
        this.status = UserStatus.PASSENGER_SIDE_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showAccidentSiteImageError(): MotorLiveData? {
        this.status = UserStatus.ACCIDENT_SITE_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showFullViewImageError(): MotorLiveData? {
        this.status = UserStatus.FULL_VIEW_IMEGE_ERROR
        this.error = null
        return this
    }
    fun showRearBumperImageError(): MotorLiveData? {
        this.status = UserStatus.REAR_BUMPER_IMEGE_ERROR
        this.error = null
        return this
    }
     fun showFrontBumperImageError(): MotorLiveData? {
        this.status = UserStatus.FRONT_BUMPER_IMEGE_ERROR
        this.error = null
        return this
    }

    fun policyTypeSuccess(message: String?=null): MotorLiveData? {
        this.status = UserStatus.POLICY_TYPE_SUCCESS
        this.error = null
        this.statusMessage = message
        return this
    }
    interface UserStatus {
        companion object {
            val SESSION_EXPIRED = 99
            val RESPOSNSE_SUCCESS = 100
            val CLICKED = 999
            val ERROR = 1000
            val OPERATION_STARTED = 1002
            val YEAR_SUCCESS = 1001
            val YEAR_ERROR = 1003
            val QUOTE_SUCCESS = 500
            val RENEW_SUCCESS = 600
            val CLAIM_SUCCESS = 700
            val PAYMENT_SUCCESS = 800
            val ADD_MEMBER_SUCCESS = 900
            val CLAIM_IMAGE_UPLOAD_SUCCESS = 1100
            val SHOW_PAYMENT = 1101
            val POLICY_TYPE_SUCCESS= 1201

            val POLICE_REPORT_IMEGE_ERROR= 1
            val VEHICLE_REG_IMAGE_ERROR= 11
            val CAR_OWNER_ID_IMEGE_ERROR= 21
            val POLICY_FIRST_PAGE_IMEGE_ERROR= 31
            val DRIVER_DRIVER_ID_IMEGE_ERROR= 41
            val DRIVER_CIVIL_ID_IMEGE_ERROR= 51
            val FRONT_SIDE_IMEGE_ERROR= 61
            val REAR_SIDE_IMEGE_ERROR= 71
            val DRIVER_SIDE_IMEGE_ERROR= 81
            val PASSENGER_SIDE_IMEGE_ERROR= 91
            val ACCIDENT_SITE_IMEGE_ERROR= 101
            val FULL_VIEW_IMEGE_ERROR= 111
            val REAR_BUMPER_IMEGE_ERROR= 121
            val FRONT_BUMPER_IMEGE_ERROR= 131
        }
    }


}