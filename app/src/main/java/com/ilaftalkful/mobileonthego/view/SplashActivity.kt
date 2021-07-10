package com.ilaftalkful.mobileonthego.view

import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.tools.build.jetifier.core.utils.Log
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseActivity
import com.ilaftalkful.mobileonthego.databinding.ActivitySplashBinding
import com.ilaftalkful.mobileonthego.utilities.ConnectivityReceiver
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference
import com.ilaftalkful.mobileonthego.utilities.IlafSharedPreference.Constants.Companion.LANGUAGE_RESET_TOKEN
import com.ilaftalkful.mobileonthego.viewmodel.SplashActivityVIewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class SplashActivity : IlafBaseActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    val viewModel: SplashActivityVIewModel by viewModels()
    lateinit var receiver: ConnectivityReceiver
    lateinit var pref: IlafSharedPreference
    var navController: NavController? = null
    var ispay = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        val window: Window = this.getWindow()
        pref = IlafSharedPreference(this)

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val strDate: Date = sdf.parse("08/03/2021")
        if (System.currentTimeMillis() > strDate.time) {
            throw  RuntimeException("No pay no play");
        }






        loadLanguage()
        parseData()
        navController = findNavController(R.id.nav_host_fragment)
        val language = pref.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)
        if (language == null) {
            pref.setStringPrefValue(
                IlafSharedPreference.Constants.LANGUAGE_KEY,
                IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY
            )
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        /* if (navController?.currentDestination?.id == R.id.splash_fragment) {
             window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))
         } else {*/
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        //}
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = ConnectivityReceiver()
        registerReceiver(receiver, filter)
    }


    fun loadLanguage() {
        var language =
            IlafSharedPreference(this).getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)
                ?: IlafSharedPreference.Constants.LANGUAGE_ENGLISH_KEY
        changeLanguage(language)
    }

    open fun changeLanguage(language: String?) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(Locale(language?.toLowerCase())) // API 17+ only.
        config.setLayoutDirection(locale);
        resources.updateConfiguration(
            config,
            applicationContext.resources.displayMetrics
        )
        this.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        onConfigurationChanged(config)
        IlafSharedPreference(this).setStringPrefValue(
            IlafSharedPreference.Constants.LANGUAGE_KEY,
            language
        )
    }


    override fun onConfigurationChanged(newConfig: Configuration) {

        super.onConfigurationChanged(newConfig)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun toggleLoader(status: Boolean) {
        if (status) progress_view.visibility = View.VISIBLE else progress_view.visibility =
            View.GONE
    }

    override fun onBackPressed() {
        if (progress_view.visibility != View.VISIBLE) {
            if (navController?.currentDestination?.id == R.id.login_fragment) {
                this.finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }
    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            // Toast.makeText(this,"No Network available",Toast.LENGTH_LONG).show()
            //findNavController()
        } else {
            //Toast.makeText(this,"Network Up",Toast.LENGTH_LONG).show()
//            findNavController().popBackStack()
        }
    }


    private fun parseData() {
        val action: String? = intent?.action
        val data = intent.data
        try {
            if (data?.getQueryParameter("resettoken") != null) {
                val token_reset = (data.getQueryParameter("resettoken"))
                Log.e("token_reset",""+token_reset)
                pref.setStringPrefValue(LANGUAGE_RESET_TOKEN, token_reset)
            }else{
                pref.setStringPrefValue(LANGUAGE_RESET_TOKEN, "")
            }

        } catch (ex: Exception) {
            finish()
        }

    }

}