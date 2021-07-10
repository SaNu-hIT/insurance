package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.content.Intent
import android.net.Uri
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
import com.ilaftalkful.mobileonthego.databinding.AssistanceForClameFragmentBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.health.claimassistance.Datum
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.SplashActivity

class AssistanceForClameFragment : IlafBaseFragment(), OnItemClickListener {

    val viewModel: AssistanceForClameViewModel by viewModels()
    var assistanceForClameFragmentBinding: AssistanceForClameFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         assistanceForClameFragmentBinding =
            DataBindingUtil.inflate<AssistanceForClameFragmentBinding>(
                inflater,
                R.layout.assistance_for_clame_fragment,
                container,
                false
            )
        assistanceForClameFragmentBinding?.lifecycleOwner = this
        assistanceForClameFragmentBinding?.viewModel = viewModel
        assistanceForClameFragmentBinding?.fragment = this
        assistanceForClameFragmentBinding?.executePendingBindings()
        assistanceForClameFragmentBinding?.label =
            getString(R.string.assistance_for_claim)
        return assistanceForClameFragmentBinding?.root
    }
    override fun moveToLogin() {
        val bundle = bundleOf( Constants.IS_FROM_SCREENS to true)
        findNavController().navigate(R.id.login_fragment,bundle)

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
        viewModel.getAssistanceForClime()
        observedata()
        setAdapter()

    }


    override fun onBackClicked(view: View) {
        super.onBackClicked(view)
    }

    private fun observedata() {

        viewModel.assisitanceDetails.observe(viewLifecycleOwner, Observer {
            travelPlansAdapter?.setPlansData(it)
        })
    }

    var travelPlansAdapter: AssistanceClaimAdapter? = null


    private fun setAdapter() {
        if (travelPlansAdapter == null) {
            travelPlansAdapter = AssistanceClaimAdapter()
            travelPlansAdapter?.onClick =this
            assistanceForClameFragmentBinding?.recyclerView?.adapter = travelPlansAdapter
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

    override fun onItemClick(v: View, obj: Any) {
        var data = obj as Datum
        if(data.phoneNumber!=null) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.phoneNumber))
            startActivity(intent)
        }
    }
}