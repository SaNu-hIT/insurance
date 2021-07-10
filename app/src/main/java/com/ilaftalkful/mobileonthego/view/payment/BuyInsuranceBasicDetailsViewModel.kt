package com.ilaftalkful.mobileonthego.view.payment

import android.app.Application
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.model.family.FamilyParameter
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveUpdate
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BuyInsuranceBasicDetailsViewModel(application: Application) : IlafBaseViewModel(application) {
    var isAgeError: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    internal var motorLiveData: MotorLiveUpdate? = null
    internal var userLiveData: UserLiveUpdate? = null
    var nationalityList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualNationalityList = ArrayList<TravelPolicyType>()
    var selectedNationality = MutableLiveData<Int>(0)
    var selectedPolicyType = MutableLiveData<Int>(-1)
    var policyTypeList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualpolicyType = ArrayList<TravelPolicyType>()
    var selectedPolicyOptions = MutableLiveData<Int>(-1)
    var policyOptionList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualpolicyOption = ArrayList<TravelPolicyType>()
    var selectedPolicyPeriod = MutableLiveData<Int>(-1)
    var policyPeriodList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualPolicyPeriod = ArrayList<TravelPolicyType>()
    var paymentPolicyType = MutableLiveData<String>("")
    var paymentAmount = MutableLiveData<String>("")
    val toDate = MutableLiveData<String>("")
    val startDate = MutableLiveData<String>("")
    val firstName = MutableLiveData<String>("")
    val secondName = MutableLiveData<String>("")
    val familyName = MutableLiveData<String>("")
    val paymentRequest = PaymentRequest()
    val mobileNumber = MutableLiveData<String>("")
    val emailId = MutableLiveData<String>("")
    var dashBoardResponse = MutableLiveData<DashBoardResponse>()
    val doB = MutableLiveData<String>("")
    var imageType: MutableLiveData<Int> = MutableLiveData<Int>(-1)
    val nationalId = MutableLiveData<String>("")
    val pssportNo = MutableLiveData<String>("")
    val civiliId = MutableLiveData<String>("")
    val familyNameMember = MutableLiveData<String>("")
    val dateOfBirth = MutableLiveData<String>("")
    val pssportNoMember = MutableLiveData<String>("")
    var ilafPreference: IlafSharedPreference? = null
    var familyList = MutableLiveData<ArrayList<Datum>>()
    var selectedFamilyMembers = MutableLiveData<ArrayList<Datum>>()
    var familyMembersList = ArrayList<Datum>(1)
    var memberList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualMemberList = ArrayList<Datum>()
    var selectedMemberId = MutableLiveData<Int>(0)
    val isTermsChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var relationList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualrelationList = ArrayList<TravelPolicyType>()
    var selectedrelationId= MutableLiveData<Int>(0)
    var isPassort: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isCivilId: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isIndividualSelected: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var termsAndConditions: MutableLiveData<String> = MutableLiveData<String>("")
    var maxStartDays :MutableLiveData<String> = MutableLiveData<String>("0")



fun clear(){
    /* isPassort.value=false
     isCivilId.value =false*/


}

    fun onSaveAndNextClicked(view: View, error: BuyInsurenceBasicErrors) {

        if (IlafValidator.isNullOrEmpty(error.nameError)
            && IlafValidator.isNullOrEmpty(error.secondNameError)
            && IlafValidator.isNullOrEmpty(error.userEmailError)
            && IlafValidator.isNullOrEmpty(error.phoneNumberError)
            && IlafValidator.isNullOrEmpty(error.familyNameError)
            && IlafValidator.isNullOrEmpty(error.civilidError)
                && IlafValidator.isNullOrEmpty(error.passPortNumber)
                && IlafValidator.isNullOrEmpty(error.nationalityError)
        )
        {
            motorLiveData?.showPayment()
        }
    }

    init {
        motorLiveData = MotorLiveUpdate()
        ilafPreference = IlafSharedPreference(application)
        userLiveData = UserLiveUpdate()
        pref = IlafSharedPreference(application)
        if (pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY) == null
            && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)
        ) {
            isENS.postValue(false)
        } else {
            isENS.postValue(true)
        }
        termsAndConditions.postValue(IlafSharedPreference(getApplication()).getStringPrefValue(IlafSharedPreference.Constants.TRAVEL_TC))
        maxStartDays.postValue(IlafSharedPreference(getApplication()).getStringPrefValue(IlafSharedPreference.Constants.TRAVEL_MAX_START_DAYS))
    }

    fun uploadImage(encoded: String) {
        motorLiveData?.processing()
        var imageUpload = ImageUpload()
        imageUpload.fileTypeExtension = Constants.FILE_FORMAT
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
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.responseSuccess(it.body()?.messageStatus)
                            processImageUpload(it.body()?.Data)
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
                                motorLiveData?.sessionExpired()
                            }else {
                                motorLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        }
                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, null))
                })
    }

    private fun processImageUpload(data: ImageUpload?) {
        if (imageType.value == Constants.DRIVER_CIVIL_ID) {
            isCivilId.postValue(true)
            paymentRequest.civilIDFilePath = data?.relativePath

        } else if (imageType.value == Constants.PASSPORT_NO) {
            isPassort.postValue(true)
            paymentRequest.passportFilePath = data?.relativePath

        }

    }


    var isValid = MediatorLiveData<Boolean>().apply {

        addSource(selectedPolicyPeriod) {
            value = it != -1 ?: false  && selectedPolicyOptions.value != -1 ?: false
                    && selectedPolicyType.value != -1 ?: false
        }
        addSource(selectedPolicyOptions) {
            value = it != -1 ?: false  && selectedPolicyPeriod.value != -1 ?: false
                    && selectedPolicyType.value != -1 ?: false
        }
        addSource(selectedPolicyType) {
            value = it != -1 ?: false && selectedPolicyOptions.value != -1 ?: false
                    && selectedPolicyPeriod.value != -1 ?: false
        }
    }

    fun getPolicyOptions() {
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.travelPolicyOptions()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            processPolicyoptions(it.body()?.Data!!)
                            motorLiveData?.policyTypeSuccees(it.body()?.messageStatus)
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
                                motorLiveData?.sessionExpired()
                            } else {
                                motorLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        }
                    } else {

                        motorLiveData?.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }

    private fun processPolicyoptions(data: ArrayList<TravelPolicyType>) {
        actualpolicyOption = ArrayList<TravelPolicyType>()
        var policyType = ArrayList<String>()
        if (data != null) {
            actualpolicyOption.addAll(data!!)
            for (item in data) {
                policyType.add(item.text!!)
            }
            policyOptionList.postValue(policyType)
        }

    }

    fun getPolicyType() {
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.travelPolicyTypes()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            processPolicyTypes(it.body()?.Data!!)
                            motorLiveData?.yearSuccess()

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
                                motorLiveData?.sessionExpired()
                            } else {
                                motorLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        }
                    } else {

                        motorLiveData?.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }

    private fun processPolicyTypes(data: ArrayList<TravelPolicyType>) {
        actualpolicyType = ArrayList<TravelPolicyType>()
        var policyType = ArrayList<String>()
        if (data != null) {
            actualpolicyType.addAll(data!!)
            for (item in data) {
                policyType.add(item.text!!)
            }
            policyTypeList.postValue(policyType)
        }

    }

    fun getNationalities() {
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.nationalities()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.responseSuccess()
                            processNationalities(it.body()?.Data!!)
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
                                motorLiveData?.sessionExpired()
                            } else {
                                motorLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        }
                    } else {

                        motorLiveData?.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })

    }

    private fun processNationalities(data: ArrayList<TravelPolicyType>) {
        actualNationalityList = ArrayList<TravelPolicyType>()
        var nationality = ArrayList<String>()
        if (data != null) {
            actualNationalityList.addAll(data!!)
            nationality.add("")
            for (item in data) {
                nationality.add(item.text!!)
            }
            nationalityList.postValue(nationality)
        }

    }

    fun getTravelPolicyPeriods() {
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.travelPolicyPeriods()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.renewSuccess()
                            processPolicyPeriods(it.body()?.Data!!)
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
                                motorLiveData?.sessionExpired()
                            } else {
                                motorLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        }
                    } else {

                        motorLiveData?.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }

    private fun processPolicyPeriods(data: ArrayList<TravelPolicyType>) {
        actualPolicyPeriod = ArrayList<TravelPolicyType>()
        var policyType = ArrayList<String>()
        if (data != null) {
            actualPolicyPeriod.addAll(data!!)
            for (item in data) {
                policyType.add(item.text!!)
            }
            policyPeriodList.postValue(policyType)
        }

    }

    fun getTotalPolicyAmount() {
        motorLiveData?.processing()
        paymentPolicyType.postValue(actualpolicyType.get(selectedPolicyType.value!!).text)
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        var policyPeriodId = actualPolicyPeriod.get(selectedPolicyPeriod.value!!).id
        var policyTypeId = actualpolicyType.get(selectedPolicyType.value!!).id
        var policyOptionId = actualpolicyOption.get(selectedPolicyOptions.value!!).id
        val subscribe =
            dashBoardService?.travelPremiumAmount(policyPeriodId, policyTypeId, policyOptionId)
                ?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.responseSuccess()
                            processTotalAmount(it.body()?.Data!!)
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
                                motorLiveData?.sessionExpired()
                            } else {
                                motorLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        }
                    } else {

                        motorLiveData?.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }

    private fun processTotalAmount(data: String) {
        paymentAmount.postValue(data)
    }

    fun onPaymentSubmit() {


        paymentRequest.nameFirst = firstName.value
        paymentRequest.nameSecond = secondName.value
        paymentRequest.nameFamily = familyName.value
        paymentRequest.mobileNumber = mobileNumber.value
        paymentRequest.civilIDNumber = civiliId.value
        paymentRequest.emailID = emailId.value
        paymentRequest.dOB = DateUtil.getDateTorequestFromat(doB.value)
        paymentRequest.policyTypeID = actualpolicyType.get(selectedPolicyType.value!!).id
        paymentRequest.policyOptionID = actualpolicyOption.get(selectedPolicyOptions.value!!).id
        paymentRequest.policyPeriodID = actualPolicyPeriod.get(selectedPolicyPeriod.value!!).id
        paymentRequest.passportNumber = pssportNo.value
        paymentRequest.nationalityID = selectedNationality.value!!
        paymentRequest.policyDateStart = DateUtil.getDateTorequestFromat(startDate.value)
        paymentRequest.policyDateEnd = DateUtil.getDateTorequestFromat(toDate.value)


        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.travelPolicyAdd(paymentRequest)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.paymentSuccess(it.body()?.Data!!)
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
                                motorLiveData?.sessionExpired()
                            } else {
                                motorLiveData?.postError(ErrorData(it.code(), it?.body()?.messageStatus))
                            }
                        }
                    } else {

                        motorLiveData?.postError(ErrorData(it.code(), it.message()))

                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })

    }

    fun getFamilyList() {
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.GetfamilyMember()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.responseSuccess()
                            familyList.postValue(it?.body()?.Data)
                            processFamilyData(it?.body()?.Data)

                        } else {

                            motorLiveData?.postError(ErrorData(it.code(), it.message()))

                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            ilafPreference?.setBooleanPrefValue(
                                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                false
                            )
                            ilafPreference?.setStringPrefValue(
                                IlafSharedPreference.Constants.TOKEN_KEY,
                                null
                            )
                            motorLiveData?.sessionExpired()
                        } else {
                            motorLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })

    }

    private fun processFamilyData(data: ArrayList<Datum>?) {
        actualMemberList = ArrayList<Datum>()
        var membertype = ArrayList<String>()
        if (data != null) {
            actualMemberList.addAll(data!!)
            for (item in data) {
                membertype.add(item.relationDescription!!)
            }
            memberList.postValue(membertype)
        }

    }

    fun setFamilyList(familyListSelected: ArrayList<Datum>) {
        var travelPolicyDetails = ArrayList<TravelPolicyDetail>(1)
        for (item in familyListSelected) {
            var travelPolicyDetailItem = TravelPolicyDetail()
            travelPolicyDetailItem.familyMemberID = item?.familyMemberID.toInt()
            travelPolicyDetails.add(travelPolicyDetailItem)
        }
        paymentRequest.travelPolicyDetails = travelPolicyDetails
        selectedFamilyMembers.postValue(familyListSelected)
    }


    fun AddFamilyMember() {
        var datum = FamilyParameter()
        //actualMemberList?.get(selectedMemberId.value!! -1)?.familyMemberID.toString()
        datum.dob = DateUtil.getDateTorequestFromat(dateOfBirth.value)
        datum.fullName = familyNameMember.value
        datum.relationID = actualrelationList.get(selectedrelationId.value!!).id!!
        datum.passportNumber = pssportNoMember.value
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.postFamilyCreate(datum)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            motorLiveData?.addFamilyMemberSuccess()
                            clearData()
                        } else {
                            motorLiveData?.postError(ErrorData(it.code(), it.body()?.message))
                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            ilafPreference?.setBooleanPrefValue(
                                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                false
                            )
                            ilafPreference?.setStringPrefValue(
                                IlafSharedPreference.Constants.TOKEN_KEY,
                                null
                            )
                            motorLiveData?.sessionExpired()
                        } else {
                            motorLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })

    }

    private fun clearData() {
        selectedMemberId.postValue(0)
        pssportNoMember.postValue("")
        familyNameMember.postValue("")
        dateOfBirth.postValue("")
    }

    fun getRelation() {
        motorLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
                dashBoardService?.getRelation()?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                if (it.body()?.isSuccess!!) {
                                    motorLiveData?.claimSuccess()
                                    processRelationData(it?.body()?.Data)

                                } else {

                                    motorLiveData?.postError(ErrorData(it.code(), it.message()))

                                }
                            } else {
                                if (it.code() == Constants.UNAUTH_ERROR) {
                                    ilafPreference?.setBooleanPrefValue(
                                            IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                            false
                                    )
                                    ilafPreference?.setStringPrefValue(
                                            IlafSharedPreference.Constants.TOKEN_KEY,
                                            null
                                    )
                                    motorLiveData?.sessionExpired()
                                } else {
                                    motorLiveData?.postError(ErrorData(it.code(), it.message()))
                                }
                            }

                        }, { error ->
                            motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                            error.printStackTrace()
                        })

    }
    private fun processRelationData(data: ArrayList<TravelPolicyType>?) {
        actualrelationList = ArrayList<TravelPolicyType>()
        var membertype = ArrayList<String>()
        if (data != null) {
            actualrelationList.addAll(data!!)
            for (item in data) {
                membertype.add(item.text!!)
            }
            relationList.postValue(membertype)
        }

    }
    fun getDashBoardData() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
                dashBoardService?.getDashBoardDetails()?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                if (it.body()?.isSuccess!!) {
                                    dashBoardResponse.postValue(it.body()?.Data)
                                    userLiveData?.responseSuccess()
                                } else {

                                    userLiveData?.postError(ErrorData(it.code(), it.message()))

                                }
                            } else {
                                if (it.code() == Constants.UNAUTH_ERROR) {
                                    ilafPreference?.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                    ilafPreference?.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                    userLiveData?.sessionExpired()
                                } else {
                                    userLiveData?.postError(ErrorData(it.code(), it.message()))
                                }
                            }

                        }, { error ->
                            motorLiveData?.postError(ErrorData(100, "Unknown Error...!!!"))
                            error.printStackTrace()
                        })
    }

    public fun processData(userDetails: UserDetailsDashBoard?) {
        firstName.postValue(userDetails?.nameFirst  )
        secondName.postValue(userDetails?.nameLast  )
        emailId.postValue(userDetails?.emailID  )
        mobileNumber.postValue(userDetails?.mobileNumber  )
        civiliId.postValue(userDetails?.civilID  )
        System.out.println("ISUUUUUU"+userDetails?.dOB)
        doB.postValue(DateUtil.getStringDateToFromat(userDetails?.dOB  ))

    }

}