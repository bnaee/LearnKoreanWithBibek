package com.genesiswtech.lkwb.ui.web

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ActivityLkwbWebBinding
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants.DATA_LINK
import com.genesiswtech.lkwb.utils.LKWBConstants.DATA_TITLE


class LKWBWebActivity : AppCompatActivity() {

    private lateinit var lkwbWebActivity: ActivityLkwbWebBinding

    val webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        AppUtils.actionBarWithTitle(this, intent.getStringExtra(DATA_TITLE)!!)
        lkwbWebActivity = DataBindingUtil.setContentView(this, R.layout.activity_lkwb_web)
        lkwbWebActivity.webHandler = this

        lkwbWebActivity.llProgressBar.linear.visibility = View.VISIBLE
        val webView = lkwbWebActivity.webView
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl(intent.getStringExtra(DATA_LINK)!!)

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    lkwbWebActivity.llProgressBar.linear.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}