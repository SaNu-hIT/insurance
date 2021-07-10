package com.ilaftalkful.mobileonthego.view.payment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.PaymentWebViewFragmentBinding
import com.ilaftalkful.mobileonthego.utilities.Constants


class
PaymentWebViewFragment : IlafBaseFragment() {
    
    val viewModel: PaymentWebViewViewModel by viewModels()
    var paymentUrl:String?=null
    lateinit var binding:PaymentWebViewFragmentBinding
    var navController:NavController?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            paymentUrl = arguments?.getString(Constants.PAYMENT_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<PaymentWebViewFragmentBinding>(
            inflater,
            R.layout.payment_web_view_fragment, container, false
        )
        binding.lifecycleOwner =this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)

         binding.webView.setWebViewClient(object : WebViewClient() {
             override fun shouldOverrideUrlLoading(view: WebView, urlConection: String): Boolean {
                 if (urlConection.contains("clipconvertor")) {
                     var bundle:Bundle?=null
                     bundle = bundleOf("isSuccess" to 1)
                     navController?.navigate(R.id.action_show_after_payment_screen,bundle)

                 }
                return true;
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }
        })
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(yourWebClient);
        binding.webView.loadUrl(paymentUrl);
        binding.webView.addJavascriptInterface(MyJavaScriptInterface(requireActivity(),navController!!), "HtmlViewer");
    }


    var yourWebClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            binding.webView.loadUrl(
                "javascript:HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('IsSuccess')[0].innerHTML+'</html>');"
            )
        }
    }


    internal class MyJavaScriptInterface(ctx: Activity, navController: NavController) {
        private val ctx: Context
        var navController:NavController
        @JavascriptInterface
        fun showHTML(html: String?) {
            var bundle:Bundle?=null
            if(html?.toLowerCase()?.contains("true")?:false){
                bundle = bundleOf("isSuccess" to 1)
            }else {
                bundle = bundleOf("isSuccess" to 2)
            }
            navController?.navigate(R.id.action_show_after_payment_screen,bundle)

        }

        init {
            this.ctx = ctx
            this.navController=navController
        }
    }

}
