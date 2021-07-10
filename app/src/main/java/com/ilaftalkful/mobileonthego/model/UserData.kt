package com.ilaftalkful.mobileonthego.model

class UserData {

    private var status: Int = 0
    var statusMessage: String? = null
    var isSuccess: Boolean? = false
    private var error: ErrorData? = null

    init {
        this.error = null
    }

    fun error(error: ErrorData): UserData {
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

    fun loginSuccess(): UserData {
        this.status = UserStatus.LOGIN_SUCCESS
        this.error = null
        return this
    }


    fun registrationSuccess(messageStatus: String?): UserData {
        this.status = UserStatus.REGISTRATION_SUCCESS
        this.error = null
        this.statusMessage = messageStatus
        return this
    }
    fun userExistSUccess(messageStatus: String?,isSuccess:Boolean?): UserData {
        this.status = UserStatus.USER_EXIST_SUCCESS
        this.error = null
        this.isSuccess = isSuccess
        this.statusMessage = messageStatus
        return this
    }

    fun userExistFailed(): UserData {
        this.status = UserStatus.USER_EXIST_FAILED
        this.error = null
        return this
    }

    fun processing(): UserData {
        this.status = UserStatus.OPERATION_STARTED
        this.error = null
        return this
    }

    fun buttonClicked(): UserData {
        this.status = UserStatus.CLICKED
        this.error = null
        return this
    }

    fun responseSuccess(): UserData {
        this.status = UserStatus.RESPOSNSE_SUCCESS
        this.error = null
        return this
    }

    fun logOutSuccess(): UserData {
        this.status = UserStatus.LOG_OUT_SUCCESS
        this.error = null
        return this
    }

    fun logOutFailed(): UserData {
        this.status = UserStatus.LOG_OUT_ERROR
        this.error = null
        return this
    }

    fun sessionExpired(): UserData {
        this.status = UserStatus.SESSION_EXPIRED
        this.error = null
        return this
    }


    fun corporateQouteSuccess(mMessage: String?): UserData {
        this.status = UserStatus.CORPORATE_QOUTE_SUCCESS
        this.error = null
        this.statusMessage = mMessage
        return this
    }
    fun familyRefresh(): UserData {
        this.status = UserStatus.FAMILY_REFRESH
        this.error = null
        return this
    }
    fun corporateQouteFail(): UserData {
        this.status = UserStatus.CORPORATE_QOUTE_FAIL
        this.error = null
        return this
    }
    fun resetPasswordSuccess(message:String?): UserData {
        this.status = UserStatus.RESET_PASSWORD_SUCCESS
        this.error = null
        this.statusMessage = message
        return this
    }
    fun changePasswordSuccess(message:String?): UserData {
        this.status = UserStatus.CHANGE_PASSWORD_SUCCESS
        this.error = null
        this.statusMessage = message
        return this
    }

    fun fingerPrintLoginFailed(): UserData {
        this.status = UserStatus.FINGER_PRINT_LOGIN_FAILED
        this.error = null
        return this
    }

    fun userLoginFailed(): UserData {
        this.status = UserStatus.USER_LOGIN_FAILED
        this.error = null
        return this
    }

    fun appSettingsFailed(): UserData? {
        this.status = UserStatus.APPSETTINGS_FAILED
        this.error = null
        return this
    }
    fun appSettingsSuccess(): UserData? {
        this.status = UserStatus.APPSETTINGS_SUCCESS
        this.error = null
        return this
    }
    fun editFamilySuccess(): UserData? {
        this.status = UserStatus.EDIT_FAMILY_SUCCESS
        this.error = null
        return this
    }

    fun addFamilySuccess(): UserData? {
        this.status = UserStatus.ADD_FAMILY_SUCCESS
        this.error = null
        return this
    }

    fun familyListSuccess(): UserData? {
        this.status = UserStatus.FAMILY_LIST_SUCCESS
        this.error = null
        return this
    }
    fun pdfDownloadSuccess(): UserData? {
        this.status = UserStatus.PDF_DOWNLOAD_SUCCESS
        this.error = null
        return this
    }
    fun pdfDownloadFailed(msg:String?= null): UserData? {
        this.status = UserStatus.PDF_DOWNLOAD_FAILED
        this.error = null
        this.statusMessage = msg
        return this
    }
    interface UserStatus {
        companion object {
            val APPSETTINGS_SUCCESS = 1
            val APPSETTINGS_FAILED = 2

            val RESPOSNSE_SUCCESS = 100
            val CLICKED = 999
            val ERROR = 1000
            val FINGER_PRINT_LOGIN_FAILED = 10
            val LOG_OUT_ERROR = 10000
            val LOGIN_SUCCESS = 1001
            val OPERATION_STARTED = 1002
            val REGISTRATION_SUCCESS = 1003
            val LOG_OUT_SUCCESS = 9999
            val SESSION_EXPIRED = 99
            val RESET_PASSWORD_SUCCESS = 399
            val CHANGE_PASSWORD_SUCCESS = 400
            val EDIT_FAMILY_SUCCESS = 401
            val FAMILY_LIST_SUCCESS = 402
            val ADD_FAMILY_SUCCESS = 403

            val CORPORATE_QOUTE_SUCCESS = 101
            val FAMILY_REFRESH = 105
            val CORPORATE_QOUTE_FAIL = 102

            val SHOW_PAYMENT = 103
            val USER_EXIST_SUCCESS =1111
            val USER_EXIST_FAILED=1112
            val USER_LOGIN_FAILED =1113
            val PDF_DOWNLOAD_SUCCESS =1114
            val PDF_DOWNLOAD_FAILED =1115

        }
    }


}