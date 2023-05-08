package com.genesiswtech.lkwb.ui.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityForgotPasswordBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import com.genesiswtech.lkwb.ui.forgotPassword.presenter.ForgotPasswordPresenter
import com.genesiswtech.lkwb.ui.forgotPassword.view.IForgotPasswordView
import com.genesiswtech.lkwb.ui.otpVerification.OTPVerificationActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import retrofit2.Response

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(), IForgotPasswordView {

    private lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    private var forgotPasswordPresenter: ForgotPasswordPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)

        forgotPasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        forgotPasswordBinding.forgothandler = this

        initDependencies()
    }

    override fun initDependencies() {
        forgotPasswordPresenter = ForgotPasswordPresenter(this, application)
    }

    override fun onSendCodeClick(v: View?) {
        forgotPasswordPresenter!!.postForgotPassword(
            this,
            forgotPasswordBinding.emailEdt.text.toString().trim()
        )
    }

    override fun onEmailError() {
        showSnackBar(getString(R.string.enter_email_address))
    }

    override fun onInvalidEmailError() {
        showSnackBar(getString(R.string.enter_valid_email_address))
    }

    override fun onSuccess(forgotPasswordResponse: Response<ForgotPasswordResponse>?) {
        val intent = Intent(this, OTPVerificationActivity::class.java)
        intent.putExtra(LKWBConstants.EMAIL, forgotPasswordBinding.emailEdt.text.toString().trim())
        intent.putExtra(LKWBConstants.FORGOT, true)
        startActivity(intent)
        finish()
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        forgotPasswordBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override val binding: ActivityForgotPasswordBinding
        get() = ActivityForgotPasswordBinding.inflate(layoutInflater)
}