package com.genesiswtech.lkwb.ui.referCode

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivityReferCodeBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.referCode.view.IReferCodeView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class ReferCodeActivity : BaseActivity<ActivityReferCodeBinding>(), IReferCodeView {

    private lateinit var referCodeBinding: ActivityReferCodeBinding
    private val REFER_CODE: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)

        referCodeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_refer_code)
        referCodeBinding.referCodehandler = this

    }

    private fun intentCall() {
        val intent = Intent()
        intent.putExtra(LKWBConstants.REF_CODE, referCodeBinding.referCodeEdt.text.toString())
        setResult(REFER_CODE, intent)
        finish()
    }

    override fun onContinueClick(v: View?) {
        intentCall()
    }

    override fun onReferCodeToggleClick(v: View?) {
        referCodeDialog()
    }

    override fun onBackPressed() {
        intentCall()
        super.onBackPressed()
    }

    private fun referCodeDialog() {
        AppUtils.showPurchaseDialog(this,
            getString(R.string.refer_code),
            getString(R.string.refer_code_popup),
            titleOfPositiveButton = getString(R.string.ok),
            positiveButtonFunction = {})
    }

    override val binding: ActivityReferCodeBinding
        get() = ActivityReferCodeBinding.inflate(layoutInflater)
}