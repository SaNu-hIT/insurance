package com.ilaftalkful.mobileonthego.view.login

import android.app.KeyguardManager
import android.content.Context.KEYGUARD_SERVICE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.FingerPrintFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import java.util.concurrent.Executors


class FingerPrintFragment : IlafBaseFragment() {
    val viewModel: FingerPrintViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =DataBindingUtil.inflate<FingerPrintFragmentBinding>(
            inflater,
            R.layout.finger_print_fragment, container, false
        )
        binding.lifecycleOwner =this
        binding.fragment=this
        binding.viewModel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    findNavController().navigate(R.id.action_show_home_guest)
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
                    showMsg(error?.getErrorMessage() ?: getString(R.string.something_went_wrong))
                }
            }
        })
    }

    fun onCancelClick(view: View){
        findNavController().popBackStack()
    }

    fun checkBiometricSupport(): Boolean? {
        if(viewModel.pref.getBooleanPrefValue(IlafSharedPreference.Constants.IS_FINGERPRINT) && viewModel.username.value!=null) {
            val keyguardManager = requireActivity().getSystemService(KEYGUARD_SERVICE) as KeyguardManager?
            val packageManager: PackageManager = requireActivity().getPackageManager()
            if (!keyguardManager!!.isKeyguardSecure) {
//            notifyUser("Lock screen security not enabled in Settings")
                showMsg(getString(R.string.lock_screen_not_enabled))
                return false
            } else if (!Utility.hasPermissions(requireContext()) ){
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        Utility.permissonList,
                        Constants.PERMISSION_FINGER_PRINT
                )
                //
                return false
            } else {
                loginUsingFingerPrint()
            }
            return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                true
            } else true
        }else{
            if( viewModel.username.value==null){
                showMsg(getString(R.string.something_went_wrong))
            }else {
                showMsg(getString(R.string.enabled_in_app))
            }
            return false
        }
    }

    override fun loginUsingFingerPrint() {
        val executor = Executors.newSingleThreadExecutor()
        val biometricPrompt = BiometricPrompt(
            requireActivity(),
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    requireActivity().runOnUiThread(Runnable {
                            viewModel.callLogin()
                    })
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showMsg( getString(R.string.something_went_wrong))
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.finger_on_sensor))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }


    
}

