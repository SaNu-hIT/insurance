package com.ilaftalkful.mobileonthego.view.home

import android.app.Dialog
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
import androidx.navigation.fragment.NavHostFragment
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.CommonLoginViewBinding
import com.ilaftalkful.mobileonthego.databinding.ProductsFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.ProductsViewModel

class ProductsFragment : IlafBaseFragment() {
    val viewModel: ProductsViewModel by viewModels()
    lateinit var  navController:NavController
    lateinit var productsFragmentBinding:ProductsFragmentBinding
    var dialogs: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAppSettings()

        if(  Utility.checkInternetConnection(requireActivity())) {

                viewModel.getDashBoardData()

        } else {
            IlafCommonAlert(requireActivity(),getString(R.string.no_interbnet),getString(R.string.ok),null,null).show()
        }
        viewModel.dashBoardResponse.observe(this, Observer {
            viewModel.data.postValue(it)
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsFragmentBinding = DataBindingUtil.inflate<ProductsFragmentBinding>(inflater,R.layout.products_fragment, container, false)
        productsFragmentBinding.lifecycleOwner=this
        productsFragmentBinding.viewModel=viewModel
        productsFragmentBinding.fragment=this
        return productsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when(it.getStatus()){
                UserData.UserStatus.RESPOSNSE_SUCCESS->{
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED->{
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR->{
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(),error?.errorMessage?:getString(R.string.something_went_wrong),null,getString(R.string.ok),
                    null,null).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED->{
                   sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.APPSETTINGS_SUCCESS->{
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.APPSETTINGS_FAILED->{
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })

    }

    override fun moveToLogin() {
        val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
        navController.navigate(R.id.login_fragment,bundle)

    }
   override fun onRegisterOrLoginClicked(view: View){
       super.onRegisterOrLoginClicked(view)
       dialogs?.dismiss()
        navController.navigate(R.id.action_show_login_from_home)
    }


    fun loginPopup(){

        dialogs = activity?.let { Dialog(it,R.style.customAlertDialog) }
        dialogs?.setCancelable(true)
        if (null != dialogs) {
            val binding: CommonLoginViewBinding =
                DataBindingUtil.inflate(
                    dialogs!!.layoutInflater,
                    R.layout.common_login_view,
                    null,
                    false
                )
            binding.fragment = this
            dialogs?.setContentView(binding.root)
            binding.txtLoginOrRegister.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    dialogs?.dismiss()
                }

            })
            dialogs?.show()
        }
    }

    fun onTravelInsuranceClicked(view : View){
//        if (!viewModel.isValidUser.value!!){
//            loginPopup()
//        }else {
            navController.navigate(R.id.action_show_travel_insurance)
//        }
    }

    fun onTravelClaimClicked(view : View){
        if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
            navController.navigate(R.id.action_show_travel_claim)
        }
    }
    fun onMotorInsuranceClaimClicked(view : View){
        if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
            navController.navigate(R.id.action_dashboard_fragment_to_motorClaimFragment)
        }
    }

    fun onMotorInsuranceQouteClicked(view : View){
        if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
            navController.navigate(R.id.action_dashboard_fragment_to_motorQouteFragment)
        }
    }
    fun onClickProfile(view : View){
        val navHostFragment: NavHostFragment = parentFragment as NavHostFragment
        val parent = navHostFragment.parentFragment as DashBoardFragment
        parent.showProfile()
    }

    fun onMotorInsuranceRenewClicked(view : View){
        if (!viewModel.isValidUser.value!!){
            loginPopup()
        }else {
            navController.navigate(R.id.action_dashboard_fragment_to_motorRenewFragment)
        }
    }

    fun onHelthInsurenceCorpQouteClicked(view : View){
            navController.navigate(R.id.action_dashboard_fragment_to_corporateQoutesFragment)
    }

    fun onHelthInsurenceHospitalnetworkClicked(view : View){
       /* if (!viewModel.isValidUser.value!!){
            viewModel.toggleView.postValue(true)
        }else {*/
            navController.navigate(R.id.action_dashboard_fragment_to_hospitalNetworkFragment)
       // }
    }

    fun onHelthInsurenceAssistForClameClicked(view : View){
      /*  if (!viewModel.isValidUser.value!!){
            viewModel.toggleView.postValue(true)
        }else {*/
            navController.navigate(R.id.action_dashboard_fragment_to_assistanceForClameFragment)
      //  }
    }
    fun onMarineClicked(view : View){
       /* if (!viewModel.isValidUser.value!!){
            viewModel.toggleView.postValue(true)
        }else {
*/

            val bundle = bundleOf( Constants.ISFROM_FGA to false)
            navController.navigate(R.id.action_dashboard_fragment_to_marineInsurenceFragment,bundle)
      //  }
    }

    fun onFGAClicked(view : View){
       /* if (!viewModel.isValidUser.value!!){
            viewModel.toggleView.postValue(true)
        }else {*/
            val bundle = bundleOf( Constants.ISFROM_FGA to true)
            navController.navigate(R.id.action_dashboard_fragment_to_marineInsurenceFragment,bundle)
       // }
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

}