package com.ilaftalkful.mobileonthego.view.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.PaymentStatusFragmentBinding
import com.ilaftalkful.mobileonthego.view.SplashActivity

class PaymentStatusFragment : IlafBaseFragment() {

    val viewModel:PaymentStatusViewModel by viewModels()
     var isSuccess:Int?=null
    var navController: NavController?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        (activity as SplashActivity).toggleLoader(false)
        if(arguments!=null){
            isSuccess = arguments?.getInt("isSuccess")
        }
    }
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController?.navigate(R.id.action_show_home)
        }
    }

     fun onBackHome(view: View) {
         navController?.navigate(R.id.action_show_home)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<PaymentStatusFragmentBinding>(inflater,R.layout.payment_status_fragment, container, false)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel
        binding.fragment =this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isSuccess!=null) {
            viewModel.isSuccess.postValue(isSuccess)
        if(isSuccess ==1){
            viewModel.msg.postValue(getString(R.string.payment_success))
        }else if(isSuccess==2){
            viewModel.msg.postValue(getString(R.string.payment_failure))
        }else if(isSuccess==3){
            viewModel.msg.postValue(getString(R.string.claim_intimated))
        }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        callback.remove()
        super.onStop()
    }
}