package com.ilaftalkful.mobileonthego.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.ilaftalkful.mobileonthego.databinding.ProfileFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.*


class ProfileFragment : IlafBaseFragment() {
    val viewModel: ProfileViewModel by viewModels()
    lateinit var warnignDialog:IlafCommonAlert
    var navController:NavController?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val profileFragmentBinding = DataBindingUtil.inflate<ProfileFragmentBinding>(inflater, R.layout.profile_fragment, container, false)
        profileFragmentBinding.lifecycleOwner=this
        profileFragmentBinding.viewModel=viewModel
        profileFragmentBinding.fragment=this
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        iv_back_arrow
        return profileFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel.isChecked.observe(viewLifecycleOwner, Observer {
            viewModel.setFingerPrintEnable(it)
        })
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.LOG_OUT_SUCCESS -> {
                    warnignDialog.dismiss()
                    navController?.navigate(R.id.action_show_login_from_home)
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.LOG_OUT_ERROR -> {
                    warnignDialog.dismiss()
                    warnignDialog = IlafCommonAlert(requireActivity(), getString(R.string.logotu_failed), getString(R.string.ok), null,
                            object : IlafCommonAlert.IlafDialogListener {
                                override fun onDialogPositiveClick() {
                                    warnignDialog.dismiss()
                                }

                                override fun onDialogNegativeClick() {
                                }

                            })
                    warnignDialog.show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED -> {
                    dialog = IlafCommonAlert(requireActivity(), getString(R.string.session_expiry), getString(R.string.ok), null, object : IlafCommonAlert.IlafDialogListener {
                        override fun onDialogPositiveClick() {
                            dialog?.dismiss()
                            val bundle = bundleOf(Constants.IS_FROM_SCREENS to true)
                            navController?.navigate(R.id.action_session_expiry, bundle)

                        }

                        override fun onDialogNegativeClick() {
                            dialog?.dismiss()
                        }

                    })
                    dialog?.setCancelable(false)
                    dialog?.show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.ERROR->{
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(), error?.errorMessage?:getString(R.string.something_went_wrong), getString(R.string.ok), null, null).show()
                }
            }
        })
    }

    fun onLogoutClicked(view: View){
        warnignDialog = IlafCommonAlert(requireActivity(), getString(R.string.log_out_warning), getString(R.string.ok), getString(R.string.cancel),
                object : IlafCommonAlert.IlafDialogListener {
                    override fun onDialogPositiveClick() {
                        if (Utility.isLogedIn(requireActivity())) {
                            warnignDialog.dismiss()
                            if (Utility.checkInternetConnection(requireActivity())) {
                                viewModel.callLogout()
                            } else {
                                IlafCommonAlert(
                                        requireActivity(),
                                        getString(R.string.no_interbnet),
                                        getString(R.string.ok),
                                        null,
                                        null
                                ).show()
                            }
                        }
                    }

                    override fun onDialogNegativeClick() {
                        warnignDialog.dismiss()
                    }

                })
        warnignDialog.show()
    }

    fun onAddFamilyClicked(view: View){
        navController?.navigate(R.id.action_profile_fragment_to_editFamilyListFragment)

      //  IlafSharedPreference(requireContext()).setBooleanPrefValue(IlafSharedPreference.Constants.IS_GUEST_LOGIN,true)
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
        viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY, IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        viewModel.isENS.postValue(true)
        refreshConfigChange()
    }

    override fun onArabicClicked(view: View) {
        viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY, IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY)
        viewModel.isENS.postValue(false)
        refreshConfigChange()
    }
    override fun onRegisterOrLoginClicked(view: View){
        super.onRegisterOrLoginClicked(view)
        navController?.navigate(R.id.action_show_login_from_home)
    }

    override fun onHomeClicked(view: View) {
        val navHostFragment : NavHostFragment =  getParentFragment() as NavHostFragment
        val parent =  navHostFragment.getParentFragment() as DashBoardFragment
        parent.showHome()
    }

    fun onChangePasswordClicked()
    {
        navController?.navigate(R.id.action_change_password)
    }

    fun onEditProfileClick(){
        navController?.navigate(R.id.action_edit_profile)
    }

    fun onAboutUsClicked(view: View){
        navController?.navigate(R.id.action_show_about_us)
    }

    fun onWebAddressClicked(view: View){
        var v = view as TextView
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.powered_by_alghanim_technologies_link)))
        startActivity(browserIntent)
    }
}