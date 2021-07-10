package com.ilaftalkful.mobileonthego.view.payment

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.BuyInsuranceBasicDetailsFragmentBinding
import com.ilaftalkful.mobileonthego.model.BuyInsurenceBasicErrors
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.view.SplashActivity
import java.util.*
import java.util.regex.Pattern

class BuyInsuranceBasicDetailsFragment : IlafBaseFragment() {

    val viewModel: BuyInsuranceBasicDetailsViewModel by viewModels()
    var binding:BuyInsuranceBasicDetailsFragmentBinding?=null
    var navController: NavController?=null
    var plan:String ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNationalities()
        viewModel.getDashBoardData()
        observerData()
        if(arguments!=null){
            plan = arguments?.getString("Plan")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate<BuyInsuranceBasicDetailsFragmentBinding>(
            inflater,
            R.layout.buy_insurance_basic_details_fragment, container, false
        )
        binding?.lifecycleOwner = this
        binding?.fragment = this
        binding?.viewModel = viewModel
        binding?.error = BuyInsurenceBasicErrors(null)
        binding?.label = findNavController().currentDestination?.label.toString()
        binding?.executePendingBindings()
        return binding?.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding?.tlFirstName?.setHint(getString(R.string.enter_your_name))
        binding?.tlEmail?.setHint(getString(R.string.email))
        binding?.tlMobileNumber?.setHint(getString(R.string.mobile_number))
        binding?.tlCivilId?.setHint(getString(R.string.civil_id))
        binding?.tlPassportNumber?.setHint(getString(R.string.passport_number))
        binding?.layoutDob?.setHint(getString(R.string.dob))
        binding?.tlFamilyName?.setHint(getString(R.string.family_name))
        binding?.tlSecondName?.setHint(getString(R.string.second_name))


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
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


        fun onSaveAndNextClicked(view: View, error: BuyInsurenceBasicErrors) {
            error.nameError = isValidName(viewModel.firstName.value!!)
            error.secondNameError = isValidSecondName(viewModel.secondName.value!!)
            error.phoneNumberError = isValidMobile(viewModel.mobileNumber.value!!)
            error.userEmailError = isValidEmails(viewModel.emailId.value!!.trim())
            error.familyNameError = isValidSecondName(viewModel.familyName.value!!)
            error.dateOfBirth = IlafValidator.isValidDate(viewModel.doB.value!!,requireActivity())
            error.passPortNumber = isValidPasportNumber(viewModel.pssportNo.value!!)
            if(isCivilIdImageUploaded()!=null){
                error.civilidError = isCivilIdImageUploaded()
            }else{
                error.civilidError = IlafValidator.isValidCivilId(viewModel.civiliId.value!!,viewModel.doB.value!!,requireActivity())
            }
            if(viewModel.selectedNationality.value==0){
                error.nationalityError=getString(R.string.nationality_error)
                IlafCommonAlert(requireActivity(),getString(R.string.nationality_error),null,getString(R.string.ok),null,null).show()
            }else{
                error.nationalityError=null
            }
            viewModel.onSaveAndNextClicked(view,error)

        }

    private fun isCivilIdImageUploaded(): String? {
        var errorMSg: String? = null
         if(!viewModel.isCivilId.value!!){
            errorMSg = getString(R.string.civil_id_image)
        }
        return errorMSg
    }

    fun isValidPasportNumber(value: String): String? {
        var errorMSg: String? = null
        if (value.isNullOrEmpty()) {
            errorMSg = getString(R.string.passport_no_empty)
        } else if(!viewModel.isPassort.value!!){
            errorMSg = getString(R.string.passport_image_upload)
        }
        return errorMSg
    }
    fun isValidName(value: String): String? {
        var errorMSg: String? = null
        if (value.isNullOrEmpty()){
            errorMSg = getString(R.string.name_lenght)
        } else if (value.length < 3) {
            errorMSg = getString(R.string.name_lenght)
        } else if (!Pattern.matches("[a-zA-Z ]+", value)) {
            errorMSg = getString(R.string.naem_alphabet_only)
        }
        return errorMSg
    }

    fun isValidSecondName(value: String): String? {
        var errorMSg: String? = null
        if (value.isNullOrEmpty()){
            errorMSg = getString(R.string.name_empty)
        } else if (!Pattern.matches("[a-zA-Z ]+", value)) {
            errorMSg = getString(R.string.naem_alphabet_only)
        }
        return errorMSg
    }
    fun isValidEmails(email: String): String? {
        var errorMSg: String? = null
        if (IlafValidator.isNullOrEmpty(email)) {
            errorMSg = getString(R.string.email_empty)
            return errorMSg
        }
        if (!IlafValidator.isValidEmail(email)) {
            errorMSg = getString(R.string.invalid_email)
            return errorMSg
        }
        return errorMSg
    }
    fun isValidMobile(phone: String): String? {
        var errorMSg: String? = null
        if (phone.length < 9) {
            errorMSg = getString(R.string.invalid_phone_number)
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            errorMSg = getString(R.string.invalid_phone_number)
        }
        return errorMSg
    }

    fun isValidCivilId(phone: String): String? {
        var errorMSg: String? = null
        if (phone.length < 12) {
            errorMSg = getString(R.string.invalid_civilid)
        }

        return errorMSg
    }

    fun onCivilIdClick() {
        viewModel.imageType.postValue(Constants.DRIVER_CIVIL_ID)
        onUploadImageClick()
    }

    fun onPassportClick() {
        viewModel.imageType.postValue(Constants.PASSPORT_NO)
        onUploadImageClick()

    }

    override fun setImage(imageBitmap: Bitmap, encoded: String) {
        viewModel.uploadImage(encoded)
    }

    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }

