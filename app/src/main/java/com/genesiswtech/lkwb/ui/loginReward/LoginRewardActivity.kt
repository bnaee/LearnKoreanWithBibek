package com.genesiswtech.lkwb.ui.loginReward

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityLoginRewardBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.loginReward.view.ILoginRewardView
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.utils.AppUtils

class LoginRewardActivity : BaseActivity<ActivityLoginRewardBinding>(), ILoginRewardView {

    private lateinit var loginRewardBinding: ActivityLoginRewardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)

        loginRewardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login_reward)
        loginRewardBinding.loginRewardhandler = this

    }

    private fun intentCall() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onContinueClick(v: View?) {
        intentCall()
    }

    override fun onBackPressed() {
        intentCall()
       // super.onBackPressed()
    }

    override val binding: ActivityLoginRewardBinding
        get() = ActivityLoginRewardBinding.inflate(layoutInflater)
}