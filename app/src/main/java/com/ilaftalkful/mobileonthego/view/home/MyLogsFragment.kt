package com.ilaftalkful.mobileonthego.view.home

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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.customcomponents.IlafCommonAlert
import com.ilaftalkful.mobileonthego.databinding.MyLogsFragmentBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.UserData
import com.ilaftalkful.mobileonthego.model.log.ModuleDetail
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.view.SplashActivity
import com.ilaftalkful.mobileonthego.view.mylog.MyLogAdapter
import com.ilaftalkful.mobileonthego.viewmodel.MyLogsViewModel


class MyLogsFragment : IlafBaseFragment(),OnItemClickListener {
    val viewModel: MyLogsViewModel by viewModels()
    lateinit var navController: NavController
    var myLogsFragmentBinding: MyLogsFragmentBinding? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        myLogsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.my_logs_fragment, container, false)
        myLogsFragmentBinding?.lifecycleOwner = this
        myLogsFragmentBinding?.viewModel = viewModel
        myLogsFragmentBinding?.fragment = this
        return myLogsFragmentBinding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onRegisterOrLoginClicked(view: View) {
        super.onRegisterOrLoginClicked(view)
        navController.navigate(R.id.action_show_login_from_home)
    }

    override fun onNotificationClicked(view: View) {

    }

    override fun onResume() {
        super.onResume()
        setAdapter()
        observedata()
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

    var travelPlansAdapter: MyLogAdapter? = null
    private fun setAdapter() {
        if (travelPlansAdapter == null) {
            travelPlansAdapter = MyLogAdapter()
            travelPlansAdapter?.onClick =this
            travelPlansAdapter?.setPlansData(emptyList())
            myLogsFragmentBinding?.recyclerView?.adapter = travelPlansAdapter
        } else {
            travelPlansAdapter?.notifyDataSetChanged()
        }
    }

    private fun observedata() {
        if(IlafSharedPreference(requireActivity()).getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER)) {
            viewModel.getLogList()
        }
        viewModel.logRespone.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                myLogsFragmentBinding?.recyclerView?.adapter = travelPlansAdapter
                travelPlansAdapter?.setPlansData(it)

            }
        })
        viewModel.userLiveData?.observe(viewLifecycleOwner, Observer {
            when (it.getStatus()) {
                UserData.UserStatus.RESPOSNSE_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                }
                UserData.UserStatus.PDF_DOWNLOAD_SUCCESS -> {
                    (activity as SplashActivity).toggleLoader(false)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.pdfUrl.value))
                    startActivity(browserIntent)
                    viewModel.userLiveData?.userExistFailed()
                }
                UserData.UserStatus.PDF_DOWNLOAD_FAILED -> {
                    (activity as SplashActivity).toggleLoader(false)
                    IlafCommonAlert(requireActivity(),it.getError()?.getErrorMessage(),getString(R.string.ok),null,null).show()
                }
                UserData.UserStatus.OPERATION_STARTED -> {
                    (activity as SplashActivity).toggleLoader(true)
                }
                UserData.UserStatus.ERROR -> {
                    var error = it.getError()
                    if (error?.getErrorCode() == 100) {
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
        viewModel.isMyLogEmpty.observe(viewLifecycleOwner, Observer {
            if (it) {
                myLogsFragmentBinding?.recyclerView?.visibility = View.GONE
            } else {
                myLogsFragmentBinding?.recyclerView?.visibility = View.VISIBLE
            }
        })
    }

    override fun onItemClick(v: View, obj: Any) {
        val data = obj as ModuleDetail
        when (v.id){
            R.id.txt_viewgarage -> {
                val bundle = bundleOf(Constants.ISFROM_MOTOR_CLAIM to false)
                navController?.navigate(R.id.action_show_garage, bundle)
            }
            R.id.txt_download -> {
                viewModel.getPdfToDownload(data.policyID.trim().toString())
            }
        }

    }

}