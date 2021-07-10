package com.ilaftalkful.mobileonthego.view.payment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
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
import com.ilaftalkful.mobileonthego.customcomponents.ChooseFamilyPopup
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.AddFamilyMemberBinding
import com.ilaftalkful.mobileonthego.databinding.PaymentSubmitFragmentBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveData
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.SplashActivity
import java.util.*

class PaymentSubmitFragment : IlafBaseFragment() ,OnItemClickListener{

    val viewModel :BuyInsuranceBasicDetailsViewModel by  viewModels()
    var navController:NavController?=null
    var chooseFamilyPopup: ChooseFamilyPopup?=null
    var binding: PaymentSubmitFragmentBinding?=null
    var familyMemberPolicyAdapter = FamilyMemberPolicyAdapter()
    var plan:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

         binding = DataBindingUtil.inflate<PaymentSubmitFragmentBinding>(inflater,
                 R.layout.payment_submit_fragment, container, false)
        binding?.lifecycleOwner =this
        binding?.fragment=this
        binding?.viewModel = viewModel
        binding?.label = findNavController().currentDestination?.label.toString()
        binding?.executePendingBindings()
        return binding?.root
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding?.textInputLayout6?.setHint(getString(R.string.start_date))
        binding?.layoutExitData?.setHint(getString(R.string.to_date))
    }


   /* val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(R.id.action_back_press)
        }
    }*/

    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }

 /*   override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }
*/
    override fun onResume() {
        super.onResume()
        viewModel.getFamilyList()
        if(viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null) {
            viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY, IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        }
        if(viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectedPolicyPeriod.observe(this, androidx.lifecycle.Observer {
            setToDate()
        })
        viewModel.selectedPolicyOptions.observe(this, androidx.lifecycle.Observer {
            setToDate()
        })
        viewModel.selectedPolicyType.observe(this, androidx.lifecycle.Observer {

            if(binding?.txtLayoutPolicyOption?.selectedItem.toString().equals(getString(R.string.individual))) {
                viewModel.isIndividualSelected.postValue(true)
            }else{
                viewModel.isIndividualSelected.postValue(false)
            }
            setToDate()
        })
        viewModel.startDate.observe(this, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                var maxAge = IlafSharedPreference(requireActivity()).getStringPrefValue(IlafSharedPreference.Constants.MAX_AGE)?:"80"
                if(checkDobIs80(maxAge)){
                    viewModel.isAgeError.postValue(true)
                    binding?.textInputLayout6?.error = getString(R.string.age_error)+ maxAge + getString(R.string.age_error_maxage)
                    IlafCommonAlert(requireActivity(),getString(R.string.age_error)+ maxAge + getString(R.string.age_error_maxage),getString(R.string.ok),null,null).show()
                }else {
                    viewModel.isAgeError.postValue(false)
                    binding?.textInputLayout6?.error = null
                    setToDate()
                }
            }
        })

        viewModel.selectedFamilyMembers.observe(this, androidx.lifecycle.Observer {
            setFamilySelectedAdapter(it)
        })

        viewModel.isValid.observe(this, androidx.lifecycle.Observer {
            if (it) {
                viewModel.getTotalPolicyAmount()
            }
        })

        viewModel.familyList.observe(this, androidx.lifecycle.Observer {
            //viewModel.familyMembersList = it
            chooseFamilyPopup?.updateFamilyList(it)


        })

        viewModel.motorLiveData?.observe(this, androidx.lifecycle.Observer {
            when (it.getStatus()) {
                MotorLiveData.UserStatus.RESPOSNSE_SUCCESS -> {
//                        showMsg(it?.statusMessage!!)
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                MotorLiveData.UserStatus.ERROR -> {
                    val error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    showMsg(error?.getErrorMessage() ?:getString(R.string.something_went_wrong))
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.YEAR_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.RENEW_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.CLAIM_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }

                MotorLiveData.UserStatus.YEAR_ERROR -> {
                    val error = it.getError()
                    showMsg(error?.getErrorMessage() ?: getString(R.string.something_went_wrong))
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.PAYMENT_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    val bundle = bundleOf(Constants.PAYMENT_URL to it.statusMessage)
                    navController?.navigate(R.id.action_payment_webview, bundle)
                }
                MotorLiveData.UserStatus.ADD_MEMBER_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    viewModel.getFamilyList()
                }
                MotorLiveData.UserStatus.POLICY_TYPE_SUCCESS -> {
                    if (plan != null && !viewModel.actualpolicyOption.isNullOrEmpty()) {
                        var i = 0
                        for (item in viewModel.actualpolicyOption) {
                            if (plan!!.equals(item.text!!.trim())) {
                               viewModel.selectedPolicyOptions.postValue(i)
                            }
                            i++
                        }

                    }
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })
        if(null!=arguments){
            viewModel.firstName.postValue(arguments?.get("firstName").toString())
            viewModel.secondName.postValue(arguments?.get("secondName").toString())
            viewModel.familyName.postValue(arguments?.get("familyName").toString())
            viewModel.emailId.postValue(arguments?.get("email").toString())
            viewModel.mobileNumber.postValue(arguments?.get("mobileNumber").toString())
            viewModel.selectedNationality.postValue(arguments?.get("nationalityID") as Int)
            viewModel.civiliId.postValue(arguments?.get("civilID").toString())
            viewModel.pssportNo.postValue(arguments?.get("passportNo").toString())
            viewModel.doB.postValue(arguments?.get("dob").toString())
            plan = arguments?.getString("plan")
        }
        viewModel.getRelation()
        viewModel.getPolicyOptions()
        viewModel.getPolicyType()
        viewModel.getTravelPolicyPeriods()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


    }

    private fun checkDobIs80(maxAge: String): Boolean {

        var dateOfBirth = DateUtil.myFormat.parse(viewModel.doB.value)
        var travelDate = DateUtil.myFormat.parse(viewModel.startDate.value)

        if(DateUtil.getDiffYears(dateOfBirth,travelDate) >Integer.parseInt(maxAge)){
            return true
        }else{
            return false
        }

    }

    private fun setFamilySelectedAdapter(it: ArrayList<Datum>?) {
       updateadapter(it)
    }



    fun setToDate() {
        if(!viewModel.startDate.value.isNullOrEmpty() && viewModel.selectedPolicyPeriod.value!=-1){
            getDayAfter(viewModel.startDate.value!!, viewModel.actualPolicyPeriod.get(viewModel.selectedPolicyPeriod.value!!).text)
        }
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
                    viewModel.dateOfBirth.postValue(currentDate)
                },
                myCalendar
                        .get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().maxDate = System.currentTimeMillis()
        picker.show()
    }

    fun onAddMemberClick(){
        var dialogs: Dialog? = null
            dialogs = activity?.let { Dialog(it, R.style.customAlertDialog) }
            // where "this" is the context
            if (null != dialogs) {
                val binding: AddFamilyMemberBinding =
                        DataBindingUtil.inflate(
                                dialogs!!.layoutInflater,
                                R.layout.add_family_member,
                                null,
                                false
                        )
                binding.viewModel = viewModel
                binding.fragment = this
                binding.lifecycleOwner=this
                dialogs?.setContentView(binding.root)
                binding.txtGarage.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        dialogs.dismiss()
                    }
                })
                binding.txtNotify.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if (binding.spinnerMemberRelation.selectedItem.equals(getString(R.string.string_son)) || binding.spinnerMemberRelation.selectedItem.equals(getString(R.string.string_daughter))) {
                            if (isDateIsValidForSonOrDaughter()) {
                                showMsg(binding.spinnerMemberRelation.selectedItem.toString()+getString(R.string.age_error_18))
                            } else {
                                if(isAllFieldEntered()) {
                                    viewModel.AddFamilyMember()
                                    dialogs.dismiss()
                                }
                            }
                        } else {
                            if(isAllFieldEntered()) {
                                viewModel.AddFamilyMember()
                                dialogs.dismiss()
                            }
                        }
                    }
                })
                dialogs?.show()
            }

    }
    private fun isAllFieldEntered(): Boolean {
        if(viewModel.familyNameMember.value.isNullOrEmpty() || viewModel.familyNameMember?.value!!.length <3){
           IlafCommonAlert(requireActivity(),getString(R.string.name_lenght),getString(R.string.ok),null,null).show()
            return false
        }else if(viewModel.pssportNoMember.value.isNullOrEmpty()){
            IlafCommonAlert(requireActivity(),getString(R.string.passport_no_empty),getString(R.string.ok),null,null).show()
            return false
        }else if(viewModel.selectedrelationId.value==-1){
            IlafCommonAlert(requireActivity(),getString(R.string.relation_emoty),getString(R.string.ok),null,null).show()
            return false
        }else if(viewModel.dateOfBirth.value.isNullOrEmpty()){
            IlafCommonAlert(requireActivity(),getString(R.string.dob_empty),getString(R.string.ok),null,null).show()
            return false
        }else{
            return true
        }
    }

    private fun isDateIsValidForSonOrDaughter(): Boolean {
        var cal = Calendar.getInstance()
        var dateOfBirth = DateUtil.myFormat.parse(viewModel.dateOfBirth.value)

        if(DateUtil.getDiffYears(dateOfBirth,cal.time) >=18){
            return true
        }else{
            return false
        }
    }

    private fun getDayAfter(value: String, text: String?) {
        var myCalendar= Calendar.getInstance()
        myCalendar.time = DateUtil.myFormat.parse(value)
        if(text.equals(getString(R.string.one_week)))
        myCalendar.add(Calendar.DAY_OF_YEAR, +6)
        else if(text.equals(getString(R.string.fifteen_days)))
        myCalendar.add(Calendar.DAY_OF_YEAR, +14)
        else if(text.equals(getString(R.string.one_month))) {
            myCalendar.add(Calendar.MONTH, +1)
            myCalendar.add(Calendar.DATE, -1)
        }
        else if(text.equals(getString(R.string.two_months))){
            myCalendar.add(Calendar.MONTH, +2)
            myCalendar.add(Calendar.DATE, -1)
        }
        else if(text.equals(getString(R.string.three_months))){
            myCalendar.add(Calendar.MONTH, +3)
            myCalendar.add(Calendar.DATE, -1)
        }
        else if(text.equals(getString(R.string.six_months))){
            myCalendar.add(Calendar.MONTH, +6)
            myCalendar.add(Calendar.DATE, -1)
        }
        else if(text.equals(getString(R.string.two_yrs))) {
            myCalendar.add(Calendar.YEAR, +2)
            myCalendar.add(Calendar.DATE, -1)
        }
        else if(text.equals(getString(R.string.three_yrs))){
            myCalendar.add(Calendar.YEAR, +3)
            myCalendar.add(Calendar.DATE, -1)
        }
        else{
            myCalendar.add(Calendar.YEAR, +1)
            myCalendar.add(Calendar.DATE, -1)
        }
        val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
        viewModel.toDate.postValue(currentDate)

    }

    fun onStartDateClicked(view: View){
        var myCalendar= Calendar.getInstance()
        var picker = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                    viewModel.startDate.postValue(currentDate)
                },
                myCalendar
                        .get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().minDate = System.currentTimeMillis()
        var cal =Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR,+viewModel.maxStartDays.value!!.toInt())
            picker.getDatePicker().maxDate= cal.timeInMillis
        picker.show()
    }

    fun showFamilyMemberPopup(){
        if(!binding?.txtLayoutPolicyOption?.selectedItem.toString().equals(getString(R.string.individual))) {
            chooseFamilyPopup =
                ChooseFamilyPopup(requireContext(), object : ChooseFamilyPopup.IlafDialogListener {
                    override fun onDialogPositiveClick(familyListSelected: ArrayList<Datum>) {
                        viewModel.familyMembersList.clear()
                        viewModel.familyMembersList.addAll(familyListSelected)
                        viewModel.setFamilyList(familyListSelected)
                        // updateadapter(familyListSelected)
                    }

                    override fun onDialogNegativeClick() {
                    }

                    override fun onEditClicked() {
                        findNavController().navigate(R.id.action_show_edit_family_members)
                    }

                }, viewModel.familyList.value ?: emptyList())
            chooseFamilyPopup?.show()
        }else{
            showMsg(getString(R.string.idividual_policy_error))
        }

    }


    override fun onItemClick(v: View, obj: Any) {
        var data = obj as Datum
        viewModel.familyMembersList.remove(data)
        for(item in viewModel.familyList.value!!){
            if(item.familyMemberID == data.familyMemberID)
            item.isSelected=false
        }
        updateadapter(viewModel.familyMembersList)

    }

    fun updateadapter(it: ArrayList<Datum>?) {
        familyMemberPolicyAdapter.onClick =this
        familyMemberPolicyAdapter.setFamilyMembersSelectedListData(it ?: emptyList())
        binding?.rvFamilyList?.adapter = familyMemberPolicyAdapter
    }

    fun onPaymentSubmit(){
         if(validationsDone()){
             viewModel.onPaymentSubmit()
         }
    }

    private fun validationsDone(): Boolean {
        if(!viewModel.isTermsChecked.value!!){
            IlafCommonAlert(requireActivity(),getString(R.string.confirm_terms_and_conditions),getString(R.string.ok),null,null).show()
            return false
        }  else if(viewModel.isAgeError.value!!){
            var maxAge = IlafSharedPreference(requireActivity()).getStringPrefValue(IlafSharedPreference.Constants.MAX_AGE)?:"80"
            IlafCommonAlert(requireActivity(),getString(R.string.age_error) + maxAge+ "  " +getString(R.string.age_error_maxage) ,getString(R.string.ok),null,null).show()
            return false
        }
        else if(viewModel.startDate.value.isNullOrEmpty()){
            IlafCommonAlert(requireActivity(),getString(R.string.start_date_error),getString(R.string.ok),null,null).show()
            return false
        }
        else if(viewModel.toDate.value.isNullOrEmpty()){
            IlafCommonAlert(requireActivity(),getString(R.string.start_date_error),getString(R.string.ok),null,null).show()
            return false
        }
        return true
    }


}