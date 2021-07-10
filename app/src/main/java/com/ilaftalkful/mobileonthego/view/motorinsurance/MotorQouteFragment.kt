package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.MotorQouteFragmentBinding
import com.ilaftalkful.mobileonthego.model.BasicErros
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.MotorQouteViewModel

class MotorQouteFragment : IlafBaseFragment() {

    val viewModel: MotorQouteViewModel by viewModels()
    lateinit var motorQouteFragmentBinding: MotorQouteFragmentBinding
    lateinit var navController:NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         motorQouteFragmentBinding = DataBindingUtil.inflate<MotorQouteFragmentBinding>(inflater,R.layout.motor_qoute_fragment, container, false)
        motorQouteFragmentBinding.lifecycleOwner=this
        motorQouteFragmentBinding.viewModel=viewModel
        motorQouteFragmentBinding.fragment=this
        motorQouteFragmentBinding.label = getString(R.string.quote)
        motorQouteFragmentBinding.error = BasicErros(null)
        return motorQouteFragmentBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel. getManufaturingYears()
        viewModel.getcarMakeData()
        viewModel.getcarTypeData()
        viewModel.getcarModelData()

       /* viewModel.selectYear.observe(viewLifecycleOwner, Observer {
        })
        viewModel.selectCarMake.observe(viewLifecycleOwner, Observer {
        })
        viewModel.selectCarType.observe(viewLifecycleOwner, Observer {
        })*/

       // obsever()
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                MotorLiveData.UserStatus.RESPOSNSE_SUCCESS -> {
                //    if(it?.statusMessage!=null)
//                    showMsg(it?.statusMessage!!)
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
                    IlafCommonAlert(requireActivity(),error?.getErrorMessage()?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
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
                    showMsg(error?.getErrorMessage() ?: getString(R.string.something_went_wrong))
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.SESSION_EXPIRED->{
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.QUOTE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(requireActivity(),it.statusMessage,getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                            navController?.popBackStack()
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                }
            }
        })


    }

    override fun moveToLogin() {
        val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
        navController.navigate(R.id.login_fragment,bundle)

    }
    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    fun onSubmit(view: View,error: BasicErros){
        if(Utility.checkInternetConnection(requireContext())){
            error.sumInsuredError = IlafValidator.isValidSum(viewModel.sumInsured.value!!,requireActivity())

             if(viewModel.selectYear.value==0){
                IlafCommonAlert(requireActivity(),"Choose Year",null,getString(R.string.ok),null,null).show()
            } else  if(viewModel.selectCarMake.value==0){
                 IlafCommonAlert(requireActivity(),"Choose Make",null,getString(R.string.ok),null,null).show()
             } else if(viewModel.selectCarType.value==0){
                IlafCommonAlert(requireActivity(),"Choose Type",null,getString(R.string.ok),null,null).show()
            } else if(viewModel.selectCarModel.value==0){
                IlafCommonAlert(requireActivity(),"Choose Model",null,getString(R.string.ok),null,null).show()
            }
            if(viewModel.selectYear.value?:0>0 &&  viewModel.selectCarMake.value?:0>0 && viewModel.selectCarType.value?:0 >0
                    && viewModel.selectCarModel.value?:0>0 && error.sumInsuredError.isNullOrEmpty()) {
                viewModel.callMotorQuote(error)
            }
        }else{
            IlafCommonAlert(requireActivity(),getString(R.string.no_interbnet),null,getString(R.string.ok),null,null).show()
        }
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
    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }
}