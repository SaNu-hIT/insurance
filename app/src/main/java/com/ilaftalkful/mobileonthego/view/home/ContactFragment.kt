package com.ilaftalkful.mobileonthego.view.home
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.ContactFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.deptcontact.Datum
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.viewmodel.ContactViewModel
import java.util.*
import kotlin.collections.ArrayList

class ContactFragment : IlafBaseFragment() {
    val viewModel: ContactViewModel by viewModels()
    var contactFragmentBinding: ContactFragmentBinding? = null
    lateinit var  navController: NavController
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        contactFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false)
        contactFragmentBinding?.lifecycleOwner = this
        contactFragmentBinding?.viewModel = viewModel
        contactFragmentBinding?.viewModel = viewModel
        contactFragmentBinding?.fragment = this
        return contactFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        observedata()
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
    override fun onHomeClicked(view: View) {
        val navHostFragment: NavHostFragment = parentFragment as NavHostFragment
        val parent = navHostFragment.parentFragment as DashBoardFragment
        parent.showHome()
    }

    fun emailClicked(view: View, data: Datum) {
        sendEmail(data.emailID)
    }

    fun callClicked(view: View, data: Datum) {
        if(data.phoneNumber!=null)
        callPhone(data.phoneNumber)
        else
            Toast.makeText(requireActivity(), getString(R.string.phone_number_required), Toast.LENGTH_LONG).show()
    }
    fun locationClicked(view: View, data: Datum) {
        callLocation(data)
    }

    var listItem = ArrayList<Datum>()
    private fun observedata() {
        viewModel.getContachDetails()
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR -> {
                    var error = it.getError()
                    if(error?.getErrorCode() == 100){
                        error.errorMessage = getString(R.string.no_interbnet)
                    }
                    IlafCommonAlert(requireActivity(), error?.errorMessage
                            ?: getString(R.string.something_went_wrong), null, getString(R.string.ok),
                            null, null).show()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
            }
        })
        viewModel.contactResponce.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listItem = it
                listItem = listItem.filter { it -> it.ShownInContactUs() == 1 } as ArrayList<Datum>
            }
        })

        viewModel.depatSeletId.observe(viewLifecycleOwner, Observer {
            if (it != null && !listItem.isEmpty()) {
                contactFragmentBinding?.data = listItem[it]
            }
        })
    }


    fun sendEmail(adress: String?) {
        val TO = arrayOf(adress)
        val uri = Uri.parse(adress)
            .buildUpon()
            .build()
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        emailIntent.data = Uri.parse("mailto:$adress")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        activity?.let {
            ContextCompat.startActivity(
                    it, Intent.createChooser(emailIntent, getString(R.string.send_mail)), null
            )
        }
    }


    fun callPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
        startActivity(intent)
    }

    fun callLocation(data: Datum) {
        val markerName = "Ilaf"+data.geAddressLine1()
        val uriBegin = "geo:" + data.departmentLocationLat.toString() + "," +  data.departmentLocationLon.toString()
        val uri = java.lang.String.format(Locale.ENGLISH, uriBegin+"?q=%f,%f(%s)", data.departmentLocationLat, data.departmentLocationLon, markerName)
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity()!!.packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}