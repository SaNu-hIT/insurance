package com.ilaftalkful.mobileonthego.view.login

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseActivity
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.FragmentResetPasswordBinding
import com.ilaftalkful.mobileonthego.model.SignInErrors
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.ResetPasswordViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : IlafBaseFragment() {

    val viewModel: ResetPasswordViewModel by viewModels()
    var fragmentResetPasswordBinding:FragmentResetPasswordBinding?=null
    lateinit var navConrtrol: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentResetPasswordBinding = DataBindingUtil.inflate<FragmentResetPasswordBinding>(inflater,R.layout.fragment_reset_password, container, false)
        fragmentResetPasswordBinding?.lifecycleOwner=this
        fragmentResetPasswordBinding?.viewModel=viewModel
        fragmentResetPasswordBinding?.fragment=this
        fragmentResetPasswordBinding?.errors = SignInErrors(null)
        return fragmentResetPasswordBinding?.root
    }
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navConrtrol.navigate(R.id.action_show_login)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navConrtrol = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.CLICKED -> {
                    (activity as SplashActivity).hideKeyboard()
                }
                UserData.UserStatus.RESET_PASSWORD_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    if(it.statusMessage!=null) {
                        IlafCommonAlert(requireActivity(),it.statusMessage?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                            override fun onDialogPositiveClick() {
                                navConrtrol.navigate(R.id.action_show_home)

                            }

                            override fun onDialogNegativeClick() {

                            }

                        }).show()
                    }

                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
//                    showMsg("Singing in...!!")
                }
                UserData.UserStatus.ERROR -> {
                    (activity as SplashActivity).toggleLoader(false)
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(),it.getError()?.getErrorMessage()?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        fragmentResetPasswordBinding?.textInputLayout2?.setHint(getString(R.string.email_id))

    }

    fun onResetClicked(view: View,errors:SignInErrors){
        if(Utility.checkInternetConnection(requireActivity())) {
            (activity as IlafBaseActivity).hideKeyboard()
            errors.userNameError = IlafValidator.isValidUserName(viewModel.username.value!!.trim(),requireActivity())
            viewModel.resetPassword(errors)
        }else{
            IlafCommonAlert(requireActivity(),getString(R.string.no_interbnet),getString(R.string.ok),null,null).show()
        }
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
    override fun onResume() {
        super.onResume()
        if(viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null) {
            viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY,IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        }
        if(viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY))
            viewModel.isENS.postValue(true)
        else
            viewModel.isENS.postValue(false)
    }
}