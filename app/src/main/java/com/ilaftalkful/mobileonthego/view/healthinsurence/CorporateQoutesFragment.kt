package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.CorporateQoutesFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.health.Department
import com.ilaftalkful.mobileonthego.model.health.HelthErros
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity

class CorporateQoutesFragment : IlafBaseFragment() {

    val viewModel: CorporateQoutesViewModel by viewModels()
    var department: List<Department>? = null
    var departmentName: List<Department>? = null
    var corporateQoutesFragmentBinding: CorporateQoutesFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        corporateQoutesFragmentBinding = DataBindingUtil.inflate<CorporateQoutesFragmentBinding>(
            inflater,
            R.layout.corporate_qoutes_fragment,
            container,
            false
        )
        corporateQoutesFragmentBinding?.lifecycleOwner = this
        corporateQoutesFragmentBinding?.viewModel = viewModel
        corporateQoutesFragmentBinding?.fragment = this
        corporateQoutesFragmentBinding?.error = HelthErros(null)
        corporateQoutesFragmentBinding?.executePendingBindings()
        corporateQoutesFragmentBinding?.label =
            getString(R.string.corporate_qoutes)
        return corporateQoutesFragmentBinding?.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        corporateQoutesFragmentBinding?.layoutCompanyName?.setHint(getString(R.string.company_name))
        corporateQoutesFragmentBinding?.txtLayoutName?.setHint(getString(R.string.name))
        corporateQoutesFragmentBinding?.txtLayoutEmailid?.setHint(getString(R.string.email_id))
        corporateQoutesFragmentBinding?.textInputLayout7?.setHint(getString(R.string.mobile_number))
        corporateQoutesFragmentBinding?.layoutMessage?.setHint(getString(R.string.your_message))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDeprtments()
        observedata()
        corporateQoutesFragmentBinding?.edPhoneNumber?.addTextChangedListener(object : TextWatcher {
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
                    IlafCommonAlert(requireActivity(),it.statusMessage?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object : IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                            viewModel.clearAll()
                            findNavController()?.popBackStack()
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()

                }
                UserData.UserStatus.ERROR -> {
                    (activity as SplashActivity).toggleLoader(false)
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(), error?.errorMessage?:getString(R.string.something_went_wrong), getString(R.string.ok), null, null).show()

                }
                UserData.UserStatus.SESSION_EXPIRED ->{
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })
    }
    override fun moveToLogin() {
        val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
        findNavController().navigate(R.id.login_fragment,bundle)

    }

    var departmentSpinner: ArrayAdapter<*>? = null
    private fun observedata() {

        viewModel.department.observe(viewLifecycleOwner, Observer {
            department = it
            departmentSpinner = department?.let { it1 ->
                context?.let {
                    ArrayAdapter<Department?>(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        it1
                    )
                }
            }!!
            corporateQoutesFragmentBinding?.departmentSpinner?.adapter = departmentSpinner
        })
    }
    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }



    fun onSubmitClick(view: View,errors: HelthErros){
        if(Utility.checkInternetConnection(requireContext())){
            errors.contactPersonNameError = IlafValidator.isValidName(viewModel.ContactPersonName.value!!,requireActivity())
            errors.companyNameError = IlafValidator.isValidName(viewModel.CompanyName.value!!,requireActivity())
            errors.contactPersonEmailError = IlafValidator.isValidUserName(viewModel.ContactPersonEmail.value!!,requireActivity())
            errors.contactPersonMobileError = IlafValidator.isValidMobile(viewModel.ContactPersonMobile.value!!,requireActivity())
            errors.messageDetailError = if(viewModel.MessageDetail.value!!.isNullOrEmpty()) getString(R.string.message_empty)
            else null
            viewModel.submitQoute(errors)
        }else{

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

}