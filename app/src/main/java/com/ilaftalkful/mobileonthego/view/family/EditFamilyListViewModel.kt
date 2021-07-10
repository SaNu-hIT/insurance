package com.ilaftalkful.mobileonthego.view.family

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.TravelPolicyType
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.model.family.FamilyErros
import com.ilaftalkful.mobileonthego.model.family.FamilyMemberId
import com.ilaftalkful.mobileonthego.model.family.FamilyParameter
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditFamilyListViewModel(application: Application) : IlafBaseViewModel(application) {
    // TODO: Implement the ViewModel
    lateinit var userLiveData: UserLiveUpdate
    var mfullName = MutableLiveData<String>("")
    var mpassportNumber = MutableLiveData<String>("")
    var mRelationID = MutableLiveData<Int>()
    var familyMemberID = MutableLiveData<String>()
    var mDOB = MutableLiveData<String>("")
    var isFamilyListEmpty:MutableLiveData<Boolean> =MutableLiveData<Boolean>(false)
    var dateOfBirth: MediatorLiveData<String> = MediatorLiveData<String>().apply {
        addSource(mDOB) {
            if (it.isEmpty()) {
                if (!mDOB.value?.isEmpty()!!) {
                    value = mDOB.value
                }
            } else {
                value = mDOB.value
            }
        }
    }

    val relationType: ObservableArrayList<String> = ObservableArrayList<String>()
    val relationId = MutableLiveData<Int>()
    var relationList: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var actualrelationList = ArrayList<TravelPolicyType>()
    var selectedrelationId= MutableLiveData<Int>(0)
    var selectedAddFamilyrelationId= MutableLiveData<Int>(0)


    fun postFamilyCreate(errors: FamilyErros, datum: FamilyParameter, isEdit: Boolean) {

        if (null != datum.fullName && null != datum.passportNumber && null != mDOB.value) {
            mfullName.value = datum.fullName
            mpassportNumber.value = datum.passportNumber
            mRelationID.value = datum.relationID
            familyMemberID.value = datum.familyMemberID
              if (IlafValidator.isNullOrEmpty(errors.mFullNameError)
                && IlafValidator.isNullOrEmpty(errors.mPassportNumberError) && IlafValidator.isNullOrEmpty(
                    errors.mRelationIDError
                )
                && IlafValidator.isNullOrEmpty(errors.mDOBNameError)

            ) {
                errors.uiUpdate = true
                var qoute = FamilyParameter()
                qoute.fullName = mfullName.value
                qoute.relationID = datum.relationID
                  if(isEdit){
                      qoute.dob = DateUtil.getDateTorequestFromaForEditFamily(datum.dob)
                  }else {
                      qoute.dob = DateUtil.getDateToProfile(mDOB.value)
                  }
                qoute.passportNumber = mpassportNumber.value
                qoute.familyMemberID = datum.familyMemberID
                userLiveData.processing()
                var corperateService = UserService.create(getApplication<Application>(), true)
                val subscribe =
                    corperateService?.postFamilyCreate(qoute)?.observeOn(
                        AndroidSchedulers.mainThread()
                    )
                        ?.subscribeOn(
                            Schedulers.io()
                        )
                        ?.subscribe({
                            if (it.isSuccessful) {
                                if (it.body()?.isSuccess!!) {
                                    errors.uiUpdate = false
                                    if(isEdit){
                                        userLiveData.editFamilySuccess()
                                    }else{
                                        userLiveData.addFamilySuccess()
                                    }


                                } else {
                                    userLiveData?.postError(ErrorData(it.code(), it.message()))
                                }

                            } else if (it.code() == Constants.UNAUTH_ERROR) {
                                ilafPreference.setBooleanPrefValue(
                                    IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                    false
                                )
                                ilafPreference.setStringPrefValue(
                                    IlafSharedPreference.Constants.TOKEN_KEY,
                                    null
                                )
                                userLiveData?.sessionExpired()
                            } else {
                                userLiveData?.postError(ErrorData(it.code(), it.message()))
                            }


                        }, { error ->
                            userLiveData.postError(ErrorData(100, "Unknown Error...!!!"))
                            error.printStackTrace()
                        })
            }
        }
    }

    var isValidUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    lateinit var ilafPreference: IlafSharedPreference
    val logRespone = MutableLiveData<ArrayList<Datum>>()

    init {
        ilafPreference = IlafSharedPreference(application)
        isValidUser.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))
        ilafPreference = IlafSharedPreference(application)
        isENS = MutableLiveData<Boolean>(false)
        if (ilafPreference.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY) != null && pref.getStringPrefValue(
                IlafSharedPreference.Constants.LANGUAGE_KEY
            ).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)
        ) {
            isENS.postValue(false)
        } else {
            isENS.postValue(true)
        }
        userLiveData = UserLiveUpdate()
    }

    fun clearAll() {
        mfullName.value = ""
        mpassportNumber.value = ""
        mDOB.value = ""
        mRelationID.value = 0
    }

    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(mpassportNumber) {
            value = it.toString().isNotEmpty() && mfullName.value.toString()
                .isNotEmpty()

                    && mRelationID.value.toString().isNotEmpty()
        }
        addSource(mfullName) {
            value = it.toString().isNotEmpty() && mpassportNumber.value.toString().isNotEmpty()

                    && mRelationID.value.toString().isNotEmpty()
        }

        addSource(mRelationID) {
            value = it.toString().isNotEmpty() && mpassportNumber.value.toString().isNotEmpty()

                    && mfullName.value.toString().isNotEmpty()
        }

    }


    fun getFamilyist() {
        userLiveData.processing()
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
                            if( it.body()?.Data.isNullOrEmpty()){
                                isFamilyListEmpty.postValue(true)
                            }
                            userLiveData.familyListSUccess()
                            clearAll()
                            logRespone.postValue(it.body()?.Data)
                        }else{
                            isFamilyListEmpty.postValue(true)
                            userLiveData.postError(ErrorData(it.code(),it.body()?.messageStatus))
                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            ilafPreference.setBooleanPrefValue(
                                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                false
                            )
                            ilafPreference.setStringPrefValue(
                                IlafSharedPreference.Constants.TOKEN_KEY,
                                null
                            )
                            userLiveData?.sessionExpired()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    isFamilyListEmpty.postValue(true)
                    userLiveData.postError(ErrorData(100, "Unknown Error...!!!"))
                    error.printStackTrace()
                })
    }

    fun updateData(obj: Any) {
        if (obj is Datum) {
            var logArray = logRespone.value
            if (logArray != null) {
                for (log in logArray) {
                    if(log.isSelected){
                        log.isSelected=false
                    }else {
                        log.isSelected = log.familyMemberID == obj.familyMemberID
                    }
                }
            }
            logRespone.postValue(logArray)
        }
    }


    fun familyMemberDelete(obj: Datum) {
        userLiveData.processing()
        var familyMemberId = FamilyMemberId()
        familyMemberId.familyMemberID = obj.familyMemberID.toString()
        var corperateService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            corperateService?.postFamilyDelete(familyMemberId.familyMemberID)?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful) {
                        if (it.body()?.isSuccess!!) {
                            userLiveData.responseSuccess()
                        } else {
                            userLiveData.postError(ErrorData(it.code(), it.message()))
                        }
                    } else {
                        if (it.code() == Constants.UNAUTH_ERROR) {
                            ilafPreference.setBooleanPrefValue(
                                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                                false
                            )
                            ilafPreference.setStringPrefValue(
                                IlafSharedPreference.Constants.TOKEN_KEY,
                                null
                            )
                            userLiveData?.sessionExpired()
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    userLiveData.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }

    fun editFamily(obj: FamilyParameter) {

        var erros = FamilyErros()
        postFamilyCreate(erros, obj,true)
    }

    fun relationShips() {
        userLiveData?.processing()
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
                                    userLiveData?.responseSuccess()
                                    processRelationData(it?.body()?.Data)

                                } else {

                                    userLiveData?.postError(ErrorData(it.code(), it.message()))

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


}