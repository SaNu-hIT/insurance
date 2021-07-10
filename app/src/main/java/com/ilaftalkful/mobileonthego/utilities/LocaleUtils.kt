package com.ilaftalkful.mobileonthego.utilities

import android.content.Context

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build

import androidx.annotation.StringDef
import com.ilaftalkful.mobileonthego.IlafApplication
import java.util.*


internal object LocaleUtils {
    const val ENGLISH = "en"
    const val ARABIC = "ar"
    fun initialize(context: Context, @LocaleDef defaultLanguage: String?) {
        setLocale(context, defaultLanguage)
    }

    fun setLocale(context: Context, @LocaleDef language: String?): Boolean {
        return updateResources( language)
    }

    private fun updateResources(/*context: Context,*/ language: String?): Boolean {
        var context = IlafApplication.context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()
        context.createConfigurationContext(configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale);

        // updateConfiguration(...) is deprecated in N
        if (Build.VERSION.SDK_INT >= 25) {
            context = context.getApplicationContext().createConfigurationContext(configuration);
            context = context.createConfigurationContext(configuration);
        }

        context.getResources().updateConfiguration(configuration,
                resources.getDisplayMetrics());

        return true
    }

    private fun getDefaultSharedPreference(context: Context): IlafSharedPreference? {
        return if (IlafSharedPreference(IlafApplication.getAppInstance()) != null) IlafSharedPreference(IlafApplication.getAppInstance()) else null
    }

    var selectedLanguageId: String?
        get() = getDefaultSharedPreference(IlafApplication.getAppInstance())?.getStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY)
        set(id) {
            val prefs = getDefaultSharedPreference(IlafApplication.getAppInstance())
           prefs?.setStringPrefValue(IlafSharedPreference.Constants.LANGUAGE_KEY,id)
        }

   @StringDef(ENGLISH, ARABIC)
    annotation class LocaleDef {
        companion object {
            var SUPPORTED_LOCALES = arrayOf(ENGLISH, ARABIC)
        }
    }
}