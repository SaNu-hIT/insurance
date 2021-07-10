package com.ilaftalkful.mobileonthego.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.ilaftalkful.mobileonthego.model.ErrorData
import com.ilaftalkful.mobileonthego.model.UserLiveUpdate
import com.ilaftalkful.mobileonthego.model.deptcontact.Datum
import com.ilaftalkful.mobileonthego.retrofit.UserService
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContactViewModel(application: Application) : IlafBaseViewModel(application){
    val contactResponce= MutableLiveData<ArrayList<Datum>>()
    internal var userLiveData: UserLiveUpdate? = null
    val departmentName: ObservableArrayList<String> = ObservableArrayList<String>()
    val departmentIDs: ObservableArrayList<Int> = ObservableArrayList<Int>()
    val depatSeletId = MutableLiveData<Int>()
    var isGusetLogin: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    lateinit var ilafPreference: IlafSharedPreference

    fun getContachDetails() {
        userLiveData?.processing()
        var corperateService = UserService.create(getApplication<Application>(), true)
        val subscribe =
            corperateService?.DepartmentContactList()?.observeOn(
                AndroidSchedulers.mainThread()
            )
                ?.subscribeOn(
                    Schedulers.io()
                )
                ?.subscribe({
                    if (it.isSuccessful() ){
                        if (it.body()?.isSuccess!!) {
                            userLiveData?.responseSuccess()
                            contactResponce.postValue(it.body()?.Data?.filter { it->it.ShownInContactUs() ==1 } as ArrayList<Datum>)
                            processResponce(it.body()?.Data?.filter { it->it.ShownInContactUs()==1 } as ArrayList<Datum>)
                        } else {
                            userLiveData?.postError(ErrorData(it.code(), it.body()?.messageStatus))
                        }
                    }else {
                        if(it.code()== Constants.UNAUTH_ERROR){
                            ilafPreference.setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)
                            ilafPreference.setStringPrefValue(IlafSharedPreference.Constants.TOKEN_KEY,null)
                            userLiveData?.sessionExpired()
                        }else {
                            userLiveData?.postError(ErrorData(it.code(), it.message()))
                        }
                    }

                }, { error ->
                    userLiveData?.postError(ErrorData(100, null))
                    error.printStackTrace()
                })
    }




    init {
            userLiveData = UserLiveUpdate()
        ilafPreference = IlafSharedPreference(application)

        isGusetLogin.postValue(ilafPreference.getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER))


        if(pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null && pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)){
            isENS.postValue(false)
        }else {
            isENS.postValue(true)
        }
    }


private fun processResponce(data: ArrayList<Datum>?) {
    var depatmentName = ArrayList<String>()
    var depatmenId = ArrayList<Int>()
    departmentName.clear()
    departmentIDs.clear()
    if (data != null) {
        for(item in data){
            var name= String()
            var id: Int
            name = item.departmentName.toString()
            id = item.departmentID!!.toInt()
            depatmentName.add(name)
            depatmenId.add(id)
        }
    }
    departmentName.addAll(depatmentName)
    departmentIDs.addAll(depatmenId)
}





}