package com.ilaftalkful.mobileonthego.base

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.customcomponents.IlafImagePickAlert
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.Utility
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.IlafBaseViewModel
import java.io.ByteArrayOutputStream


open class IlafBaseFragment : Fragment() ,IlafImagePickAlert.ImageSelectionListener{

    val baseViewModel: IlafBaseViewModel by viewModels()

    lateinit var ilafSharedPreference: IlafSharedPreference
    var dialog: IlafCommonAlert? = null
    var cameraPickPopup:IlafImagePickAlert?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    open fun onBackClicked(view: View) {
        (activity as SplashActivity).onBackPressed()
    }

    fun showMsg(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

    open fun onEnglishClicked(view: View) {
    }

    open fun onArabicClicked(view: View) {

    }

    open fun onRegisterOrLoginClicked(view: View) {
        IlafSharedPreference(requireContext()).setBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER,false)


    }

    open fun onHomeClicked(view: View) {

    }

    open fun onNotificationClicked(view: View) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            Constants.PERMISSION_FINGER_PRINT ->{
                loginUsingFingerPrint()
            }
            Constants.CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraStartCall()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        Utility.permissonList,
                        Constants.CAMERA_PERMISSION_REQUEST_CODE
                    )
                    // requestPermissions(Utility.permissonList, Constants.CAMERA_PERMISSION_REQUEST_CODE);
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_needed),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            Constants.PHONE_CALL -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall()
                } else {
                    /* requestPermissions(requireActivity(),
                             Utility.permissonList,
                             Constants.CAMERA_PERMISSION_REQUEST_CODE
                     )*/
                    // requestPermissions(Utility.permissonList, Constants.CAMERA_PERMISSION_REQUEST_CODE);
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_needed),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
           Constants.EXTERNAL_READ_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val cameraIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(cameraIntent, Constants.PHONE_GALLERY_INTENT)
                } else {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.permission_needed),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
            }

            Constants.PERMISSION_FINGER_PRINT -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loginUsingFingerPrint()
                } else {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.permission_needed),
                            Toast.LENGTH_SHORT
                    )
                            .show()
                }
            }
        }
    }

    open fun loginUsingFingerPrint() {

    }

    open fun makePhoneCall() {

    }


    fun cameraStartCall() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, Constants.CAMERA_INTENT)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                Constants.CAMERA_INTENT -> {
                    try {
                        if (resultCode == Activity.RESULT_OK) {
                            cameraPickPopup?.dismiss()
                            val imageBitmap = data?.extras?.get(Constants.IMAGE_DATA) as Bitmap
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            imageBitmap.compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                byteArrayOutputStream
                            )
                            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            setImage(imageBitmap, encoded)
                        }
                    } catch (e: OutOfMemoryError) {
                        showMsg("You are running low memory")
                    }

                }
               Constants.PHONE_GALLERY_INTENT -> {
                    try {
                        cameraPickPopup?.dismiss()
                        if (data?.data != null) {
                            val selectedImageUri = data.data
                            val bitmap: Bitmap =
                                    selectedImageUri?.let {
                                        Utility.getBitmapFromUri(
                                                requireContext(),
                                                it
                                        )
                                    }!!
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            bitmap.compress(
                                    Bitmap.CompressFormat.PNG,
                                    100,
                                    byteArrayOutputStream
                            )
                            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            setImage(bitmap, encoded)
                        } else {
                            val imageBitmap = data?.extras?.get(Constants.IMAGE_DATA) as Bitmap
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            imageBitmap.compress(
                                    Bitmap.CompressFormat.PNG,
                                    100,
                                    byteArrayOutputStream
                            )
                            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            setImage(imageBitmap, encoded)

                        }
                    } catch (e: java.lang.Exception) {

                    }
                }


            }
        }

    }

    open fun setImage(imageBitmap: Bitmap, encoded: String) {

    }

    open fun askPermissionOrLaunchCamera() {
        val isCameraPermission = Utility.hasPermissions(requireContext())
        if (isCameraPermission) {
            cameraStartCall()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                Utility.permissonList,
                Constants.CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun sessionExpiryHandle() {
        dialog = IlafCommonAlert(
            requireActivity(),
            getString(R.string.session_expiry),
            getString(R.string.ok),
            null,
            object : IlafCommonAlert.IlafDialogListener {
                override fun onDialogPositiveClick() {
                    dialog?.dismiss()
                    moveToLogin()
                }

                override fun onDialogNegativeClick() {
                    dialog?.dismiss()
                }

            })
        dialog?.setCancelable(false)
        dialog?.show()
    }

    open fun moveToLogin() {
    }

    open fun onContctNumberClicked(view: View) {

    }

    open fun onEmailClicked(view: View) {

    }
    fun onUploadImageClick(){
        if(cameraPickPopup==null) {
            cameraPickPopup = IlafImagePickAlert(requireContext(), this)
        }
        cameraPickPopup?.show()
    }

    fun refreshConfigChange() {
       // val intent = activity?.intent
        //intent?.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
       // activity?.overridePendingTransition(0, 0)
     //   startActivity(intent)
       // getActivity()?.recreate();
       // activity?.recreate()
        (activity as SplashActivity).loadLanguage()
        activity?.recreate()
    }

    override fun imagePickFromCamera() {
        askPermissionOrLaunchCamera()
    }

    override fun imagePickFromPhoneGallery() {
        val isReadPermission = Utility.hasPermissions(requireContext())
        if(isReadPermission){
            cameraPickPopup?.dismiss()
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, Constants.PHONE_GALLERY_INTENT)

        }else{
            requestPermissions( Utility.permissonList, Constants.EXTERNAL_READ_PERMISSION_REQUEST_CODE);
        }
    }
}