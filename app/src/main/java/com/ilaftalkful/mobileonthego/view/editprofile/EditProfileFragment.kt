package com.ilaftalkful.mobileonthego.view.editprofile

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.EditProfileFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.UserRegistrationErrors
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.EditProfileViewModel
import java.util.*


class EditProfileFragment : IlafBaseFragment() {

    val viewModel: EditProfileViewModel by viewModels()
    var  binding:EditProfileFragmentBinding?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate<EditProfileFragmentBinding>(inflater, R.layout.edit_profile_fragment, container, false)
        binding?.viewModel=viewModel
        binding?.fragment=this
        binding?.lifecycleOwner =this
        binding?.label = getString(R.string.edit_profile)
        binding?.error = UserRegistrationErrors(null)
        return binding?.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding?.textInputLayout?.setHint(getString(R.string.enter_your_name))
        binding?.textInputLayout3?.setHint(getString(R.string.email))
        binding?.textInputLayout4?.setHint(getString(R.string.mobile_number))
        binding?.textInputLayout5?.setHint(getString(R.string.civil_id))
        binding?.txtLayoutDateSick?.setHint(getString(R.string.dob))


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.edPhoneNumber?.addTextChangedListener(object : TextWatcher {
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
        viewModel.userLiveData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.getStatus()) {
                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR -> {
                    val error = it.getError()
                    showMsg(getString(R.string.something_went_wrong))
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })

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

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int) {
        when(id){
            R.id.radioButton2 -> viewModel.isMaleChecked.postValue("1")
            R.id.radioButton -> viewModel.isMaleChecked.postValue("2")
        }
    }
    fun onDateofBirthClicked(view: View){
        var myCalendar= Calendar.getInstance()
        var picker = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                    viewModel.dob.postValue(currentDate)
                },
                myCalendar
                        .get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show()
    }

    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }

    fun onCancelClick(){
        findNavController().popBackStack()
    }

    fun callRegistration(error: UserRegistrationErrors){
        error.nameError = IlafValidator.isValidName(viewModel.name.value!!,requireActivity())
        error.userEmailError = IlafValidator.isValidUserName(viewModel.email.value!!.trim(),requireActivity())
        error.phoneNumberError = IlafValidator.isValidMobile(viewModel.phonenumber.value!!,requireActivity())
        viewModel.callRegistration(error)

    }
}