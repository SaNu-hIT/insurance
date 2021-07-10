package com.ilaftalkful.mobileonthego.view.login

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseActivity
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.LoginFragmentBinding
import com.ilaftalkful.mobileonthego.model.SignInErrors
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.LoginViewModel
import com.ilaftalkful.mobileonthego.view.SplashActivity

class LoginFragment : IlafBaseFragment() {

    val viewModel: LoginViewModel by viewModels()
     var loginFragmentBinding :LoginFragmentBinding?=null
    var isFromScreens: Boolean? = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
         loginFragmentBinding = DataBindingUtil.inflate<LoginFragmentBinding>(
                inflater,
                R.layout.login_fragment,
                container,
                false
        )
        loginFragmentBinding?.lifecycleOwner = this
        loginFragmentBinding?.viewModel = viewModel
        loginFragmentBinding?.fragment = this
        loginFragmentBinding?.errors = SignInErrors(null)
        return loginFragmentBinding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isFromScreens = arguments?.getBoolean(Constants.IS_FROM_SCREENS)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      viewModel.isUsernameEmpty.observe(viewLifecycleOwner, Observer {
          if(it){
              loginFragmentBinding?.edtxtEmilid?.requestFocus()
          }else{
              loginFragmentBinding?.edtPassword?.requestFocus()
          }
      })
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.CLICKED -> {
                    (activity as SplashActivity).hideKeyboard()
                }
                UserData.UserStatus.LOGIN_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafSharedPreference(requireContext()).setBooleanPrefValue(
                            IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                            true
                    )
                    if (isFromScreens != null && isFromScreens!!) {
                        findNavController().popBackStack()
                    } else {

                        findNavController().navigate(R.id.action_show_home_guest)
                    }
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
//                    showMsg("Singing in...!!")
                }
                UserData.UserStatus.ERROR -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(requireActivity(),it.statusMessage?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                }

                UserData.UserStatus.USER_LOGIN_FAILED -> {
                    viewModel.isUserExist()
                }

                UserData.UserStatus.USER_EXIST_FAILED -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(requireActivity(),it.statusMessage?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                }
                UserData.UserStatus.USER_EXIST_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    var isSuccess =  it.isSuccess
                    var msg =it.statusMessage
                    if(isSuccess!!){
                        msg = getString(R.string.credentials_error)
                    }
                    IlafCommonAlert(requireActivity(),msg,getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                }
                UserData.UserStatus.FINGER_PRINT_LOGIN_FAILED -> {
                    (activity as SplashActivity).toggleLoader(false)
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

    fun onGuestClicked(view: View) {
        IlafSharedPreference(requireContext()).setBooleanPrefValue(
                IlafSharedPreference.Constants.IS_LOGEDIN_USER,
                false
        )
        findNavController().navigate(R.id.action_show_home_guest)
    }

    fun onRegisterClicked(view: View) {
        findNavController().navigate(R.id.action_show_register)
    }

    fun onResetPasswordClicked(view: View) {
        findNavController().navigate(R.id.action_login_fragment_to_resetPasswordFragment)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onLoginClicked(view: View, errors: SignInErrors) {
        if (Utility.checkInternetConnection(requireActivity())) {
            (activity as IlafBaseActivity).hideKeyboard()
            errors.userNameError = IlafValidator.isValidUserName(viewModel.username.value!!.trim(),requireActivity())
            errors.userPasswordError = IlafValidator.isValidUserPassword(viewModel.password.value!!.trim(),requireActivity())

            viewModel.callLogin(errors)
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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        loginFragmentBinding?.txtLayoutPassword?.setHint(getString(R.string.password))
        loginFragmentBinding?.txtLayoutEmilid?.setHint(getString(R.string.email_id))

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

    fun showFingerPrint(view: View){
        findNavController().navigate(R.id.action_show_finger_print)
    }
}