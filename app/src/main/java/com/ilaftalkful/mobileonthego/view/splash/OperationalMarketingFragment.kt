package com.ilaftalkful.mobileonthego.view.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.OperationalMarketingFragmentBinding
import com.ilaftalkful.mobileonthego.model.OptionMarketingModel
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference


class OperationalMarketingFragment : IlafBaseFragment() {

    val viewModel: OperationalMarketingViewModel by viewModels()
    var data:ArrayList<OptionMarketingModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null) {
            data = arguments?.getSerializable("splashMessage") as ArrayList<OptionMarketingModel>
            viewModel.data.postValue(data)
            viewModel.setMessage(data?.get(0)?.splashMessage.toString())
            if(!data?.get(0)?.splashURL.isNullOrEmpty()){
                viewModel.isMoreDetailVisible.postValue(true)
            }else{
                viewModel.isMoreDetailVisible.postValue(false)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<OperationalMarketingFragmentBinding>(inflater,
                R.layout.operational_marketing_fragment, container, false)
        binding.lifecycleOwner =this
        binding.fragment=this
        binding.viewModel=viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
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
    fun onSkipClicked(){
        if (IlafSharedPreference(requireContext()).getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER) == null) {
            findNavController().navigate(R.id.action_show_login_om)
        } else if (IlafSharedPreference(requireContext()).getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER)) {
            findNavController().navigate(R.id.action_show_home_from_om)
        } else {
            findNavController().navigate(R.id.action_show_login_om)
        }
    }

    fun    onMoreDetailsClicked(){
       /* val bundle = bundleOf(Constants.PAYMENT_URL to viewModel.data.value?.get(0)?.splashURL)
        findNavController().navigate(R.id.action_show_web_view, bundle)*/
        var url = viewModel.data.value?.get(0)?.splashURL
        if(!viewModel.data.value?.get(0)?.splashURL.isNullOrEmpty() && (!viewModel.data.value?.get(0)?.splashURL?.startsWith("http://")!! || !viewModel.data.value?.get(0)?.splashURL?.startsWith("https://")!! )){
            url = "http://"+url?.trim()
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse( url))
        startActivity(browserIntent)
    }

}