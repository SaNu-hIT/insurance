package com.ilaftalkful.mobileonthego.view.splash

import android.os.Bundle
import android.os.Handler
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
import com.ilaftalkful.mobileonthego.databinding.SplashFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.viewmodel.SplashViewModel

class SplashFragment : IlafBaseFragment() {

    val viewModel: SplashViewModel by viewModels()
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //3 seconds

    /* internal val mRunnable: Runnable = Runnable {

     }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val splashFragmentBinding = DataBindingUtil.inflate<SplashFragmentBinding>(
            inflater,
            R.layout.splash_fragment,
            container,
            false
        )
        splashFragmentBinding.lifecycleOwner = this
        splashFragmentBinding.viewModel = viewModel
        return splashFragmentBinding.root
    }


    fun isfromDeepLink(): Boolean {

        return viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_RESET_TOKEN) == "" || viewModel.pref.getStringPrefValue(
            IlafSharedPreference.Constants.LANGUAGE_RESET_TOKEN
        ) == null
//                && viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_ISFROM_DEEP) == "0"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isfromDeepLink()) {
            viewModel.getSplashSceenMessage()
        } else {
            val bundle = bundleOf(Constants.IS_FROM_RESET to true)
            findNavController().navigate(R.id.action_change_password, bundle)
        }
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {

                UserData.UserStatus.OPERATION_STARTED -> {

                }
                UserData.UserStatus.ERROR -> {
                    if (IlafSharedPreference(requireContext()).getBooleanPrefValue(
                            IlafSharedPreference.Constants.IS_LOGEDIN_USER
                        ) == null
                    ) {
                        findNavController().navigate(R.id.action_show_login)
                    } else if (IlafSharedPreference(requireContext()).getBooleanPrefValue(
                            IlafSharedPreference.Constants.IS_LOGEDIN_USER
                        )
                    ) {
                        findNavController().navigate(R.id.action_show_home_for_guest)
                    } else {
                        findNavController().navigate(R.id.action_show_login)
                    }
                }

                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    if (!requireActivity().isFinishing) {
                        if(viewModel.data!=null  && viewModel.data.value?.isEmpty()?:false){
                            if (IlafSharedPreference(requireContext()).getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER) == null) {
                                findNavController().navigate(R.id.action_show_home)
                            } else if (IlafSharedPreference(requireContext()).getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER)) {
                                findNavController().navigate(R.id.action_show_login)
                            } else {
                                findNavController().navigate(R.id.action_show_login)
                            }
                        }else {
                            val bundle = bundleOf("splashMessage" to viewModel.data.value)
                            findNavController().navigate(
                                    R.id.action_show_splash_message,
                                    bundle
                            )
                        }

                    }
                }
            }
        })


    }

}