package com.genesiswtech.lkwb

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.genesiswtech.lkwb.di.module.NetModule
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import com.genesiswtech.lkwb.utils.RuntimeLocaleChanger
import com.genesiswtech.lkwb.utils.appModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class LKWBApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LKWBApplication)
            modules(appModule)
        }
        FirebaseApp.initializeApp(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        LKWBPreferencesManager.with(this)
        applicationComponent = DaggerApplicationComponent.builder()
            .netModule(NetModule())
            .build()

        applicationComponent.inject(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }
}