package com.ilaftalkful.mobileonthego.view.register

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseActivity
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.RegisterFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.UserRegistrationErrors
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.RegisterViewModel
import java.util.*


class RegisterFragment : IlafBaseFragment() {

    val viewModel: RegisterViewModel by viewModels()
    var registerFragmentBinding: RegisterFragmentBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerFragmentBinding = DataBindingUtil.inflate<RegisterFragmentBinding>(
            inflater,
            R.layout.register_fragment,
            container,
            false
        )
        registerFragmentBinding?.lifecycleOwner = this
        registerFragmentBinding?.viewModel = viewModel
        registerFragmentBinding?.fragment = this
        registerFragmentBinding?.errors = UserRegistrationErrors(null)
        return registerFragmentBinding?.root
    }
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(R.id.action_show_login_from_register)
        }
    }



    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerFragmentBinding?.spinnerView?.setEnabled(false)
        registerFragmentBinding?.edPhoneNumber?.addTextChangedListener(object : TextWatcher {
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
        viewModel.isChecked.observe(viewLifecycleOwner, Observer {
                viewModel.setFingerPrintEnable(it)
        })
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.REGISTRATION_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(requireActivity(),it.statusMessage,getString(R.string.ok),null,object :IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                            findNavController().navigate(R.id.action_show_login_from_register)
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
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

    fun onLoginClicked(view: View) {
        findNavController().popBackStack()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        registerFragmentBinding?.textInputLayout?.setHint(getString(R.string.enter_your_name))
        registerFragmentBinding?.textInputLayout3?.setHint(getString(R.string.email))
        registerFragmentBinding?.textInputLayoutPassword?.setHint(getString(R.string.password))
        registerFragmentBinding?.textInputLayoutConfPassword?.setHint(getString(R.string.conf_password))
        registerFragmentBinding?.textInputLayout4?.setHint(getString(R.string.enter_your_mobile_number))
        registerFragmentBinding?.textInputLayout5?.setHint(getString(R.string.civil_id))


    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun onRegisterClicked(view: View, error: UserRegistrationErrors){
        if(Utility.checkInternetConnection(requireActivity())) {
            (activity as IlafBaseActivity).hideKeyboard()
            error.nameError = IlafValidator.isValidName(viewModel.name.value!!.trim(),requireActivity())
            error.userEmailError = IlafValidator.isValidEmails(viewModel.email.value!!.trim(),requireActivity())
            error.phoneNumberError = IlafValidator.isValidMobile(viewModel.phonenumber.value!!,requireActivity())
            // error.civilidError = IlafValidator.isValidUserPassword(civilid.value!!)4            error.phoneNumberError = IlafValidator.isValidMobile(viewModel.phonenumber.value!!,requireActivity()
            error.dobError = IlafValidator.isValidDate(DateUtil.getDateFormatFromDOb(viewModel.dd.value!!),requireActivity())

            error.genderError = IlafValidator.isGenderSelected(viewModel.isMaleChecked.value,requireActivity())
            error.passwordError = IlafValidator.isValidUserPassword(viewModel.password.value,requireActivity())
            if(  error.passwordError.isNullOrEmpty())
            error.passwordError = IlafValidator.isValidUserPassword(viewModel.password.value, viewModel.confPassword.value,requireActivity())
            error.confirmError = IlafValidator.isValidUserPassword(viewModel.confPassword.value,requireActivity())
            if( error.confirmError.isNullOrEmpty())
            error.confirmError = IlafValidator.isValidUserPassword(viewModel.confPassword.value, viewModel.password.value,requireActivity())
            error.civilidError = IlafValidator.isValidCivilId(viewModel.civilid.value!!,viewModel.dd.value!!,requireActivity())
            if (IlafValidator.isNullOrEmpty(error.nameError)
                    && IlafValidator.isNullOrEmpty(error.userEmailError)
                    && IlafValidator.isNullOrEmpty(
                            error.phoneNumberError
                    ) && IlafValidator.isNullOrEmpty(
                            error.civilidError
                    ) && IlafValidator.isNullOrEmpty(error.civilidError)
                    && IlafValidator.isNullOrEmpty(error.genderError) && IlafValidator.isNullOrEmpty(error.passwordError) && IlafValidator.isNullOrEmpty(
                            error.confirmError
                    )
            ) {
                viewModel.callRegistration(error)
            }
        }else{
            IlafCommonAlert(requireActivity(),getString(R.string.no_interbnet),getString(R.string.ok),null,null).show()
        }


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

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int) {
        when (id) {
            R.id.radioButton2 -> viewModel.isMaleChecked.postValue("1")
            R.id.radioButton -> viewModel.isMaleChecked.postValue("2")
        }
    }

    fun onDateClicked(view: View) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                registerFragmentBinding?.editTextTime?.setText(currentDate)
                viewModel.dd.postValue(currentDate)
            },
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show()
    }
}