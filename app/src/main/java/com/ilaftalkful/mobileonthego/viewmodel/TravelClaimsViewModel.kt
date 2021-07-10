package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveUpdate
import com.ilaftalkful.mobileonthego.model.regster.MotorPolicies
import com.ilaftalkful.mobileonthego.model.regster.PolicyList
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TravelClaimsViewModel(application: Application) : IlafBaseViewModel(application) {
    var data = MutableLiveData<ArrayList<String>>(ArrayList<String>())
    var policyId = MutableLiveData<Int>()
    var selectedPolicyType = MutableLiveData<Int>(0)
    val policyNo = MutableLiveData<String>("")
    val phoneNoText = MutableLiveData<String>("")
    val typeOfClaim = MutableLiveData<String>("")
    val claimAmount = MutableLiveData<String>("")
    val dateOfSickness = MutableLiveData<String>("")
    val exitDate = MutableLiveData<String>("")
    val entryDate = MutableLiveData<String>("")
    val briefDtata = MutableLiveData<String>("")
    var policiesList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualPolicyList = ArrayList<PolicyList>()
    var policyTypeList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualpolicyType = ArrayList<TravelPolicyType>()
    internal var userLiveData: MotorLiveUpdate? = null
    val isVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var travelClaimAttachmentModel = ArrayList<TravelClaimAttachmentModel>()
    val isTermsChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isDocumentUploaded: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val termsAndConditions: MutableLiveData<String> = MutableLiveData<String>("")



    init {
        userLiveData = MotorLiveUpdate()
        termsAndConditions.postValue(IlafSharedPreference(getApplication()).getStringPrefValue(IlafSharedPreference.Constants.TRAVEL_TC))
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
                                }else {
                                    userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                                }
                            } else if (it.code() == Constants.UNAUTH_ERROR) {
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                userLiveData?.sessionExpired()
                            }

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, null))
                        })
    }

    private fun processResult(data: MotorPolicies) {
        actualPolicyList  = ArrayList<PolicyList> ()
        var policyName = ArrayList<String>()
        if(data?.policyList != null) {
            actualPolicyList.addAll(data.policyList!!)
            for (item in data.policyList!!) {
                policyName.add(item.policyNumber!!)
            }
            policiesList.postValue(policyName)
        }
    }

    fun getPolicyType() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
                dashBoardService?.travelPolicyOptions()?.observeOn(
                        AndroidSchedulers.mainThread()
                )
                        ?.subscribeOn(
                                Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                if (it.body()?.isSuccess!!) {
                                    userLiveData?.responseSuccess(it.body()?.messageStatus)
                                    processPolicyTypes(it.body()?.Data!!)
                                } else {
                                    userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                                }
                            }
                            else if (it.code() == Constants.UNAUTH_ERROR) {
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                userLiveData?.sessionExpired()
                            }

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, null))
                        })    }

    private fun processPolicyTypes(data: ArrayList<TravelPolicyType>) {
        actualpolicyType  = ArrayList<TravelPolicyType> ()
        var policyType = ArrayList<String>()
        if(data != null) {
            actualpolicyType.addAll(data!!)
            for (item in data) {
                policyType.add(item.text!!)
            }
            policyTypeList.postValue(policyType)
        }

    }

    fun onSubmit(errors: TravelClaimErrors) {


        if(IlafValidator.isNullOrEmpty(errors.policyNoError) && IlafValidator.isNullOrEmpty(errors.typeOfClaimError)
                && IlafValidator.isNullOrEmpty(errors.claimAmountError) && IlafValidator.isNullOrEmpty(errors.policyTypeError)
                && IlafValidator.isNullOrEmpty(errors.dateSicknessError) && IlafValidator.isNullOrEmpty(errors.exitDateError)
                && IlafValidator.isNullOrEmpty(errors.entryDateError) && IlafValidator.isNullOrEmpty(errors.briefError)
                && !isVisible.value!!){

                    var travelClaimSubmitRequest = TravelClaimSubmitRequest()
            travelClaimSubmitRequest.briefDescription=briefDtata.value
            travelClaimSubmitRequest.travelPolicyID=policyId.value
            travelClaimSubmitRequest.claimAmount=claimAmount.value?.toInt()
            travelClaimSubmitRequest.claimTypeID = actualpolicyType.get(((selectedPolicyType.value?:0))).id
            travelClaimSubmitRequest.dateEntry =DateUtil.getDateTorequestFromat(entryDate.value)
            travelClaimSubmitRequest.dateExit =DateUtil.getDateTorequestFromat(exitDate.value)
            travelClaimSubmitRequest.dateSickness =DateUtil.getDateTorequestFromat(dateOfSickness.value)
            travelClaimSubmitRequest.travelClaimAttachmentModels=travelClaimAttachmentModel

            userLiveData?.processing()
            var loginService = UserService.create(getApplication<Application>(), true)
            val subscribe =
                    loginService?.AddTravelClaim(travelClaimSubmitRequest)?.observeOn(
                            AndroidSchedulers.mainThread()
                    )
                            ?.subscribeOn(
                                    Schedulers.io()
                            )
                            ?.subscribe({
                                if (it.isSuccessful) {
                                    if (it.body()?.isSuccess!!) {
                                        errors.uiUpdate = true
                                        userLiveData?.claimSuccess()
                                    } else {
                                        errors.uiUpdate = false
                                        userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                                    }
                                } else {
                                    if (it.code() == Constants.UNAUTH_ERROR) {
                                        pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                        pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                        userLiveData?.sessionExpired()
                                    }
                                } // this will tell you why your api doesnt work most of time

                            }, { error ->
                                errors.uiUpdate = false
                                userLiveData?.postError(ErrorData(100, null))

                            })
        }
    }

    fun uploadImage(encoded: String) {
        userLiveData?.processing()
        var imageUpload = ImageUpload()
        imageUpload.fileTypeExtension= Constants.FILE_FORMAT
        imageUpload.imageData=encoded
        imageUpload.module = FileUploadModules.TRAVEL.value
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
                                    isDocumentUploaded.postValue(true)
                                    var travelClaimAttachmentModelItem = TravelClaimAttachmentModel()
                                    travelClaimAttachmentModelItem.claimFilePath =it.body()?.Data?.relativePath
                                    travelClaimAttachmentModel.add(travelClaimAttachmentModelItem)
                                    userLiveData?.responseSuccess(it.body()?.messageStatus)
                                } else {
                                    userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                                }
                            } else if (it.code() == Constants.UNAUTH_ERROR) {
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY, null)
                                userLiveData?.sessionExpired()
                            }

                        }, { error ->
                            userLiveData?.postError(ErrorData(100, null))
                            error.printStackTrace()
                        })
    }
}