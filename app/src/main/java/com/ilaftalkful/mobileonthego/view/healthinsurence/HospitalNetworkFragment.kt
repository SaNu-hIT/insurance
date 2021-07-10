package com.ilaftalkful.mobileonthego.view.healthinsurence

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
import com.ilaftalkful.mobileonthego.databinding.HospitalNetworkFragmentBinding
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.health.hospital.HospitalNetwork
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.SplashActivity
import java.util.*

class HospitalNetworkFragment : IlafBaseFragment() {
    val viewModel: HospitalNetworkViewModel by viewModels()
    var hospitalNetworkFragmentBinding: HospitalNetworkFragmentBinding? = null

    var travelPlansAdapter: HospitalNetworkAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hospitalNetworkFragmentBinding = DataBindingUtil.inflate<HospitalNetworkFragmentBinding>(
            inflater,
            R.layout.hospital_network_fragment,
            container,
            false
        )
        hospitalNetworkFragmentBinding?.lifecycleOwner = this
        hospitalNetworkFragmentBinding?.viewModel = viewModel
        hospitalNetworkFragmentBinding?.fragment = this
        hospitalNetworkFragmentBinding?.executePendingBindings()
        hospitalNetworkFragmentBinding?.label =
            getString(R.string.hospital_network)
        return hospitalNetworkFragmentBinding?.root
    }

    override fun moveToLogin() {
        val bundle = bundleOf(Constants.IS_FROM_SCREENS to true)
        findNavController().navigate(R.id.login_fragment, bundle)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.SESSION_EXPIRED -> {
                    sessionExpiryHandle()
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
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
                    IlafCommonAlert(requireActivity(), error?.errorMessage?:getString(R.string.something_went_wrong), getString(R.string.ok), null, null).show()
                }
            }
        })
        observedata()
        setAdapter()
    }

    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    private fun observedata() {
        viewModel.getHospitalNetworks()
        viewModel.hospitalResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                travelPlansAdapter?.setPlansData(it.hospitalNetworks as ArrayList<HospitalNetwork>)
            }
        })
        viewModel.searchKey.observe(viewLifecycleOwner, Observer {
            var data: List<HospitalNetwork>? = null
            var networks: ArrayList<HospitalNetwork> =
                    viewModel.hospitalResponse.value?.hospitalNetworks as ArrayList<HospitalNetwork>
            if (viewModel.hospitalResponse.value?.hospitalNetworks != null) {
                if (viewModel.regionSeletId.value != 0) {
                    if (it.isNullOrEmpty()) {
                        data =
                                networks?.filter { it ->
                                    it.hospitalRegion?.toLowerCase(Locale.getDefault()).toString()
                                            .contains(
                                                    viewModel.regionName[viewModel.regionSeletId.value!!].toLowerCase(
                                                            Locale.getDefault()
                                                    ).toString()
                                            )
                                }
                    } else {
                        data =
                                networks?.filter { it ->
                                    it.hospitalRegion?.toLowerCase(Locale.getDefault()).toString()
                                            .contains(
                                                    viewModel.regionName[viewModel.regionSeletId.value!!].toLowerCase(
                                                            Locale.getDefault()
                                                    ).toString()
                                            )
                                }
                        data =
                                data.filter { it ->

                                    it?.hospitalName?.toLowerCase(Locale.getDefault()).toString().contains(
                                            viewModel.searchKey.value?.toLowerCase(
                                                    Locale.getDefault()
                                            ).toString()
                                    )
                                }
                    }
                } else {
                    data = networks
                    if (!it.isNullOrEmpty()) {

                        data =
                                data.filter { it ->

                                    it?.hospitalName?.toLowerCase(Locale.getDefault()).toString().contains(
                                            viewModel.searchKey.value?.toLowerCase(
                                                    Locale.getDefault()
                                            ).toString()
                                    )
                                }
                    }
                }
                travelPlansAdapter?.setPlansData(
                        data as ArrayList<HospitalNetwork>)
            }
        })


        viewModel.regionSeletId.observe(viewLifecycleOwner, Observer {
            if (it != null) {


                if (viewModel.hospitalResponse.value?.hospitalNetworks != null) {
                    var networks: ArrayList<HospitalNetwork> =
                        viewModel.hospitalResponse.value?.hospitalNetworks as ArrayList<HospitalNetwork>
                    var data: List<HospitalNetwork>? = null

                    if(viewModel.regionSeletId.value != 0) {


                        data =
                            networks.filter { it ->
                                it.hospitalRegion?.toLowerCase(Locale.getDefault()).toString()
                                    .contains(
                                        viewModel.regionName[viewModel.regionSeletId.value!!].toLowerCase(
                                            Locale.getDefault()
                                        ).toString()
                                    )
                            }
                    }else{
                        data = networks
                    }
                    travelPlansAdapter?.setPlansData(
                        data as ArrayList<HospitalNetwork>
                    )
                }


            }
        })

    }

    private fun setAdapter() {
        if (travelPlansAdapter == null) {
            travelPlansAdapter = HospitalNetworkAdapter()
            hospitalNetworkFragmentBinding?.recyclerView?.adapter = travelPlansAdapter
        } else {
            travelPlansAdapter?.notifyDataSetChanged()
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