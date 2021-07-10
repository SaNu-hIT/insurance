package com.ilaftalkful.mobileonthego.view.buyinsurance

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.LoginOrRegisterDialogBinding
import com.ilaftalkful.mobileonthego.databinding.TravelInsuranceFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.TravelInsuranceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelInsuranceFragment : IlafBaseFragment() {

    val viewModel: TravelInsuranceViewModel by viewModels()
    lateinit var travelInsuranceFragmentBinding :TravelInsuranceFragmentBinding

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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        travelInsuranceFragmentBinding = DataBindingUtil.inflate<TravelInsuranceFragmentBinding>(inflater,R.layout.travel_insurance_fragment, container, false)
        travelInsuranceFragmentBinding.lifecycleOwner=this
        travelInsuranceFragmentBinding.viewModel=viewModel
        travelInsuranceFragmentBinding.fragment=this
        travelInsuranceFragmentBinding.label = getString(R.string.travel_insurence)
        return travelInsuranceFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if(Utility.isLogedIn(requireContext())){
            if(Utility.checkInternetConnection(requireContext())){
                viewModel.setData()
            } else {
                IlafCommonAlert(requireActivity(),getString(R.string.no_interbnet),getString(R.string.ok),null,null).show()
            }
//        }
        viewModel.setAdapter()
        viewModel.data.observe(viewLifecycleOwner, Observer {
            if(it!=null)
            viewModel.travelPlansAdapter?.setPlansData(it)
        })
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when(it.getStatus()){
                UserData.UserStatus.RESPOSNSE_SUCCESS->{
                    (activity as SplashActivity).toggleLoader(false)

                }
                UserData.UserStatus.OPERATION_STARTED->{
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR->{
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED->{
                    dialog= IlafCommonAlert(requireActivity(),getString(R.string.session_expiry),getString(R.string.ok),null,object : IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                            dialog?.dismiss()
                            val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
                            findNavController().navigate(R.id.action_session_expiry,bundle)
                        }
                        override fun onDialogNegativeClick() {
                            dialog?.dismiss()
                        }

                    })
                    dialog?.setCancelable(false)
                    dialog?.show()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })

    }



    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    fun onBuyNowGoldClick(view: View){

                if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
                    var bundle = bundleOf("Plan" to "Gold")
                    findNavController().navigate(R.id.action_show_action_buy_now,bundle)
        }

    }

    fun onBuyNowPlatinumClick(view: View){

        if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
            var bundle = bundleOf("Plan" to "Platinum")
            findNavController().navigate(R.id.action_show_action_buy_now,bundle)
        }

    }

    fun onBuyNowSilverClick(view: View){

        if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
            var bundle = bundleOf("Plan" to "Silver")
            findNavController().navigate(R.id.action_show_action_buy_now,bundle)
        }

    }
    var dialogs: Dialog? = null
    fun loginPopup(){

        dialogs = activity?.let { Dialog(it,R.style.customAlertDialog) }
        dialogs?.setCancelable(true)
        if (null != dialogs) {
            val binding: LoginOrRegisterDialogBinding =
                DataBindingUtil.inflate(
                    dialogs!!.layoutInflater,
                    R.layout.login_or_register_dialog,
                    null,
                    false
                )
            dialogs?.setContentView(binding.root)
            binding.txtLoginOrRegister.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {

                    if (v != null) {
                        onRegisterOrLoginClicked(v)
                    }
                    findNavController().navigate(R.id.action_show_login_from_home)
                    dialogs?.dismiss()
                }

            })
            dialogs?.show()
        }
    }

}