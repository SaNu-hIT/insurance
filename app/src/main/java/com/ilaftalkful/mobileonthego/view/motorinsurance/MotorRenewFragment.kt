package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.MotorRenewFragmentBinding
import com.ilaftalkful.mobileonthego.model.TravelClaimErrors
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.MotorRenewViewModel

class MotorRenewFragment : IlafBaseFragment() {


    val viewModel: MotorRenewViewModel by viewModels()
    lateinit var    motorRenewFragmentBinding:MotorRenewFragmentBinding
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        motorRenewFragmentBinding = DataBindingUtil.inflate<MotorRenewFragmentBinding>(inflater,R.layout.motor_renew_fragment, container, false)
        motorRenewFragmentBinding.lifecycleOwner=this
        motorRenewFragmentBinding.viewModel=viewModel
        motorRenewFragmentBinding.fragment=this
        motorRenewFragmentBinding.error= TravelClaimErrors()
        motorRenewFragmentBinding.label =  getString(R.string.renew)
        return motorRenewFragmentBinding.root
    }
    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPolicyList()
        viewModel.getCarTypeList()

        viewModel.policyId.observe(viewLifecycleOwner, Observer {
                viewModel.PopulateData(it)
        })
        viewModel.isHomeDeliver.observe(viewLifecycleOwner, Observer {
            viewModel.addHomeDeliverCalue()
        })
        viewModel.isLiability.observe(viewLifecycleOwner, Observer {
            viewModel.addLiabilityValue()
        })
        viewModel.isUpgrade.observe(viewLifecycleOwner, Observer {
            viewModel.addUpgradeValue()
        })
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                MotorLiveData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                MotorLiveData.UserStatus.ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    showMsg(error?.getErrorMessage() ?: "Something went wrong")
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.YEAR_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.YEAR_ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    showMsg(error?.getErrorMessage() ?: "Something went wrong")
                      (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.SESSION_EXPIRED->{
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.RENEW_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    val bundle = bundleOf(Constants.PAYMENT_URL to it.statusMessage)
                    navController?.navigate(R.id.action_payment_webview, bundle)

                }
            }
        })
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
    fun onRenewPolicy(view: View,errors: TravelClaimErrors){
        viewModel.renawPolicy(errors)
    }
}