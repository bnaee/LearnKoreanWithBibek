package com.genesiswtech.lkwb.ui.beginTest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityBeginTestBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestDataResponse
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestResponse
import com.genesiswtech.lkwb.ui.beginTest.presenter.BeginTestPresenter
import com.genesiswtech.lkwb.ui.beginTest.view.IBeginTestView
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.UBTQuestionActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class BeginTestActivity : BaseActivity<ActivityBeginTestBinding>(), IBeginTestView {
    private lateinit var beginTestBinding: ActivityBeginTestBinding
    private var ubtTestData: UBTTestDataResponse? = null
    private var beginTestPresenter: BeginTestPresenter? = null
    private var beginTestDataResponse: BeginTestDataResponse? = null

    private var purchase: Boolean = false
    private var packageId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageId = intent.getStringExtra(LKWBConstants.BLOG_PACKAGE)
        ubtTestData = intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? UBTTestDataResponse
        AppUtils.actionBarWithTitle(this, ubtTestData!!.title!!)
        purchase = intent.getBooleanExtra(LKWBConstants.DATA_PURCHASE, false)
        beginTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_begin_test)
        beginTestBinding.beginTestHandler = this

        initDependencies()
        beginTestPresenter!!.getUBTSet(this, ubtTestData!!.id!!)

    }

    override fun initDependencies() {
        beginTestPresenter = BeginTestPresenter(this, application)
    }

    override fun onBeginButtonClick(v: View?) {
        val intent = Intent(this, UBTQuestionActivity::class.java)
        intent.putExtra(LKWBConstants.BLOG_DATA, beginTestDataResponse)
        intent.putExtra(LKWBConstants.BLOG_PACKAGE, packageId)
        startActivity(intent)
        finish()
    }

    override fun onSuccess(beginTestResponse: BeginTestResponse) {
        beginTestDataResponse = beginTestResponse.data!!
        beginTestBinding.readTV.text =
            beginTestDataResponse!!.reading.toString() + " " + getString(R.string.reading_questions)
        beginTestBinding.listenTV.text =
            beginTestDataResponse!!.listening.toString() + " " + getString(R.string.listening_questions)
        beginTestBinding.timeTV.text =
            beginTestDataResponse!!.time.toString() + " " + getString(R.string.minutes)
        beginTestBinding.fullMarkTV.text =
            getString(R.string.full_marks) + " " + beginTestDataResponse!!.fullMarks.toString()
        beginTestBinding.passMarkTV.text =
            getString(R.string.pass_marks) + " " + beginTestDataResponse!!.passMarks.toString()
        beginTestBinding.beginTestPage.visibility = View.VISIBLE

    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))

    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), this)
    }

    override fun onShowProgressBar(status: Boolean) {
        beginTestBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        showBackDialog()
        return true
    }

    override fun onBackPressed() {
        showBackDialog()
    }

    private fun showBackDialog() {
        if (purchase == true)
            AppUtils.showDialog(this,
                title = getString(R.string.you_have_purchased),
                getString(R.string.want_go_back),
                titleOfPositiveButton = getString(R.string.ok),
                titleOfNegativeButton = getString(R.string.cancel),
                positiveButtonFunction = {
                    super.onBackPressed()
                },
                negativeButtonFunction = { }
            )
        else
            finish()
    }

    override val binding: ActivityBeginTestBinding
        get() = ActivityBeginTestBinding.inflate(layoutInflater)

}