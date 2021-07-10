package com.ilaftalkful.mobileonthego.view.login
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.ChangePasswordFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.UserRegistrationErrors
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.ChangePasswordViewModel
class ChangePasswordFragment : IlafBaseFragment() {
    val viewModel: ChangePasswordViewModel by viewModels()
    var isFromReset: Boolean? = false
    var changePasswordFragmentBinding:ChangePasswordFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         changePasswordFragmentBinding = DataBindingUtil.inflate<ChangePasswordFragmentBinding>(
            inflater,
            R.layout.change_password_fragment,
            container,
            false
        )
        changePasswordFragmentBinding?.lifecycleOwner = this
        changePasswordFragmentBinding?.viewModel = viewModel
        changePasswordFragmentBinding?.fragment = this
        changePasswordFragmentBinding?.errors = UserRegistrationErrors(null)
        changePasswordFragmentBinding?.label =
            findNavController().currentDestination?.label.toString()
        return changePasswordFragmentBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            isFromReset = arguments?.getBoolean(Constants.IS_FROM_RESET)
        }

        if(isFromReset!!)
        {
            changePasswordFragmentBinding?.label = getString(R.string.reset_password)
        }else{
            changePasswordFragmentBinding?.label = getString(R.string.change_password)
        }
        observeData()
    }

    fun observeData() {
        viewModel.userLiveData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.getStatus()) {
                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.RESET_PASSWORD_SUCCESS -> {
                    it?.statusMessage?.let { it1 -> showMsg(it1) }
                    (activity as SplashActivity).toggleLoader(false)
                    viewModel.password.value = ""
                    viewModel.confPassowrd.value = ""
                    IlafCommonAlert(requireActivity(),it.statusMessage?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                            findNavController().navigate(R.id.action_show_login)
                            viewModel.clearToken()
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()

                }
                UserData.UserStatus.CHANGE_PASSWORD_SUCCESS -> {
                    it?.statusMessage?.let { it1 -> showMsg(it1) }
                    (activity as SplashActivity).toggleLoader(false)
                    viewModel.password.value = ""
                    viewModel.confPassowrd.value = ""
                }
                UserData.UserStatus.ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    viewModel.clearToken()
                    IlafCommonAlert(requireActivity(),error?.getErrorMessage()?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })

    }

    fun onResetClicked(view: View, errors: UserRegistrationErrors) {


        if (Utility.checkInternetConnection(requireActivity())) {
            errors.passwordError = IlafValidator.isValidUserPassword(viewModel.password.value?.trim(),requireActivity())
            errors.passwordError = IlafValidator.isValidUserPassword(viewModel.password.value?.trim(),viewModel.confPassowrd.value?.trim(),requireActivity())

            viewModel.callChangePassword(errors, isFromReset)
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

    override fun onResume() {
        super.onResume()
        if (viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY) == null) {
            viewModel.pref.setStringPrefValue(
                IlafSharedPreference.Constants.LANGUAGE_KEY,
                IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY
            )
        }
        if (viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)
                .equals(IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        )
            viewModel.isENS.postValue(true)
        else
            viewModel.isENS.postValue(false)
    }

    override fun onEnglishClicked(view: View) {
        viewModel.pref.setStringPrefValue(
            IlafSharedPreference.Constants.LANGUAGE_KEY,
            IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY
        )
        viewModel.isENS.postValue(true)
        refreshConfigChange()
    }

    override fun onArabicClicked(view: View) {
        viewModel.pref.setStringPrefValue(
            IlafSharedPreference.Constants.LANGUAGE_KEY,
            IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY
        )
        viewModel.isENS.postValue(false)
        refreshConfigChange()
    }

    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }
}