package com.genesiswtech.lkwb.ui.splash

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.genesiswtech.lkwb.BuildConfig
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.ui.login.LoginActivity
import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


class SplashActivity : AppCompatActivity() {


    lateinit var applicationComponent: Application
    private var checkForUpdate: Boolean = true
    private var isForceUpdate: Boolean = false
    private var versionCode: Int = 2
    private var message: String = ""
    private lateinit var remoteConfig: FirebaseRemoteConfig


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setRemoteConfig()
        pushToken()

        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                remoteConfig.all
                Log.d("TAG", "Config params updated: $updated")
                checkForUpdate = remoteConfig.getString("checkForUpdate").toBoolean()
                isForceUpdate = remoteConfig.getString("isForceUpdate").toBoolean()
                versionCode = remoteConfig.getString("versionCode").toInt()
                message = remoteConfig.getString("appUpdateMessage").toString()
                Log.d("TAG", "Config update check: $checkForUpdate")
                Log.d("TAG", "Config force update: $isForceUpdate")
                Log.d("TAG", "Config version code: $versionCode")
                Log.d("TAG", "Config message: $message")
                Log.d("TAG", "Config package: $://details?id=${this.packageName}")
                val manager = this.packageManager
                val info =
                    manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
                if (!isForceUpdate) {
                    if (checkForUpdate) {
                        if (info.versionCode < versionCode) {
                            showUpdateDialog(message)
                        } else {
                            splashCall(3000)
                        }
                    } else {
                        splashCall(3000)
                    }
                } else {
                    if (info.versionCode < versionCode) {
                        showForceUpdateDialog(message)
                    } else {
                        splashCall(3000)
                    }

                }
            } else {
                Log.d("TAG", "Config params updated: ${task.exception}")
            }
        }


    }

    private fun showUpdateDialog(message: String) {
        AppUtils.showDialog(this,
            title = "",
            message,
            titleOfPositiveButton = getString(R.string.Update),
            titleOfNegativeButton = getString(R.string.cancel),
            positiveButtonFunction = {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("market://details?id=${this.packageName}")
                    )
                )
            },
            negativeButtonFunction = { splashCall(100) })
    }

    private fun showForceUpdateDialog(message: String) {
        AppUtils.showSingleDialog(
            this,
            title = message,
            titleOfPositiveButton = getString(R.string.Update),
            positiveButtonFunction = {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("market://details?id=${this.packageName}")
                    )
                )
            },

            )
    }

    private fun setRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            if (BuildConfig.DEBUG) {
                minimumFetchIntervalInSeconds = 0 // Kept 0 for quick debug
            } else {
                minimumFetchIntervalInSeconds = 60 * 60 // Change this based on your requirement
            }
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.lkwb_remote_config)
    }


    private fun pushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            if (task.result != null) {
                val token: String = task.result
                Log.d("TAG", "Push Token: " + token)
            }
        }
    }

    private fun splashCall(delay: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            val userData = LKWBPreferencesManager.get<LoginDataResponse>(LKWBConstants.KEY_USER)
            if (userData != null) {
                if (LKWBPreferencesManager.getString(LKWBConstants.VERIFIED) == getString(R.string.verified)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 100)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, delay)


    }


}