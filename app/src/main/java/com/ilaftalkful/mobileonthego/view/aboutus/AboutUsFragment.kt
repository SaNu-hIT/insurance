package com.ilaftalkful.mobileonthego.view.aboutus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.AboutUsFragmentBinding
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference

class AboutUsFragment : IlafBaseFragment() {

    val viewModel: AboutUsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AboutUsFragmentBinding>(inflater,
                R.layout.about_us_fragment, container, false)
        binding.fragment=this
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        binding.label = getString(R.string.about_us)
        return binding.root
    }


    override fun onEnglishClicked(view: View) {
        viewModel.pref.setStringPrefValue(
                IlafSharedPreference.Constants.LANGUAGE_KEY,
                IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY
        )
        viewModel.isENS.postValue(true)
        refreshConfigChange()
    }

    override fun onArabicClicked(view: View) {
        viewModel.pref.setStringPrefValue(
                IlafSharedPreference.Constants.LANGUAGE_KEY,
                IlafSharedPreference.Constants.LANGUAGE_ARABIC_KEY
        )
        viewModel.isENS.postValue(false)
        refreshConfigChange()

    }

    override fun onResume() {
        super.onResume()
        if(viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)==null) {
            viewModel.pref.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY,IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY)
        }
        if(viewModel.pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY).equals(IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY))

            viewModel.isENS.postValue(true)
        else
            viewModel.isENS.postValue(false)
    }


    override fun onHomeClicked(view: View) {
        findNavController().navigate(R.id.action_show_home)
    }

    fun onWebClicked(view :View){
        var v = view as TextView
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.powered_by_alghanim_technologies_link)))
        startActivity(browserIntent)
    }
}