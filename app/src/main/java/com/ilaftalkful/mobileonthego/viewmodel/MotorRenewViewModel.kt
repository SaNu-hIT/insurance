package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveUpdate
import com.ilaftalkful.mobileonthego.model.mototqoute.cartype.CarType
import com.ilaftalkful.mobileonthego.model.regster.MotorPolicies
import com.ilaftalkful.mobileonthego.model.regster.PolicyList
import com.ilaftalkful.mobileonthego.model.regster.ServiceAddonsList
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MotorRenewViewModel(application: Application) : IlafBaseViewModel(application) {

    val policyNo = MutableLiveData<String>("")
    val expiryDate = MutableLiveData<String>("")
    val carType = MutableLiveData<String>("")
    val amount = MutableLiveData<String>("")
    val manufacturingYear = MutableLiveData<String>("")
    val homeDelivery = MutableLiveData<String>("")
    val upgarde = MutableLiveData<String>("")
    val liability = MutableLiveData<String>("")
    var deliveryAmount = MutableLiveData<String>()
    var upgradeAmount = MutableLiveData<String>()
    var liabilityAmount = MutableLiveData<String>()
    lateinit var context: Context
    internal var userLiveData: MotorLiveUpdate? = null
    var policiesList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualPolicyList:List<PolicyList> = ArrayList<PolicyList>()
    var serviceList:List<ServiceAddonsList> = ArrayList<ServiceAddonsList>()
    val isTermsChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isHomeDeliver: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isLiability: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isUpgrade: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val totalAmount = MutableLiveData<String>("")
    var renewPolicy  = MotorPolicyRenewalDetails ()
    var policyId = MutableLiveData<Int>()
    val carTypeList = MutableLiveData<List<CarType>> ()
    val selectCarType = MutableLiveData<Int>()
    val carTypes : ObservableArrayList<String> = ObservableArrayList<String>()
    var termsAndConditions: MutableLiveData<String> = MutableLiveData<String>("")

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
                            actualPolicyList = it?.body()?.Data?.policyList!!
                            serviceList = it.body()?.Data?.serviceAddonsList!!
                            processResult(it.body()?.Data!!)
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                        }
                    } else if (it.code() == Constants.UNAUTH_ERROR) {
                        pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER, false)
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

    private fun processResult(data: MotorPolicies) {
        var policyList = ArrayList<String>()
        policiesList.value?.clear()
        if(data.policyList!=null) {
            for (item in data?.policyList!!) {
                policyList.add(item.policyNumber.toString())
            }
            policiesList.postValue(policyList)
        }

        var i:Int=0
       for(item in data?.serviceAddonsList!!){
           if(data?.serviceAddonsList!![0]!=null) {
               homeDelivery.postValue(data?.serviceAddonsList!![0]?.addonDescription)
               deliveryAmount.postValue(data?.serviceAddonsList!![0]?.addonAmount.toString())
           }
           if(data?.serviceAddonsList!![1]!=null) {
               upgarde.postValue(data?.serviceAddonsList!![1]?.addonDescription)
               upgradeAmount.postValue(data?.serviceAddonsList!![1]?.addonAmount.toString())
           }
           if(data?.serviceAddonsList!![2]!=null) {
               liability.postValue(data?.serviceAddonsList!![2]?.addonDescription)
               liabilityAmount.postValue(data?.serviceAddonsList!![2]?.addonAmount.toString())
           }

        }

    }

    var isGusetLogin: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
     var ilafPreference: IlafSharedPreference

    init {
        ilafPreference = IlafSharedPreference(application)
        isENS  = MutableLiveData<Boolean>(false)
        userLiveData = MotorLiveUpdate()
        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null
            && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
        isGusetLogin.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))
        termsAndConditions.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.MOTOR_TC))
    }


    fun renawPolicy(errors: TravelClaimErrors) {
        userLiveData?.buttonClicked()
        errors.termsAndConditionsError = if(!isTermsChecked.value!!) getApplication<Application>().getString(R.string.check_terms_and_conditions) else null
        isVisible.postValue(!isTermsChecked.value!!)
        if(IlafValidator.isNullOrEmpty( errors.termsAndConditionsError ) && selectCarType.value!=-1){
            var data = MotorPolicyRenewalDetails()
            if(actualPolicyList!=null && policyId.value!=null)
            renewPolicy.motorPolicyID =actualPolicyList.get(policyId.value!!).motorPolicyID
            renewPolicy.policyNumber =actualPolicyList.get(policyId.value!!).policyNumber
            renewPolicy.expiryDate = DateUtil.getDateTorequestFromat(expiryDate.value)
            renewPolicy.carTypeID = carTypeList.value?.get(selectCarType.value!!)?.id?.toInt() ?:0
            renewPolicy.renewalAmountFinal = getFinalAmount(actualPolicyList.get(policyId.value!!).amount)
            renewPolicy.renewalAmountPremium = actualPolicyList.get(policyId.value!!).amount
           var renewPolicyRequest = RenewPolicyRequest()
            renewPolicyRequest.motorPolicyRenewalDetails=renewPolicy
            var motorPolicyRenewalPlans =  ArrayList<MotorPolicyRenewalPlan>()
            if(isHomeDeliver.value!!){
                var data = MotorPolicyRenewalPlan()
                data.addonID = serviceList.get(0).addonID
                motorPolicyRenewalPlans.add(data)
            }
            if(isUpgrade.value!!){
                var data = MotorPolicyRenewalPlan()
                data.addonID = serviceList.get(1).addonID
                motorPolicyRenewalPlans.add(data)
            }
            if(isLiability.value!!){
                var data = MotorPolicyRenewalPlan()
                data.addonID = serviceList.get(2).addonID
                motorPolicyRenewalPlans.add(data)
            }
            renewPolicyRequest.motorPolicyRenewalPlans= motorPolicyRenewalPlans
            userLiveData?.processing()
            var loginService = UserService.create(getApplication<Application>(), true)
            val subscribe =
                loginService?.AddMotorPolicyRenewal(renewPolicyRequest)?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                    ?.subscribeOn(
                        Schedulers.io()
                    )
                    ?.subscribe({
                        if (it.isSuccessful) {
                            if(it.body()?.isSuccess!!) {
                                errors.uiUpdate = true
                                userLiveData?.renewSuccess(it?.body()?.messageStatus)
                            } else{
                                errors.uiUpdate = false
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        } else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        } // this will tell you why your api doesnt work most of time

                    }, { error ->
                        errors.uiUpdate = false
                        userLiveData?.postError(ErrorData(100, null))
                        error.printStackTrace()

                    })
        }

    }

    private fun getFinalAmount(amount: Int?): Int? {
        var totalAmount:Int = amount?:0
        if(isUpgrade.value!!){
            totalAmount= ((totalAmount.toDouble()).plus(upgradeAmount.value?.toDouble()?:0.0).toInt())

        }
        if(isHomeDeliver.value!!){
            totalAmount= ((totalAmount.toDouble()).plus(deliveryAmount.value?.toDouble()?:0.0).toInt())

        }
        if(isLiability.value!!){
            totalAmount= ((totalAmount.toDouble()).plus(liabilityAmount.value?.toDouble()?:0.0).toInt())

        }

        return totalAmount
    }

    fun getCarTypeList() {
        userLiveData?.processing()
        var loginService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            policyId?.let {
                loginService?.getMotorCarTypes()?.observeOn(
                    AndroidSchedulers.mainThread()
                )
                    ?.subscribeOn(
                        Schedulers.io()
                    )
                    ?.subscribe({
                        if (it.isSuccessful) {
                            if(it.code()==200) {
                                userLiveData?.responseSuccess(it?.body()?.messageStatus)
                                carTypes.clear()
                                carTypeList.postValue(it?.body()?.Data)
                                for(item in it?.body()?.Data!!){
                                    carTypes.add(item.text)
                                }
                            } else{
                                userLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        } else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.message()))
                            }
                        } // this will tell you why your api doesnt work most of time

                    }, { error ->
                        userLiveData?.postError(ErrorData(100, null))
                        error.printStackTrace()

                    })
            }
    }


    fun PopulateData(it: Int) {
        expiryDate.postValue(actualPolicyList.get(it).expiryDate)
        carType.postValue(actualPolicyList.get(it).carType)
        amount.postValue(actualPolicyList.get(it).amount.toString())
        manufacturingYear.postValue(actualPolicyList.get(it).manufacturingYear)
        expiryDate.postValue(DateUtil.getStringDateToFromat(actualPolicyList.get(it).expiryDate))
        totalAmount.postValue(actualPolicyList.get(it).amount.toString())
    }

    fun addHomeDeliverCalue() {
        if(!totalAmount.value.isNullOrEmpty()) {
            if (isHomeDeliver.value!!) {
                totalAmount.postValue((totalAmount.value!!.toDouble()).plus(deliveryAmount.value?.toDouble()?:0.0).toString())
            } else {
                totalAmount.postValue((totalAmount.value!!.toDouble()).minus(deliveryAmount.value?.toDouble()?:0.0).toString())
            }
        }
    }

    fun addUpgradeValue() {
        if(!totalAmount.value.isNullOrEmpty()) {
            if (isUpgrade.value!!) {
                totalAmount.postValue((totalAmount.value!!.toDouble()).plus(upgradeAmount.value?.toDouble()?:0.0).toString())
            } else {
                totalAmount.postValue((totalAmount.value!!.toDouble()).minus(upgradeAmount.value?.toDouble()?:0.0).toString())
            }
        }
    }

    fun addLiabilityValue() {
        if(!totalAmount.value.isNullOrEmpty()) {
            if (isLiability.value!!) {
                totalAmount.postValue((totalAmount.value!!.toDouble()).plus(liabilityAmount.value?.toDouble()?:0.0).toString())
            } else {
                totalAmount.postValue((totalAmount.value!!.toDouble()).minus(liabilityAmount.value?.toDouble()?:0.0).toString())
            }
        }
    }
}