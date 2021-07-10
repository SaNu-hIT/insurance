package com.ilaftalkful.mobileonthego.view.buyinsurance

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.TravelClaimsFragmentBinding
import com.ilaftalkful.mobileonthego.model.TravelClaimErrors
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveData
import com.ilaftalkful.mobileonthego.utilities.*
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.TravelClaimsViewModel
import java.util.*


class TravelClaimsFragment : IlafBaseFragment() {


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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        travelClaimsFragmentBinding?.txtLayoutPolicyNo?.setHint(getString(R.string.policy_no))
        travelClaimsFragmentBinding?.txtLayoutTypeOfClaim?.setHint(getString(R.string.type_of_claim))
        travelClaimsFragmentBinding?.txtLayoutDateSick?.setHint(getString(R.string.date_of_sickness))
        travelClaimsFragmentBinding?.textInputLayout6?.setHint(getString(R.string.kuwait_exit_date))
        travelClaimsFragmentBinding?.layoutExitData?.setHint(getString(R.string.kuwait_entry_date))
        travelClaimsFragmentBinding?.layoutDescription?.setHint(getString(R.string.brief_derscription))
        travelClaimsFragmentBinding?.layoutClaimAmount?.setHint(getString(R.string.claim_amount))
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


    val viewModel: TravelClaimsViewModel by viewModels()
    lateinit var navController: NavController
    var travelClaimsFragmentBinding: TravelClaimsFragmentBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        travelClaimsFragmentBinding = DataBindingUtil.inflate<TravelClaimsFragmentBinding>(
            inflater,
            R.layout.travel_claims_fragment,
            container,
            false
        )
        travelClaimsFragmentBinding?.lifecycleOwner = this
        travelClaimsFragmentBinding?.viewModel = viewModel
        travelClaimsFragmentBinding?.fragment = this
        travelClaimsFragmentBinding?.label = getString(R.string.travel_claims)
        travelClaimsFragmentBinding?.error = TravelClaimErrors()
        return travelClaimsFragmentBinding?.root
    }

    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPolicyList()
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel.getPolicyType()
        viewModel.policiesList.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, it
            )
            travelClaimsFragmentBinding?.autoPolicyNo?.setAdapter(adapter)

        })

        travelClaimsFragmentBinding?.autoPolicyNo?.setOnItemClickListener(object :
            AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val policyId = viewModel.actualPolicyList.get(position).motorPolicyID
                viewModel.policyId.postValue(policyId)
            }
        })
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                MotorLiveData.UserStatus.RESPOSNSE_SUCCESS -> {
                        (activity as SplashActivity).toggleLoader(false)
                    }
                MotorLiveData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                MotorLiveData.UserStatus.ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(), error?.errorMessage?:getString(R.string.something_went_wrong), getString(R.string.ok), null, null).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.YEAR_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.YEAR_ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(), error?.errorMessage?:getString(R.string.something_went_wrong), getString(R.string.ok), null, null).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.QUOTE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                   /* IlafCommonAlert(
                        requireActivity(),
                        it.statusMessage,
                        getString(R.string.ok),
                        null,
                        object : IlafCommonAlert.IlafDialogListener {
                            override fun onDialogPositiveClick() {
                                val bundle = bundleOf("isSuccess" to 3)
                                findNavController().navigate(R.id.action_show_claim_success)

                            }

                            override fun onDialogNegativeClick() {

                            }

                        }).show()*/
                    val bundle = bundleOf("isSuccess" to 3)
                    findNavController().navigate(R.id.action_show_claim_success,bundle)

                }
            }
        })

    }

    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    fun onDateOfSicknessClicked(view: View) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                viewModel.dateOfSickness.postValue(currentDate)
            },
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show()
    }

    fun onExistDateClicked(view: View) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                viewModel.exitDate.postValue(currentDate)
            },
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show()
    }

    fun onEntryDateClicked(view: View) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                viewModel.entryDate.postValue(currentDate)
            },
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        if (viewModel.exitDate.value.toString().isNotEmpty()) {
            picker.getDatePicker().minDate = getMindate(viewModel.exitDate.value)
        }
        picker.show()
    }

    private fun getMindate(value: String?): Long {
        var myCalendar = Calendar.getInstance()
        myCalendar.time = DateUtil.myFormat.parse(value)
        return myCalendar.timeInMillis
    }

    override fun onContctNumberClicked(view: View) {
       /* var textViw = view as TextView
        viewModel.phoneNoText.postValue(textViw.text.toString())
        var isCallPermission = Utility.hasPermissions(requireContext())
        if (isCallPermission) {
            makePhoneCall()
        } else {
            requestPermissions(Utility.permissonList, Constants.PHONE_CALL)
        }*/
        var textViw = view as TextView
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" +textViw.text.toString())
        startActivity(callIntent)
    }

    override fun makePhoneCall() {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + viewModel.phoneNoText.value.toString())
        startActivity(callIntent)

    }

    fun onTravelClaimSubmitClicked(view: View, error: TravelClaimErrors) {
        viewModel.userLiveData?.buttonClicked()
        error.policyNoError = if(viewModel.policyNo.value!!.isNullOrEmpty()) getString(R.string.policy_empty) else null
        if(viewModel.entryDate.value.toString().isNotEmpty()) {
            error.entryDateError = null
        }else{
            error.entryDateError =getString(R.string.entry_date_could_not_be_empty)
        }
        if(viewModel.exitDate.value.toString().isNotEmpty()) {
            error.exitDateError = null
        }else{
            error.exitDateError = getString(R.string.exit_date_empty)
        }
        if(viewModel.dateOfSickness.value.toString().isNotEmpty()) {
            error.dateSicknessError =  null //IlafValidator.isValidDate(DateUtil.getDateTorequestFromat(dateOfSickness.value!!))
        }else{
            error.dateSicknessError =  getString(R.string.sickness_date_empty)
        }
        error.typeOfClaimError = if(viewModel.typeOfClaim.value.isNullOrEmpty())  getString(R.string.claim_type_error) else null
        error.briefError = if(viewModel.briefDtata.value.isNullOrEmpty()) getString(R.string.brief_travel_claim) else null
        error.termsAndConditionsError = if(!viewModel.isTermsChecked.value!!) getString(R.string.check_terms_and_conditions) else null
        viewModel.isVisible.postValue(!viewModel.isTermsChecked.value!!)

        error.claimAmountError = if(viewModel.claimAmount.value.isNullOrEmpty()) getString(R.string.brief_travel_claim) else null
        error.termsAndConditionsError = if(!viewModel.isVisible.value!!)getString(R.string.check_terms_and_conditions) else null
        viewModel.onSubmit(error)
    }

    override fun onEmailClicked(view: View) {
        var address =(view as TextView).text.toString()
        val TO = arrayOf(address)
        val uri = Uri.parse(address)
                .buildUpon()
                .build()
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        emailIntent.data = Uri.parse("mailto:$address")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        activity?.let {
            ContextCompat.startActivity(
                    it, Intent.createChooser(emailIntent, getString(R.string.send_mail)), null
            )
        }
    }

    fun onUploadImageClicked(view: View) {
        askPermissionOrLaunchCamera()
    }

    override fun setImage(imageBitmap: Bitmap, encoded: String) {
        viewModel.uploadImage(encoded)
    }

    override fun moveToLogin() {
        val bundle = bundleOf(Constants.IS_FROM_SCREENS to true)
        navController?.navigate(R.id.login_fragment, bundle)

    }
}