package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveUpdate
import com.ilaftalkful.mobileonthego.model.regster.MotorPolicies
import com.ilaftalkful.mobileonthego.model.regster.PolicyList
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MotorClaimViewModel(application: Application) : IlafBaseViewModel(application) {

    val policyNo = MutableLiveData<String>("")
    val policeReport = MutableLiveData<String>("")
    val vehicleReg = MutableLiveData<String>("")
    val civilIdCarOwner = MutableLiveData<String>("")
    val driverCivilId = MutableLiveData<String>("")
    val driverDriverId = MutableLiveData<String>("")
    lateinit var context: Context
    var isDateEnabled = MutableLiveData<Boolean>(false)
    internal var userLiveData: MotorLiveUpdate? = null
    var policiesList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var motorPolicyDetails = MutableLiveData<MotorPolicyDetails>()
    var actualPolicyList = ArrayList<PolicyList>()
    var imageType: MutableLiveData<Int> = MutableLiveData<Int>(-1)
    var motorClaim = MotorClaim()
    val accidentDate: MutableLiveData<String> = MutableLiveData<String>("")
    val isTermsChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var policyId = MutableLiveData<Int>()
    var isPolicyUploaded  = MutableLiveData<Boolean>(false)
    var isAudioUploaded  = MutableLiveData<Boolean>(false)
    var isPoliceReport  = MutableLiveData<Boolean>(false)
    var isVehicleReg  = MutableLiveData<Boolean>(false)
    var isOwnerCivilId  = MutableLiveData<Boolean>(false)
    var isDriverCivilId  = MutableLiveData<Boolean>(false)
    var isDriverLicenceId  = MutableLiveData<Boolean>(false)
    var termsAndConditions: MutableLiveData<String> = MutableLiveData<String>("")




    init {
        userLiveData = MotorLiveUpdate()
        context = application
        pref = IlafSharedPreference(application)
        termsAndConditions.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.MOTOR_TC))

    }


    fun getPolicyList() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.MotorPolicies()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess(it.body()?.messageStatus)
                            processResult(it.body()?.Data!!)
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    } else if (it.code() == Constants.UNAUTH_ERROR) {
                        pref.setBooleanPrefValue(
                            IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                            false
                        )
                        pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                        userLiveData?.sessionExpired()
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }


    private fun processResult(data: MotorPolicies) {
        actualPolicyList = ArrayList<PolicyList>()
        var policyName = ArrayList<String>()
        if (data?.policyList != null) {
            actualPolicyList.addAll(data.policyList!!)
            for (item in data.policyList!!) {
                policyName.add(item.policyNumber!!)
            }
            policiesList.postValue(policyName)
        }
    }

    fun getPhotos(): ArrayList<AccidentPhotosModel> {
        var damageSideList = ArrayList<AccidentPhotosModel>(1)
        var item = AccidentPhotosModel()
        item.id = 0
        item.imageIcon = context.getDrawable(R.drawable.ic_front)
        damageSideList.add(item)
        item = AccidentPhotosModel()

        item.id = 1
        item.imageIcon = context.getDrawable(R.drawable.ic_rearside)
        damageSideList.add(item)
        item = AccidentPhotosModel()
        item.id = 2
        item.imageIcon = context.getDrawable(R.drawable.ic_side_driver)
        damageSideList.add(item)
        item = AccidentPhotosModel()
        item.id = 3
        item.imageIcon = context.getDrawable(R.drawable.ic_side_passenger)
        damageSideList.add(item)
        item = AccidentPhotosModel()
        item.id = 4
        item.imageIcon = context.getDrawable(R.drawable.ic_accident_site)
        damageSideList.add(item)
        item = AccidentPhotosModel()
        item.id = 5
        item.imageIcon = context.getDrawable(R.drawable.ic_full_view)
        damageSideList.add(item)
        item = AccidentPhotosModel()
        item.id = 6
        item.imageIcon = context.getDrawable(R.drawable.ic_bumper_back)
        damageSideList.add(item)
        item = AccidentPhotosModel()
        item.id = 7
        item.imageIcon = context.getDrawable(R.drawable.ic_bumper_front)
        damageSideList.add(item)
        return damageSideList

    }

    fun uploadImage(encoded: String, ext: String? = null) {
        userLiveData?.processing()
        var imageUpload = ImageUpload()
        imageUpload.module = FileUploadModules.MOTOR.value
        if (null == ext)
            imageUpload.fileTypeExtension = Constants.FILE_FORMAT
        else
            imageUpload.fileTypeExtension = ext
        imageUpload.imageData = encoded
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.uploadFile(imageUpload)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            it.body()?.Data?.relativePath?.let { it1 -> setImagePath(it1) }
                            userLiveData?.claimImageUploadSuccess()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                        }
                    } else if (it.code() == Constants.UNAUTH_ERROR) {
                        pref.setBooleanPrefValue(
                            IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                            false
                        )
                        pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                        userLiveData?.sessionExpired()
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }

    private fun setImagePath(encoded: String) {
        when (imageType.value!!) {
            0 -> {
                motorClaim.frontSideFileName = encoded
            }
            1 -> {
                motorClaim.rearSideFileName = encoded
            }
            2 -> {
                motorClaim.driverSideFileName = encoded
            }
            3 -> {
                motorClaim.passengerSideFileName = encoded
            }
            4 -> {
                motorClaim.accidentSiteFileName = encoded
            }
            5 -> {
                motorClaim.fullViewFileName = encoded
            }
            6 -> {
                motorClaim.rearBumperFileName = encoded
            }
            7 -> {
                motorClaim.frontBumperFileName = encoded
            }
            Constants.VEHICLE_REG -> {
                motorClaim.vehicleRegistrationFileName = encoded
            }
            Constants.POLICE_REPORT -> {
                isPoliceReport.postValue(true)
                motorClaim.policeReportFileName = encoded
            }
            Constants.CAR_OWNER_ID -> {
                isOwnerCivilId.postValue(true)
                motorClaim.ownerCivilIDFileName = encoded
            }
            Constants.POLICY_FIRST_PAGE -> {
                isPolicyUploaded.postValue(true)
                motorClaim.motorPolicyFileName = encoded
            }
            Constants.DRIVER_DRIVER_ID -> {
                isDriverLicenceId.postValue(true)
                motorClaim.drivingLicenseFileName = encoded
            }
            Constants.DRIVER_CIVIL_ID -> {
                isDriverCivilId.postValue(true)
                motorClaim.driverCivilIDFileName = encoded
            }
            Constants.VOICE_NOTE -> {
                isAudioUploaded.postValue(true)
                motorClaim.voiceNoteFileName = encoded
            }
        }
    }


    fun notifyClaim(errors: TravelClaimErrors) {
        userLiveData?.buttonClicked()

        if (IlafValidator.isNullOrEmpty(errors.policyNoError) && IlafValidator.isNullOrEmpty(errors.policeReportError)
            && IlafValidator.isNullOrEmpty(errors.carOwnerCivilIdError) && IlafValidator.isNullOrEmpty(
                errors.driverCivilIdError
            )
            && IlafValidator.isNullOrEmpty(errors.accidentDateError) && IlafValidator.isNullOrEmpty(
                errors.vehicleRegError
            )
            && IlafValidator.isNullOrEmpty(errors.driverDriverIdError) && IlafValidator.isNullOrEmpty(
                errors.termsAndConditionsError
            )
            && (!isVisible.value!!)
        ) {
            motorClaim.motorPolicyNo = policyNo.value
            if (actualPolicyList != null && policyId != null) {
                motorClaim.motorPolicyID = (policyId.value!!).toString()
            }
            motorClaim.policeReportNumber = policeReport.value
            motorClaim.accidentDate = DateUtil.getDateTorequestFromat(accidentDate.value)
            motorClaim.vehicleRegistrationNumber = vehicleReg.value
            motorClaim.ownerCivilIDNumber = civilIdCarOwner.value
            motorClaim.driverCivilIDNumber = driverCivilId.value
            motorClaim.driverDriverIDNumber = driverDriverId.value
            userLiveData?.processing()
            var loginService = UserService.create(getApplication<Application>(), true)
            val subscribe =
                loginService?.CreateMotorClaim(motorClaim)?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                    ?.subscribeOn(
                        Schedulers.io()
                    )
                    ?.subscribe({
                        if (it.isSuccessful) {
                            if (it.code() == 200) {
                                errors.uiUpdate = true
                                userLiveData?.claimSuccess()
                            } else {
                                errors.uiUpdate = false
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
                        } // this will tell you why your api doesnt work most of time

                    }, { error ->
                        errors.uiUpdate = false
                        userLiveData?.postError(
                            ErrorData(
                                500, error.message
                                    ?: "Something went wrong"
                            )
                        )
                        error.printStackTrace()

                    })
        }

    }

    fun getPolicyDetails(position: Int) {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), true)
        var policyId = actualPolicyList.get(position).motorPolicyID
        val subscribe =

            loginService?.motorPolicyDetails(policyId.toString())?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.code() == 200) {
                            motorPolicyDetails.postValue(it.body()?.Data)
                            processDetails(it.body()?.Data)
                            userLiveData?.responseSuccess()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            pref.setBooleanPrefValue(
                                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                false
                            )
                            pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                            userLiveData?.sessionExpired()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    } // this will tell you why your api doesnt work most of time

                }, { error ->
                    userLiveData?.postError(
                        ErrorData(
                            500, error.message
                                ?: "Something went wrong"
                        )
                    )
                    error.printStackTrace()

                })

    }

    private fun processDetails(data: MotorPolicyDetails?) {
        policyNo.postValue(data?.policyNumber)
        vehicleReg.postValue(data?.registerNumber)
        civilIdCarOwner.postValue(data?.civilIDNumber)
    }

    fun removeItem(id: Int?, data: ArrayList<AccidentPhotosModel>) {
        for (item in data) {
            if (item.id == id) {
                data.removeAt(item.id ?: 0)
                return
            }
        }

    }

    fun isAllImagesAttahced(): Boolean {
        if(motorClaim.policeReportFileName==null){
            userLiveData?.showPoliceReportImageError()
            return false
        }else  if(motorClaim.vehicleRegistrationFileName==null){
            userLiveData?.showVehicleRegImageError()
            return false
        }else  if(motorClaim.ownerCivilIDFileName==null){
            userLiveData?.showCarOwnerIdImageError()
            return false
        }else  if(motorClaim.driverCivilIDFileName==null){
            userLiveData?.showDriverCivilIdImageError()
            return false
        }else  if(motorClaim.drivingLicenseFileName==null){
            userLiveData?.showDriverDrivingIdImageError()
            return false
        }else  if(motorClaim.motorPolicyFileName==null){
            userLiveData?.showPolicyFirstPageImageError()
            return false
        }else  if(motorClaim.frontSideFileName==null){
            userLiveData?.showFrontImageError()
            return false
        }else  if(motorClaim.rearSideFileName==null){
            userLiveData?.showRearSideImageError()
            return false
        }else  if(motorClaim.driverSideFileName==null){
            userLiveData?.showDriverSideImageError()
            return false
        }else  if(motorClaim.passengerSideFileName==null){
            userLiveData?.showPassengerSideImageError()
            return false
        }else  if(motorClaim.accidentSiteFileName==null){
            userLiveData?.showAccidentSiteImageError()
            return false
        }else  if(motorClaim.fullViewFileName==null){
            userLiveData?.showFullViewImageError()
            return false
        }else  if(motorClaim.frontBumperFileName==null){
            userLiveData?.showFrontBumperImageError()
            return false
        }else  if(motorClaim.rearBumperFileName==null){
            userLiveData?.showRearBumperImageError()
            return false
        }else{
            return true
        }
    }

}