package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.*
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.BasicErros
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.ManufacturingYears
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveUpdate
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorQouteParameter
import com.ilaftalkful.mobileonthego.model.mototqoute.carmodel.CarModel
import com.ilaftalkful.mobileonthego.model.mototqoute.cartype.CarMakes
import com.ilaftalkful.mobileonthego.model.mototqoute.cartype.CarType
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MotorQouteViewModel(application: Application) : IlafBaseViewModel(application) {
    lateinit var ilafPreference: IlafSharedPreference
    val carModellist =  MutableLiveData<List<CarModel>> ()
    val carMakeList = MutableLiveData<List<CarMakes>> ()
    val carTypeList = MutableLiveData<List<CarType>> ()
    val carModel : ObservableArrayList<String> = ObservableArrayList<String>()
    val carMake : ObservableArrayList<String> = ObservableArrayList<String>()
    val carType : ObservableArrayList<String> = ObservableArrayList<String>()
    val manufacturingYearsList  : ObservableArrayList<String> = ObservableArrayList<String>()
    val manufacturingYears  : MutableLiveData<List<ManufacturingYears>> =   MutableLiveData<List<ManufacturingYears>> ()
    internal var userLiveData: MotorLiveUpdate? = null
    var isValidData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isTermsChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val sumInsured = MutableLiveData<String>("")
    val selectYear = MutableLiveData<Int>()
    val selectCarMake = MutableLiveData<Int>()
    val selectCarModel = MutableLiveData<Int>()
    val selectCarType = MutableLiveData<Int>()
    lateinit var context: Context
    var termsAndConditions: MutableLiveData<String> = MutableLiveData<String>("")

    init {
        ilafPreference = IlafSharedPreference(application)
        userLiveData = MotorLiveUpdate()
        context =application
        isValidData.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))
        termsAndConditions.postValue(pref.getStringPrefValue(IlafSharedPreference.Constants.MOTOR_TC))

    }



    fun getManufaturingYears() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.getManufacturingYear()?.observeOn(
                    AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                        Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            manufacturingYears.postValue(it?.body()?.Data)
                            processResult(it.body()?.Data ?: emptyList())
                            userLiveData?.yearSuccess()
                        } else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        }
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }

    private fun processResult(data: List<ManufacturingYears>) {
        var yearList = ArrayList<String>()
        manufacturingYearsList.clear()
        yearList.add("")
        for(item in data){
        var year= String()
            year = item.manufacturingYear.toString()
            yearList.add(year)
        }
        manufacturingYearsList.addAll(yearList)
    }


    fun getcarModelData() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.getMotorCarModels()?.observeOn(
                    AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                        Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            //userLiveData?.responseSuccess(it?.body()?.messageStatus)
                            carModel.clear()
                            carModel.add("")
                            carModellist.postValue(it?.body()?.Data)
                            for(item in it?.body()?.Data!!){
                                carModel.add(item.text)
                            }
                            userLiveData?.responseSuccess()
                        }else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        }
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }

    fun getcarMakeData() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.getMotorCarMakes()?.observeOn(
                    AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                        Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                            //userLiveData?.responseSuccess(it?.body()?.messageStatus)
                            carMake.clear()
                            carMake.add("")
                            carMakeList.postValue(it?.body()?.Data)
                            for(item in it?.body()?.Data!!){
                                carMake.add(item.text)
                            }
                            userLiveData?.responseSuccess()
                        }else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        }
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }
    fun getcarTypeData() {
        userLiveData?.processing()
        var dashBoardService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            dashBoardService?.getMotorCarTypes()?.observeOn(
                    AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                        Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful()) {
                        if (it.body()?.isSuccess!!) {
                           // userLiveData?.responseSuccess(it?.body()?.messageStatus)
                            carType.clear()
                            carType.add("")
                            carTypeList.postValue(it?.body()?.Data)
                            for(item in it?.body()?.Data!!){
                                carType.add(item.text)
                            }
                            userLiveData?.responseSuccess()
                        }  else {
                            if(it.code()== Constants.UNAUTH_ERROR){
                                pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                userLiveData?.sessionExpired()
                            }else {
                                userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                            }
                        }
                    } else {
                        userLiveData?.postError(ErrorData(it.code(), it.message()))
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }

    fun callMotorQuote(errors: BasicErros) {
        isVisible.postValue(!isTermsChecked.value!!)

        if(IlafValidator.isNullOrEmpty(errors.sumInsuredError)
                && isTermsChecked.value!!){
            errors.uiUpdate=true
            var motorQuote = MotorQouteParameter()
            motorQuote.carMakeID= carMakeList.value?.get(selectCarMake.value?:-1 )?.id
            motorQuote.manufacturingYearID= manufacturingYears.value?.get((selectYear.value?:-1) )?.manufacturingYearID
            motorQuote.carTypeID= carTypeList.value?.get((selectCarType.value?:-1 ))?.id
            motorQuote.carModelID= carModellist.value?.get(selectCarModel.value ?:-1)?.id
            motorQuote.sumInsured=sumInsured.value
            userLiveData?.processing()
            var dashBoardService = UserService.create(getApplication<Application>(), true)
            val subscribe =
                    dashBoardService?.CreateMotorQuote(motorQuote)?.observeOn(
                            AndroidSchedulers.mainThread()
                    )
                            ?.subscribeOn(
                                    Schedulers.io()
                            )
                            ?.subscribe({
                                if (it.isSuccessful()) {
                                    if (it.body()?.isSuccess!!) {
                                        errors.uiUpdate = true
                                        userLiveData?.quoteSuccess(it?.body()?.messageStatus)
                                    }  else {
                                        if(it.code()== Constants.UNAUTH_ERROR){
                                            pref.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                                            pref.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                                            userLiveData?.sessionExpired()
                                        }else {
                                            userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                                        }
                                    }
                                } else {
                                    errors.uiUpdate = false
                                    userLiveData?.postError(ErrorData(it.code(), it.message()))
                                }

                            }, { error ->
                                errors.uiUpdate = false
                                userLiveData?.postError(ErrorData(100, null))
                                error.printStackTrace()
                            })
        }
    }

    var isValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(selectYear) {
            value= it!=0 && selectCarMake.value.toString().isNotEmpty()?:false
            selectCarModel.value.toString().isNotEmpty() && selectCarType.value.toString().isNotEmpty()?:false
            isTermsChecked.value?:false && sumInsured.value.toString().isNotEmpty()?:false
        }
        addSource(selectCarMake) {
            value= it!=0 && selectYear.value.toString().isNotEmpty()?:false
            selectCarModel.value.toString().isNotEmpty() && selectCarType.value.toString().isNotEmpty()?:false
            isTermsChecked.value?:false && sumInsured.value.toString().isNotEmpty()?:false
        }
        addSource(selectCarType) {
            value= it!=0 && selectYear.toString().isNotEmpty()?:false
            selectCarModel.toString().toString().isNotEmpty() && selectCarMake.toString().isNotEmpty()?:false
            isTermsChecked.value?:false && sumInsured.value.toString().isNotEmpty()?:false
        }
        addSource(selectCarModel) {
            value= it!=0 && selectYear.value.toString().isNotEmpty()?:false
            selectCarType.value.toString().isNotEmpty() && selectCarMake.value.toString().isNotEmpty()?:false
            isTermsChecked.value?:false && sumInsured.value.toString().isNotEmpty()?:false
        }
        addSource(isTermsChecked) {
            value= it?:false && selectYear.value.toString().isNotEmpty()?:false
            selectCarType.value.toString().isNotEmpty() && selectCarMake.value.toString().isNotEmpty()?:false
            && selectCarModel.value.toString().isNotEmpty()?:false && sumInsured.value.toString().isNotEmpty()?:false
        }
        addSource(sumInsured) {
            value= it.toString().isNotEmpty() && selectYear.value.toString().isNotEmpty()?:false
            selectCarType.value.toString().isNotEmpty() && selectCarMake.value.toString().isNotEmpty()?:false
                    && isTermsChecked.value!=0?:false     && selectCarModel.value.toString().isNotEmpty()?:false
        }

    }
}