    fun onDateOfBirthClick(view: View) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                viewModel.doB.postValue(currentDate)
            },
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().maxDate = System.currentTimeMillis()
        picker.show()
    }


    fun observerData() {


        viewModel.motorLiveData?.observe(this, androidx.lifecycle.Observer {
            when (it.getStatus()) {
                MotorLiveData.UserStatus.SHOW_PAYMENT -> {
                    var bundle = Bundle()
                    bundle.putString("firstName",viewModel.firstName.value)
                    bundle.putString("secondName",viewModel.secondName.value)
                    bundle.putString("familyName",viewModel.familyName.value)
                    bundle.putString("email",viewModel.emailId.value)
                    bundle.putString("mobileNumber",viewModel.mobileNumber.value)
                    bundle.putInt("nationalityID",viewModel.actualNationalityList.get(viewModel.selectedNationality.value!!-1).id!!)
                    bundle.putString("civilID",viewModel.civiliId.value)
                    bundle.putString("passportNo",viewModel.pssportNo.value)
                    bundle.putString("dob",viewModel.doB.value)
                    bundle.putString("plan",plan)
                    findNavController().navigate(R.id.action_show_payment,bundle)
                }
                MotorLiveData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                MotorLiveData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    showMsg(error?.getErrorMessage() ?:getString(R.string.something_went_wrong))

                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.SESSION_EXPIRED-> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }

        })


        viewModel.userLiveData?.observe(this, androidx.lifecycle.Observer {
            when(it.getStatus()){
                UserData.UserStatus.RESPOSNSE_SUCCESS->{
                    viewModel.processData(viewModel.dashBoardResponse.value?.userDetails)
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED->{
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR->{
                    val error = it.getError()
                    IlafCommonAlert(requireActivity(),error?.errorMessage?:getString(R.string.something_went_wrong),null,getString(R.string.ok),
                            null,null).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED->{
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })

    }
    override fun moveToLogin() {
        val bundle = bundleOf(Constants.IS_FROM_SCREENS to true)
        navController?.navigate(R.id.login_fragment, bundle)

    }
}