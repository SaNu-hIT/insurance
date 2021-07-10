package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafAudioRecorderDialog
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.MotorClaimFragmentBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.AccidentPhotosModel
import com.ilaftalkful.mobileonthego.model.TravelClaimErrors
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorLiveData
import com.ilaftalkful.mobileonthego.utilities.*
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.MotorClaimViewModel
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class MotorClaimFragment : IlafBaseFragment(), OnItemClickListener {

    val viewModel: MotorClaimViewModel by viewModels()
     var   motorClaimFragmentBinding : MotorClaimFragmentBinding?=null
    var imageUploading:AccidentPhotosModel?=null
    var adapter:VehicleDamagePhotosAdapter?=null
    var navController:NavController?=null
     var recorder: MediaRecorder?=null
    var errors :TravelClaimErrors =TravelClaimErrors()
    lateinit var  data :ArrayList<AccidentPhotosModel>
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        motorClaimFragmentBinding = DataBindingUtil.inflate<MotorClaimFragmentBinding>(
                inflater,
                R.layout.motor_claim_fragment,
                container,
                false
        )
        motorClaimFragmentBinding?.viewModel=viewModel
        motorClaimFragmentBinding?.lifecycleOwner=this
        motorClaimFragmentBinding?.fragment=this
        motorClaimFragmentBinding?.error=errors
        motorClaimFragmentBinding?.label = getString(R.string.claims)
        return motorClaimFragmentBinding?.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        motorClaimFragmentBinding?.txtLayoutPolicyNo?.setHint(getString(R.string.policy_no))
        motorClaimFragmentBinding?.txtLayoutAccidentDate?.setHint(getString(R.string.date_of_accident))
        motorClaimFragmentBinding?.txtLayoutPoliceReport?.setHint(getString(R.string.police_station_report_papper))
        motorClaimFragmentBinding?.txtLayoutReg?.setHint(getString(R.string.vehicle_registration))
        motorClaimFragmentBinding?.txtLayoutCarOwnerCivilId?.setHint(getString(R.string.car_owner_civil_id))
        motorClaimFragmentBinding?.txtLayoutCivilidOfDriver?.setHint(getString(R.string.drive_id))
        motorClaimFragmentBinding?.txtLayoutDriverIdOfDriver?.setHint(getString(R.string.driver_license_who_drive_at_the_time_of_acident))
        motorClaimFragmentBinding?.layoutDescription?.setHint(getString(R.string.description))
    }


    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
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

    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        viewModel.getPolicyList()
        setAdapter()

        viewModel.policiesList.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1, it
            )
            motorClaimFragmentBinding?.autoPolicyNo?.setAdapter(adapter)

        })

        motorClaimFragmentBinding?.autoPolicyNo?.setOnItemClickListener(object :
                AdapterView.OnItemClickListener {
            override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                val policyId = viewModel.actualPolicyList.get(position).motorPolicyID
                viewModel.policyId.postValue(policyId)
                viewModel.getPolicyDetails(position)
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
                    showMsg(error?.getErrorMessage() ?: getString(R.string.something_went_wrong))
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
                    IlafCommonAlert(requireActivity(),error?.getErrorMessage()?:getString(R.string.something_went_wrong),getString(R.string.ok),null,object : IlafCommonAlert.IlafDialogListener{
                        override fun onDialogPositiveClick() {
                            findNavController()?.popBackStack()
                        }

                        override fun onDialogNegativeClick() {

                        }

                    }).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.CLAIM_IMAGE_UPLOAD_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                MotorLiveData.UserStatus.CLAIM_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(
                            requireActivity(),
                            it.statusMessage,
                            getString(R.string.ok),
                            null,
                            object : IlafCommonAlert.IlafDialogListener {
                                override fun onDialogPositiveClick() {
                                    navController?.popBackStack()
                                }

                                override fun onDialogNegativeClick() {

                                }

                            }).show()
                }
                MotorLiveData.UserStatus.FRONT_BUMPER_IMEGE_ERROR->{
                    showMsg(getString(R.string.front_bumper))
                }
                MotorLiveData.UserStatus.REAR_BUMPER_IMEGE_ERROR->{
                    showMsg(getString(R.string.rear_bumper))
                }
                MotorLiveData.UserStatus.FULL_VIEW_IMEGE_ERROR->{
                    showMsg(getString(R.string.full_view_error))
                }
                MotorLiveData.UserStatus.ACCIDENT_SITE_IMEGE_ERROR->{
                    showMsg(getString(R.string.accident_site_error))

                }
                MotorLiveData.UserStatus.PASSENGER_SIDE_IMEGE_ERROR->{
                    showMsg(getString(R.string.passenger_side_error))
                }
                MotorLiveData.UserStatus.DRIVER_SIDE_IMEGE_ERROR->{
                    showMsg(getString(R.string.driver_side_error))
                }
                MotorLiveData.UserStatus.REAR_SIDE_IMEGE_ERROR->{
                    showMsg(getString(R.string.rear_side_error))
                }
                MotorLiveData.UserStatus.FRONT_SIDE_IMEGE_ERROR->{
                    showMsg(getString(R.string.front_side_error))
                }
                MotorLiveData.UserStatus.POLICY_FIRST_PAGE_IMEGE_ERROR->{
                    errors.policyFirstPageError=getString(R.string.upload_first_policy)
                }
                MotorLiveData.UserStatus.POLICE_REPORT_IMEGE_ERROR->{
                    errors.policeReportError=getString(R.string.upload_police_report)

                }
                MotorLiveData.UserStatus.VEHICLE_REG_IMAGE_ERROR->{
                    errors.vehicleRegError=getString(R.string.upload_vehicle_reg)

                }
                MotorLiveData.UserStatus.CAR_OWNER_ID_IMEGE_ERROR->{
                    errors.carOwnerCivilIdError=getString(R.string.upload_owner_civil)

                }
                MotorLiveData.UserStatus.DRIVER_DRIVER_ID_IMEGE_ERROR->{
                    errors.driverDriverIdError=getString(R.string.driver_license)

                }
                MotorLiveData.UserStatus.DRIVER_CIVIL_ID_IMEGE_ERROR->{
                    errors.driverCivilIdError=getString(R.string.upload_dricer_civil)

                }
            }
        })
    }
    override fun moveToLogin() {
        val bundle = bundleOf(Constants.IS_FROM_SCREENS to true)
        navController?.navigate(R.id.login_fragment, bundle)

    }
    private fun setAdapter() {
         data = viewModel.getPhotos()
        getActualData(data)
        if(adapter ==null) {
            adapter = VehicleDamagePhotosAdapter()
            adapter?.setPlansData(data)
            adapter?.onClick = this
            motorClaimFragmentBinding?.photoList?.adapter = adapter
        }else{
            adapter?.notifyDataSetChanged()
        }
    }

    private fun getActualData(data: ArrayList<AccidentPhotosModel>){
        for(item in data){
            if(item.id == 0)
            item.sideName = getString(R.string.front)
           else if(item.id == 1)
           item.sideName = getString(R.string.rear_side)

            else if(item.id == 2)
            item.sideName = getString(R.string.side_driver)

           else if (item.id == 3)
            item.sideName = getString(R.string.side_passenger)

            else if(item.id == 4)
            item.sideName = getString(R.string.accident_site)

            else if (item.id == 5)
            item.sideName = getString(R.string.full_view)

            else if (item.id == 6)
            item.sideName = getString(R.string.bumber_back)
            else if (item.id == 7)
            item.sideName =getString(R.string.bumber_front)

        }

    }

    override fun onItemClick(v: View, obj: Any) {
        when(v.id){
            R.id.header_view -> {
                imageUploading = obj as AccidentPhotosModel
                viewModel.imageType.postValue(obj.id)
                askPermissionOrLaunchCamera()
            }
        }
    }
    override fun setImage(imageBitmap: Bitmap, encoded: String) {
        if(viewModel.imageType.value!!>=0 && viewModel.imageType.value!! <=7) {
            imageUploading?.imageBitmap = imageBitmap
            viewModel.removeItem(imageUploading?.id, data)
            data.add(imageUploading!!)
            data.sortBy { it.id }
            adapter?.notifyDataSetChanged()
        }
        viewModel.uploadImage(encoded)
    }

    fun onDriverIdDriverClick(view: View){
        viewModel.imageType.postValue(Constants.DRIVER_DRIVER_ID)
        onUploadImageClick()
    }

    fun onCivilIdDriverClick(view: View){
        viewModel.imageType.postValue(Constants.DRIVER_CIVIL_ID)
        onUploadImageClick()
    }

    fun onCarOwnerIdClick(view: View){
        viewModel.imageType.postValue(Constants.CAR_OWNER_ID)
        onUploadImageClick()
    }

    fun onVehicleRegImagePick(view: View){
        viewModel.imageType.postValue(Constants.VEHICLE_REG)
        onUploadImageClick()
    }

    fun onPoliceReportImagePick(view: View){
        viewModel.imageType.postValue(Constants.POLICE_REPORT)
        onUploadImageClick()
    }

    fun onPolicyFirstPageClicked(view: View){
        viewModel.imageType.postValue(Constants.POLICY_FIRST_PAGE)
        onUploadImageClick()
    }

    fun onNotifyClaimClicked(view: View, error: TravelClaimErrors){
        errors.policyNoError = IlafValidator.isValidPolicy(viewModel.policyNo.value!!,requireActivity())
        errors.policeReportError = IlafValidator.isValidPoliceReport(viewModel.policeReport.value!!,requireActivity())
        if (viewModel.accidentDate.value.toString().isNotEmpty()) {
            errors.accidentDateError =
                    IlafValidator.isValidDate(DateUtil.getDateTorequestFromat(viewModel.accidentDate.value!!),requireActivity())
        } else {
            errors.accidentDateError = context?.getString(R.string.could_not_be_empty)
        }
        errors.vehicleRegError = IlafValidator.isValidPolicy(viewModel.vehicleReg.value!!,requireActivity())
        errors.carOwnerCivilIdError = isValidCivilId(viewModel.civilIdCarOwner.value!!)
        errors.driverCivilIdError = isValidCivilId(viewModel.driverCivilId.value!!)
        errors.driverDriverIdError = IlafValidator.isValidPolicy(viewModel.driverDriverId.value!!,requireActivity())
        errors.termsAndConditionsError =
                if (!viewModel.isTermsChecked.value!!) context?.getString(R.string.check_terms_and_conditions) else null
        viewModel.isVisible.postValue(!viewModel.isTermsChecked.value!!)

        if(viewModel.isAllImagesAttahced()) {
            viewModel.notifyClaim(error)
        }
    }

    private fun isValidCivilId(civilId: String): String? {
        var errorMSg: String? = null
        if (civilId.length < 12) {
            errorMSg = context?.getString(R.string.invalid_civil_id)
        }else {
            var civilIdDob = civilId.substring(1,7)
            if(civilIdDob.substring(0,2).toInt()<=99){
                if(civilIdDob.substring(2,4).toInt()<=12 && civilIdDob.substring(2,4).toInt()>0){
                    if( civilIdDob.substring(2,4).toInt().toInt()==2) {
                        if(civilIdDob.substring(0,2).toInt() %4==0 ) {
                            if (civilIdDob.substring(4, 6).toInt() <= 29 && civilIdDob.substring(4,6)
                                    .toInt() > 0
                            ) {
                                errorMSg = null
                            }else {
                                errorMSg = context?.getString(R.string.invalid_civil_id)
                            }
                        }else{
                            if (civilIdDob.substring(4, 6).toInt() <= 28 && civilIdDob.substring(4,6)
                                    .toInt() > 0
                            ) {
                                errorMSg = null
                            }else {
                                errorMSg = context?.getString(R.string.invalid_civil_id)
                            }
                        }
                    } else if(civilIdDob.substring(2,4).toInt().toInt()==7 || civilIdDob.substring(2,4).toInt().toInt()==8 || civilIdDob.substring(2,4).toInt().toInt()==10
                            ||civilIdDob.substring(2,4).toInt().toInt()==12) {
                            if(civilIdDob.substring(4,6).toInt()<=31 && civilIdDob.substring(4,6).toInt()>0){
                                errorMSg =null
                            }else {
                                errorMSg = context?.getString(R.string.invalid_civil_id)
                            }
                        }else if(civilIdDob.substring(2,4).toInt().toInt()==1 ||civilIdDob.substring(2,4).toInt().toInt()==3||civilIdDob.substring(2,4).toInt().toInt()==5) {
                            if(civilIdDob.substring(4,6).toInt()<=31 && civilIdDob.substring(4,6).toInt()>0){
                                errorMSg =null
                            }else {
                                errorMSg = context?.getString(R.string.invalid_civil_id)
                            }
                        }else {
                            errorMSg = context?.getString(R.string.invalid_civil_id)
                        }
                }else {
                    errorMSg = context?.getString(R.string.invalid_civil_id)
                }
            }else {
                errorMSg = context?.getString(R.string.invalid_civil_id)
            }

        }

        return errorMSg
    }

    fun onGarageClicked(view: View){
        val bundle = bundleOf( Constants.ISFROM_MOTOR_CLAIM to true)
        navController?.navigate(R.id.action_show_garage,bundle)
    }
    fun  onAccidentDate(view: View){
        var myCalendar=Calendar.getInstance()
       var picker = DatePickerDialog(
               requireActivity(),
               DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                   myCalendar.set(Calendar.YEAR, year)
                   myCalendar.set(Calendar.MONTH, monthOfYear)
                   myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                   val currentDate: String = DateUtil.myFormat.format(myCalendar.getTime())
                   viewModel.accidentDate.postValue(currentDate)
               },
               myCalendar
                       .get(Calendar.YEAR),
               myCalendar.get(Calendar.MONTH),
               myCalendar.get(Calendar.DAY_OF_MONTH)
       )
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onAudioRecordClick(view: View){
        val isCameraPermission = Utility.hasPermissions(requireContext())
        if (isCameraPermission) {
            val AudioName =System.currentTimeMillis().toString() + MediaRecorder.OutputFormat.THREE_GPP
            val folder = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_AUDIOBOOKS), "Audio")
            if (!folder.exists()) {
                folder.mkdirs()
            }
            var filename = File(folder, AudioName)
            filename.delete() // Delete the File, just in Case, that there was still another File
            filename.createNewFile()
            IlafAudioRecorderDialog(requireContext(),
                    object : IlafAudioRecorderDialog.IlafDialogListener {
                        override fun onStopClicked() {
                            stopRecording(filename)
                        }

                        override fun onStartClicked() {

                            recorder = MediaRecorder().apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                setOutputFile(filename)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                                try {
                                    prepare()
                                } catch (e: IOException) {
                                }

                                start()
                            }
                        }

                    }).show()

        } else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    Utility.permissonList,
                    Constants.CAMERA_PERMISSION_REQUEST_CODE
            )
        }



    }
    private fun stopRecording(filename: File) {
        viewModel.imageType.postValue(Constants.VOICE_NOTE)
        recorder?.apply {
            stop()
            release()

            val bytes = ByteArray(filename?.length().toInt())
            try {
                val buf = BufferedInputStream(FileInputStream(filename))
                buf.read(bytes, 0, bytes.size)
                buf.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var audioString =Base64.encodeToString(bytes, Base64.DEFAULT)
            viewModel.uploadImage(audioString, ".mp4")
        }
        recorder = null
    }


}