package com.genesiswtech.lkwb.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.layoutDirection
import androidx.viewbinding.ViewBinding
import com.genesiswtech.lkwb.utils.RuntimeLocaleChanger
import java.util.*

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected abstract val binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        RuntimeLocaleChanger.overrideLocale(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Will give the direction of the layout depending of the Locale you've just set
        window.decorView.layoutDirection = Locale.getDefault().layoutDirection
    }
}
