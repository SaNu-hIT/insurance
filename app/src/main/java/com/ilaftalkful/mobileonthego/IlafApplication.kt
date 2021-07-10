package com.ilaftalkful.mobileonthego

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class IlafApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        context = this
        appInstance = this;
    }
    companion object {
        lateinit var context: Context

        @JvmField
        var appInstance: IlafApplication? = null


        @JvmStatic
        fun getAppInstance(): IlafApplication {
            return appInstance as IlafApplication
        }
    }


}