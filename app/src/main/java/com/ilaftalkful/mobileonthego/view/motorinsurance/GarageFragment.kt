package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.GarageFragmentBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.Garage
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import java.util.*

class GarageFragment : IlafBaseFragment(),OnItemClickListener {
    val viewModel: GarageViewModel by viewModels()
    var garageAdapter: GarageListAdapter?=null
    lateinit var   garageFragmentBinding:GarageFragmentBinding
    var navController: NavController?=null

    var isFromMotorClaim:Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isFromMotorClaim = arguments?.getBoolean(Constants.ISFROM_MOTOR_CLAIM)
        }
    }
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(isFromMotorClaim!!) {
                findNavController().navigate(R.id.action_show_motor_claim)
            }else{
                findNavController().popBackStack()
            }
        }
    }



    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         garageFragmentBinding = DataBindingUtil.inflate<GarageFragmentBinding>(inflater,R.layout.garage_fragment, container, false)
        garageFragmentBinding.lifecycleOwner=this
        garageFragmentBinding.viewModel=viewModel
        garageFragmentBinding.fragment=this
        garageFragmentBinding.label = getString(R.string.garage)
        return garageFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        setAdapter()
        if(Utility.checkInternetConnection(requireActivity())){
            viewModel.getGarageList()
        }
        viewModel.searchKey.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                garageAdapter?.setDataValues(viewModel.garageRespone.value?.garages ?: emptyList())
            }else{
                if (viewModel.garageRespone.value?.garages != null) {
                    var data :List<Garage>?=null
                    if(viewModel.selectedOption.value !=0) {
                         data =
                            viewModel.garageRespone.value?.garages?.filter { it ->
                                it.garageTypeID == viewModel.selectedOption.value && (
                                        it.garageName?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString()) ||
                                        it.garageRegion?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString())) ||
                                        it.phoneNumber?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString()) ||
                                        it.emailID?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString())
                            }
                    }else{
                        data = viewModel.garageRespone.value?.garages?.filter { it ->
                                (it.garageName?.contains(viewModel.searchKey.value.toString()) == true ||
                                        it.garageName?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString()) ||
                                        it.garageRegion?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString())) ||
                                        it.phoneNumber?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString()) ||
                                        it.emailID?.toLowerCase(Locale.getDefault()).toString().contains(viewModel.searchKey.value?.toLowerCase(Locale.getDefault()).toString())
                        }
                    }
                    garageAdapter?.setDataValues(
                        data ?: emptyList()
                    )
                }
            }
        })

        viewModel.garageRespone.observe(viewLifecycleOwner, Observer {
            garageAdapter?.setDataValues(it.garages?: emptyList())
        })

        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when(it.getStatus()){
                UserData.UserStatus.CLICKED->{
                    (activity as SplashActivity).hideKeyboard()
                }
                UserData.UserStatus.RESPOSNSE_SUCCESS->{
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED->{
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR->{
                    (activity as SplashActivity).toggleLoader(false)
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    showMsg(error?.errorMessage?:getString(R.string.something_went_wrong))
                }
                UserData.UserStatus.SESSION_EXPIRED->{
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })
    }

        override fun moveToLogin() {
            val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
            navController?.navigate(R.id.login_fragment,bundle)

        }

    private fun setAdapter() {
        garageAdapter = GarageListAdapter()
        garageAdapter?.onClick=this
        garageFragmentBinding.rvGarageList.adapter = garageAdapter
    }

    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }
    override fun onResume() {
        super.onResume()
        if (viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY) == null) {
            viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY, IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        }
        if (viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)
                        .equals(IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        )
            viewModel.isENS.postValue(true)
        else
            viewModel.isENS.postValue(false)
    }

    override fun onEnglishClicked(view: View) {
        viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY,IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        viewModel.isENS.postValue(true)
        refreshConfigChange()
    }

    override fun onArabicClicked(view: View) {
        viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY,IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)
        viewModel.isENS.postValue(false)
        refreshConfigChange()
    }

    fun onRoadAssistantClick(view: View){
        if(viewModel.selectedOption.value==3) {
            viewModel.selectedOption.postValue(0)
            garageAdapter?.setDataValues(viewModel.garageRespone.value?.garages?: emptyList())
        }else {
            var data = viewModel.garageRespone.value?.garages?.filter {it->it.garageTypeID == 3  }
            garageAdapter?.setDataValues(data?: emptyList())
            viewModel.selectedOption.postValue(3)
        }
    }

    fun onAgencyClick(view: View){
        if(viewModel.selectedOption.value==1) {
            viewModel.selectedOption.postValue(0)
            garageAdapter?.setDataValues(viewModel.garageRespone.value?.garages ?: emptyList())
        }else {
            var data = viewModel.garageRespone.value?.garages?.filter {it->it.garageTypeID == 1  }
            garageAdapter?.setDataValues(data?: emptyList())
            viewModel.selectedOption.postValue(1)
        }
    }

    fun onOutSideClick(view: View){
        if(viewModel.selectedOption.value==2){
            viewModel.selectedOption.postValue(0)
        garageAdapter?.setDataValues(viewModel.garageRespone.value?.garages?: emptyList())
        }else {
            var data = viewModel.garageRespone.value?.garages?.filter {it->it.garageTypeID == 2  }
            garageAdapter?.setDataValues(data?: emptyList())
            viewModel.selectedOption.postValue(2)
        }

    }

    override fun onContctNumberClicked(view: View) {
        var textViw = view as TextView
        viewModel.phoneNoText.postValue(textViw.text.toString())
        var isCallPermission = Utility.hasPermissions(requireContext())
        if(isCallPermission) {
            makePhoneCall()
        }else{
            requestPermissions(Utility.permissonList, Constants.PHONE_CALL)
        }
    }

    override fun makePhoneCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + viewModel.phoneNoText.value.toString())
        startActivity(callIntent)
    }

    override fun onItemClick(v: View, obj: Any) {
        onContctNumberClicked(v)
    }


}