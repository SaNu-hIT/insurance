package com.ilaftalkful.mobileonthego.view.marine

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.ilaftalkful.mobileonthego.databinding.MarineInsurenceFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.health.HelthErros
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity

class MarineInsurenceFragment : IlafBaseFragment() {
    val viewModel: MarineInsurenceViewModel by viewModels()
    var marineInsurenceFragmentBinding:MarineInsurenceFragmentBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        marineInsurenceFragmentBinding =
            DataBindingUtil.inflate<MarineInsurenceFragmentBinding>(
                inflater,
                R.layout.marine_insurence_fragment,
                container,
                false
            )

        marineInsurenceFragmentBinding?.lifecycleOwner = this
        marineInsurenceFragmentBinding?.viewModel = viewModel
        marineInsurenceFragmentBinding?.fragment = this
        marineInsurenceFragmentBinding?.executePendingBindings()
        marineInsurenceFragmentBinding?.error = HelthErros(null)
        if(isFromFga!!) {
            marineInsurenceFragmentBinding?.label =
                getString(R.string.fga_insurance)
        }else{
            marineInsurenceFragmentBinding?.label =
                getString(R.string.marine_insurence)
        }
        return marineInsurenceFragmentBinding?.root
    }
var isFromFga:Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null) {
            isFromFga = arguments?.getBoolean(Constants.ISFROM_FGA)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFromFga?.let { viewModel.getHeadertext(it) }
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.RESPOSNSE_SUCCESS -> {

                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.CORPORATE_QOUTE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(
                            requireActivity(),
                            it.statusMessage,
                            getString(R.string.ok),
                            null,
                            object : IlafCommonAlert.IlafDialogListener {
                                override fun onDialogPositiveClick() {
                                    viewModel.clearAll()
                                    findNavController()?.popBackStack()
                                }

                                override fun onDialogNegativeClick() {

                                }

                            }).show()
                }
                UserData.UserStatus.ERROR -> {
                    IlafCommonAlert(
                            requireActivity(),
                            it.statusMessage,
                            getString(R.string.ok),
                            null,
                            object : IlafCommonAlert.IlafDialogListener {
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
        marineInsurenceFragmentBinding?.edPhoneNumber?.addTextChangedListener(object : TextWatcher {
            val space = ' '
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // Remove spacing char
                if (s.length > 0 && s.length % 5 == 0) {
                    val c = s[s.length - 1]
                    if (space == c) {
                        s.delete(s.length - 1, s.length)
                    }
                }
                // Insert char where needed.
                if (s.length > 0 && s.length % 5 == 0) {
                    val c = s[s.length - 1]
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 3) {
                        s.insert(s.length - 1, space.toString())
                    }
                }
            }
        })
    }
    override fun moveToLogin() {
        val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
        findNavController().navigate(R.id.login_fragment,bundle)

    }



    fun onSubmitClick(view: View, error: HelthErros) {
        if (Utility.checkInternetConnection(requireContext())) {
            error.contactPersonNameError = IlafValidator.isValidName(viewModel.ContactPersonName.value!!,requireActivity())
            error.companyNameError = IlafValidator.isValidName(viewModel.CompanyName.value!!,requireActivity())
            error.contactPersonEmailError = IlafValidator.isValidUserName(viewModel.ContactPersonEmail.value!!,requireActivity())
            error.contactPersonMobileError = IlafValidator.isValidMobile(viewModel.ContactPersonMobile.value!!,requireActivity())
            error.messageDetailError = if(viewModel.MessageDetail.value!!.isNullOrEmpty())
                getString(R.string.message_empty)
            else null

            isFromFga?.let { viewModel.submitQoute(error, it) }
        } else {

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