package com.genesiswtech.lkwb.ui.passwordSuccess

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityPasswordSuccessBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.login.LoginActivity
import com.genesiswtech.lkwb.ui.passwordSuccess.view.IPasswordSuccessView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class PasswordSuccessActivity : BaseActivity<ActivityPasswordSuccessBinding>(), IPasswordSuccessView {

    private lateinit var passwordSuccessBinding: ActivityPasswordSuccessBinding
    private var profile: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)
        profile = intent.getBooleanExtra(LKWBConstants.PROFILE, false)
        passwordSuccessBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_password_success);
        passwordSuccessBinding.passwordsuccesshandler = this;
        if (profile == true)
            passwordSuccessBinding.proceedLoginBtn.text = getString(R.string.ok)
    }

    override fun onProceedLoginClick(v: View?) {
        if (profile == true) {
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override val binding: ActivityPasswordSuccessBinding
        get() = ActivityPasswordSuccessBinding.inflate(layoutInflater)


}