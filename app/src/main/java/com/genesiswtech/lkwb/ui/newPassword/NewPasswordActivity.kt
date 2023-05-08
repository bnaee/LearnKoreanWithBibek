package com.genesiswtech.lkwb.ui.newPassword

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityNewPasswordBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.newPassword.model.NewPasswordResponse
import com.genesiswtech.lkwb.ui.newPassword.presenter.NewPasswordPresenter
import com.genesiswtech.lkwb.ui.newPassword.view.INewPasswordView
import com.genesiswtech.lkwb.ui.passwordSuccess.PasswordSuccessActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager


class NewPasswordActivity : BaseActivity<ActivityNewPasswordBinding>(), INewPasswordView {

    private lateinit var newPasswordBinding: ActivityNewPasswordBinding
    private var newPasswordPresenter: NewPasswordPresenter? = null
    private var email: String? = null
    private var token: String? = null
    private var profile: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)
        email = intent.getStringExtra(LKWBConstants.EMAIL)
        token = intent.getStringExtra(LKWBConstants.TOKEN)
        profile = intent.getBooleanExtra(LKWBConstants.PROFILE, false)
        newPasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_password)
        newPasswordBinding.newpasswordhandler = this

        initDependencies()
    }

    override fun initDependencies() {
        newPasswordPresenter = NewPasswordPresenter(this, application)
    }

    override fun onResetPasswordClick(v: View?) {
        newPasswordPresenter!!.postNewPassword(
            this,
            newPasswordBinding.etPassword.text.toString().trim(),
            newPasswordBinding.etConfirmPassword.text.toString().trim()
        )
    }

    override fun onPasswordToggleClick(v: View?) {
        if (newPasswordBinding.etPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            newPasswordBinding.etPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            newPasswordBinding.etPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        newPasswordBinding.etPassword.setSelection(newPasswordBinding.etPassword.length())
    }

    override fun onConfirmPasswordToggleClick(v: View?) {
        if (newPasswordBinding.etConfirmPassword.transformationMethod.equals(
                PasswordTransformationMethod.getInstance()
            )
        ) {
            newPasswordBinding.etConfirmPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            newPasswordBinding.etConfirmPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        newPasswordBinding.etConfirmPassword.setSelection(newPasswordBinding.etConfirmPassword.length())
    }

    override fun onConfirmPasswordError() {
        showSnackBar(getString(R.string.password_6_character))
    }

    override fun onPasswordError() {
        showSnackBar(getString(R.string.password_6_character))
    }

    override fun onPasswordMismatchError() {
        showSnackBar(getString(R.string.password_mismatch))
    }

    override fun onSuccess(newPasswordResponse: NewPasswordResponse) {
        if (profile == false)
            LKWBPreferencesManager.putString(LKWBConstants.TOKEN, "")
        val intent = Intent(this, PasswordSuccessActivity::class.java)
        intent.putExtra(LKWBConstants.PROFILE, true)
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
        newPasswordBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override val binding: ActivityNewPasswordBinding
        get() = ActivityNewPasswordBinding.inflate(layoutInflater)
}