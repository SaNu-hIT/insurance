package com.ilaftalkful.mobileonthego.view.family

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
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
import com.ilaftalkful.mobileonthego.databinding.EditFamilyListFragmentBinding
import com.ilaftalkful.mobileonthego.databinding.EditFamilyMemberAlertBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.model.family.FamilyErros
import com.ilaftalkful.mobileonthego.model.family.FamilyParameter
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafValidator
import com.ilaftalkful.mobileonthego.view.SplashActivity
import java.util.*


class EditFamilyListFragment : IlafBaseFragment(), OnItemClickListener {
    var familyBinding: EditFamilyListFragmentBinding? = null
    val viewModel: EditFamilyListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        familyBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.edit_family_list_fragment,
            container,
            false
        )
        familyBinding?.lifecycleOwner = this
        familyBinding?.viewModel = viewModel
        familyBinding?.fragment = this
        familyBinding?.executePendingBindings()
        familyBinding?.label =getString(R.string.family_list)
        return familyBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRelationType()
        setAdapter()
        observedata()
    }

    private fun setRelationType() {
        viewModel.relationType.clear()
        viewModel.relationType.add(getString(R.string.select_one))
        viewModel.relationType.add(getString(R.string.father))
        viewModel.relationType.add(getString(R.string.mother))
        viewModel.relationType.add(getString(R.string.brother))
        viewModel.relationType.add(getString(R.string.sister))
        viewModel.relationType.add(getString(R.string.string_son))
        viewModel.relationType.add(getString(R.string.string_daughter))
        viewModel.relationType.add(getString(R.string.grand_parents))
        viewModel.relationType.add(getString(R.string.grand_children))
    }

    var adapter: FamilyMemberAdapter? = null
    private fun setAdapter() {
        if (adapter == null) {
            adapter = FamilyMemberAdapter(context)
            adapter?.onClick = this
            familyBinding?.recyclerView?.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

    private fun observedata() {

        viewModel.getFamilyist()
        viewModel.relationShips()

        viewModel.logRespone.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter?.setPlansData(it)
                adapter?.notifyDataSetChanged()
            }
        })

        viewModel.relationList.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                adapter?.setRelationShip(it)
            }
        })


        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.SESSION_EXPIRED -> {
                    if (null != dialogs) {
                        if(dialogs?.isShowing?:false){
                        }
                    }
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.FAMILY_LIST_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false);
                }
                UserData.UserStatus.EDIT_FAMILY_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    if (null != dialogs) {
                        dialogs?.dismiss()
                    }
                    viewModel.getFamilyist()
                }
                UserData.UserStatus.ADD_FAMILY_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    if (null != dialogs) {
                        dialogs?.dismiss()
                    }
                    viewModel.getFamilyist()
                }


                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    if (null != dialogs) {
                        dialogs?.dismiss()
                    }
                }
                UserData.UserStatus.FAMILY_REFRESH -> {
                    (activity as SplashActivity).toggleLoader(false)
                    viewModel.getFamilyist()
                    adapter?.notifyDataSetChanged()
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    if (null != dialogs) {
                    }
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.CORPORATE_QOUTE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    if (null != dialogs)
                        dialogs?.dismiss()
                    viewModel.clearAll()
                    showMsg(getString(R.string.staf_will_contact))
                }
                UserData.UserStatus.ERROR -> {
                    if (null != dialogs) {
                    }
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(), error?.errorMessage?:getString(R.string.something_went_wrong), getString(R.string.ok), null, null).show()

                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })

    }

    var binding: EditFamilyMemberAlertBinding? = null
    var dialogs: Dialog? = null
    fun showAddFamilyAlert(view: View) {
        dialogs = activity?.let { Dialog(it, R.style.customAlertDialog) }
        // where "this" is the context
        if (null != dialogs) {
            binding = DataBindingUtil.inflate(
                dialogs!!.layoutInflater,
                R.layout.edit_family_member_alert,
                null,
                false
            )
            binding?.viewModel = viewModel
            binding?.data = FamilyParameter()
            binding?.fragment = this
            binding?.lifecycleOwner=this
            binding?.error = FamilyErros(null)
            binding?.root?.let { dialogs?.setContentView(it) }
            dialogs?.show()
        }

    }

    override fun onItemClick(v: View, obj: Any) {
        if (obj is Datum) {
            if (v.id == R.id.delete_id) {
                viewModel.familyMemberDelete(obj)
            } else if (v.id == R.id.save_id) {
                if (isDateIsValidForSonOrDaughterFromEdit(obj.dob)) {
                    showMsg(obj.relationDescription+ "  " +getString(R.string.age_error_18))
                }else {
                    var qoute = FamilyParameter()
                    qoute.fullName = obj.fullName

                    qoute.relationID = viewModel.actualrelationList?.get(obj.relationID)?.id!!
                    qoute.passportNumber = obj.passportNumber
                    qoute.familyMemberID = obj.familyMemberID.toString()
                    qoute.dob = obj.dob
                    if(isAllFieldEntered(qoute)){
                        viewModel.editFamily(qoute)
                    }
                }
            } else if (v.id == R.id.editTextTime) {
                onDateClick(obj)
            } else {
                viewModel.updateData(obj)
            }
        }


    }

    private fun isAllFieldEntered(qoute: FamilyParameter): Boolean {
        if(qoute.fullName.isNullOrEmpty() || qoute.fullName.length<3){
            showMsg(getString(R.string.name_lenght))
            return false
        }else if(qoute.passportNumber.isNullOrEmpty()){
            showMsg(getString(R.string.passport_no_empty))
            return false
        }else if(qoute.relationID==-1){
            showMsg(getString(R.string.relation_emoty))
            return false
        }else if(qoute.dob.isNullOrEmpty()){
            showMsg(getString(R.string.dob_empty))
            return false
        }else{
            return true
        }
    }

    private fun isDateIsValidForSonOrDaughter(dob: String): Boolean {
        var cal = Calendar.getInstance()
        var dateOfBirth =DateUtil.myFormat.parse(dob)

        if(DateUtil.getDiffYears(dateOfBirth,cal.time) >=18){
            return true
        }else{
            return false
        }
    }

    private fun isDateIsValidForSonOrDaughterFromEdit(dob: String): Boolean {
        var cal = Calendar.getInstance()
        var dateOfBirth = DateUtil.myFormat.parse(DateUtil.getStringDateToFromat(dob))

        if(DateUtil.getDiffYears(dateOfBirth,cal.time) >=18){
            return true
        }else{
            return false
        }
    }
    fun addFamilyCreate(view: View, erros: FamilyErros, datum: FamilyParameter) {
        datum.relationID = viewModel.actualrelationList.get(viewModel.selectedAddFamilyrelationId.value!!).id!!
        datum.dob =viewModel.mDOB.value
        datum.fullName = viewModel.mfullName.value
        datum.passportNumber = viewModel.mpassportNumber.value
        if(isAllFieldEntered(datum)){
            postFamilyCreate(view,erros,datum)
            dialogs?.dismiss()
        }
    }

    fun postFamilyCreate(view: View, erros: FamilyErros, datum: FamilyParameter) {
        if (binding?.relationSpinner?.selectedItem?.equals(getString(R.string.string_son))?:false || binding?.relationSpinner?.selectedItem?.equals(getString(R.string.string_daughter))?:false) {
            if (isDateIsValidForSonOrDaughter(datum.dob)) {
                showMsg(binding?.relationSpinner?.selectedItem.toString()+ "  " +getString(R.string.age_error_18))
            } else {
                erros.mFullNameError = IlafValidator.isValidName(viewModel.mfullName.value!!,requireActivity())
                erros.mPassportNumberError =
                        IlafValidator.isValidPasportNumber(viewModel.mpassportNumber.value!!,requireActivity())
                erros.mDOBNameError = IlafValidator.isValidDOBNameError(viewModel.mDOB.value!!,requireActivity())

                viewModel.postFamilyCreate(erros, datum, false)
            }
        } else {
            viewModel.postFamilyCreate(erros, datum, false)
        }

    }

    fun onCancelClick(view: View) {
        if (null != dialogs)
            dialogs?.dismiss()
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

    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    override fun moveToLogin() {
        val bundle = bundleOf(Constants.IS_FROM_SCREENS to true)
        findNavController().navigate(R.id.login_fragment, bundle)

    }

    fun onDateClick(data: Datum) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.fromServer.format(myCalendar.getTime())
                data.setDOB(currentDate)
                data.isSelected = true
                adapter?.notifyDataSetChanged()
            },
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show()
    }

    fun onDateOfSicknessClicked(view: View, data: FamilyParameter) {
        var myCalendar = Calendar.getInstance()
        var picker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())

                viewModel.mDOB.postValue(currentDate)
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