package com.genesiswtech.lkwb.ui.otpVerification

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityOtpVerificationBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import com.genesiswtech.lkwb.ui.loginReward.LoginRewardActivity
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.ui.newPassword.NewPasswordActivity
import com.genesiswtech.lkwb.ui.otpVerification.model.AccountDeleteResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.OTPVerificationResponse
import com.genesiswtech.lkwb.ui.otpVerification.presenter.OTPVerificationPresenter
import com.genesiswtech.lkwb.ui.otpVerification.view.IOTPVerificationView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import retrofit2.Response

class OTPVerificationActivity : BaseActivity<ActivityOtpVerificationBinding>(), IOTPVerificationView {

    private lateinit var otpVerificationBinding: ActivityOtpVerificationBinding
    private var otpVerificationPresenter: OTPVerificationPresenter? = null
    private var email: String? = null
    private var profile: Boolean? = false
    private var verified: Boolean? = false
    private var delete: Boolean? = false
    private var register: Boolean? = false
    private var forgot: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)
        email = intent.getStringExtra(LKWBConstants.EMAIL)
        profile = intent.getBooleanExtra(LKWBConstants.PROFILE, false)
        verified = intent.getBooleanExtra(LKWBConstants.VERIFIED, false)
        delete = intent.getBooleanExtra(LKWBConstants.DELETE, false)
        register = intent.getBooleanExtra(LKWBConstants.REGISTER, false)
        forgot = intent.getBooleanExtra(LKWBConstants.FORGOT, false)
        otpVerificationBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_otp_verification)
        otpVerificationBinding.otphandler = this
        initDependencies()
        if (verified == true) {
            otpVerificationBinding.headerTV.text = getString(R.string.enter_otp_to_verify)
            otpVerificationBinding.subTV.text = getString(R.string.otp_sent_mail)
        }
        if (profile == true || verified == true || delete == true) otpVerificationPresenter!!.postResendOTP(
            this, email!!
        )

    }

    override fun initDependencies() {
        otpVerificationPresenter = OTPVerificationPresenter(this, application)
    }

    override fun onOTPVerifyClick(v: View?) {
        val otpCode: String =
            otpVerificationBinding.otp1Edt.text.toString() + otpVerificationBinding.otp2Edt.text.toString() + otpVerificationBinding.otp3Edt.text.toString() + otpVerificationBinding.otp4Edt.text.toString()
        if (profile == true || forgot == true) {
            otpVerificationPresenter!!.postOTPVerification(
                this, email!!, otpCode
            )
        }

        if (verified == true || register == true) {
            otpVerificationPresenter!!.postNewUserOTPVerification(this, otpCode)
        }

        if (delete == true) {
            AppUtils.showDialog(this,
                getString(R.string.delete_account),
                getString(R.string.sure_to_delete),
                getString(R.string.yes),
                getString(R.string.cancel),
                positiveButtonFunction = {
                    otpVerificationPresenter!!.postDeleteAccount(
                        this, email!!, otpCode
                    )
                },
                negativeButtonFunction = {})
        }


    }

    override fun onOTPResendClick(v: View?) {
        otpVerificationPresenter!!.postResendEmail(
            this, email!!
        )
    }

    override fun onFirstOTPTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) otpVerificationBinding.otp2Edt.requestFocus()
            }
        }
    }

    override fun onSecondOTPTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) otpVerificationBinding.otp3Edt.requestFocus()
            }
        }
    }

    override fun onThirdOTPTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) otpVerificationBinding.otp4Edt.requestFocus()
            }
        }
    }

    override fun onFourthOTPTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) otpVerificationBinding.otp4Edt.requestFocus()
            }
        }
    }

    override fun onSuccess(otpVerificationResponse: OTPVerificationResponse) {

        otpVerificationResponse.data!!.token?.let {
            LKWBPreferencesManager.putString(
                LKWBConstants.TOKEN, it
            )
        }
        val intent = Intent(this, NewPasswordActivity::class.java)
        intent.putExtra(LKWBConstants.TOKEN, otpVerificationResponse.data!!.token)
        intent.putExtra(LKWBConstants.EMAIL, otpVerificationResponse.data!!.email)
        intent.putExtra(LKWBConstants.PROFILE, true)
        startActivity(intent)
        finish()


    }

    override fun onUserVerificationSuccess(otpVerificationResponse: OTPVerificationResponse) {
        LKWBPreferencesManager.putString(
            LKWBConstants.VERIFIED, getString(R.string.verified)
        )
        if (LKWBPreferencesManager.getString(
                LKWBConstants.IS_NEW_USER
            ) == "true"
        ) {
            LKWBPreferencesManager.putString(
                LKWBConstants.IS_NEW_USER, "false"
            )
            val intent = Intent(this, LoginRewardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onAccountDeleteSuccess(accountDeleteResponse: AccountDeleteResponse) {
        if (accountDeleteResponse.code == 200) {
            showSnackBar(accountDeleteResponse.message)
            AppUtils.logOutCall(this)
        }

    }

    override fun onResendEmailSuccess(forgotPasswordResponse: Response<ForgotPasswordResponse>?) {
        showSnackBar(getString(R.string.email_sent))
    }

    override fun onResendOTPSuccess(forgotPasswordResponse: Response<ForgotPasswordResponse>?) {
        showSnackBar(getString(R.string.email_sent))
    }

    override fun onOTPError() {
        showSnackBar(getString(R.string.opt_error))
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
        otpVerificationBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override val binding: ActivityOtpVerificationBinding
        get() = ActivityOtpVerificationBinding.inflate(layoutInflater)
}