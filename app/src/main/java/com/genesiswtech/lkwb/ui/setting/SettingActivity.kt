package com.genesiswtech.lkwb.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.changeLanguage.ChangeLanguageActivity
import com.genesiswtech.lkwb.ui.favourite.FavouriteActivity
import com.genesiswtech.lkwb.ui.favourite.FavouriteAdapter
import com.genesiswtech.lkwb.ui.login.LoginActivity
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.ui.profile.ProfileActivity
import com.genesiswtech.lkwb.ui.setting.presenter.SettingPresenter
import com.genesiswtech.lkwb.ui.setting.view.ISettingView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.DividerItemDecoratorGrey
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import org.koin.android.ext.android.inject

class SettingActivity : BaseActivity<ActivitySettingBinding>(), ISettingView {

    private lateinit var settingBinding: ActivitySettingBinding
    private var settingPresenter: SettingPresenter? = null

    private val lkwbEventBus by inject<LKWBEventBus>()
    var language: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.actionbar(this)
        setTitle(R.string.setting)
        settingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_setting)
        settingBinding.settingHandler = this
        initDependencies()
        val data = ArrayList<String>()
        if (AppUtils.isLoggedOn()) {
            data.add(getString(R.string.profile))
            data.add(getString(R.string.favourite))
        } else {
            settingBinding.logOutBtn.visibility = View.GONE
            data.add(getString(R.string.login))
        }

        data.add(getString(R.string.change_language))
        setSettingAdapter(data)

        changeLanguage()

    }

    private fun setSettingAdapter(data: ArrayList<String>) {
        settingBinding.settingRV.layoutManager = LinearLayoutManager(this)
        val settingAdapter = FavouriteAdapter(data)
        settingBinding.settingRV.adapter = settingAdapter
        settingBinding.settingRV.addItemDecorationWithoutLastItem()
        settingAdapter.onItemClick = {
            if (it == getString(R.string.profile)) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            } else if (it == getString(R.string.change_language)) {
                val intent = Intent(this, ChangeLanguageActivity::class.java)
                startActivity(intent)
            } else if (it == getString(R.string.login)) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager)
            return
        addItemDecoration(DividerItemDecoratorGrey(context))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initDependencies() {
        settingPresenter = SettingPresenter(this, application)
    }

    override fun onLogOutClick(v: View?) {
        showLogoutDialog()
    }

    override fun onLogOutSuccess(logOutResponse: LogOutResponse) {
        AppUtils.logOutCall(this)
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
        settingBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    private fun showLogoutDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.logout),
            getString(R.string.want_to_logout),
            titleOfPositiveButton = getString(R.string.logout),
            titleOfNegativeButton = getString(R.string.cancel),
            positiveButtonFunction = {
                settingPresenter!!.postLogOut(this)
            },
            negativeButtonFunction = { }
        )
    }

    override fun onResume() {
        super.onResume()
        languageRestart()
    }

    private fun changeLanguage() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.ChangeLanguage -> {
                    language = true
                }
                else -> {}
            }
        }
    }

    private fun languageRestart() {
        if (language) {
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
            language = false
        }
    }

    override val binding get() = ActivitySettingBinding.inflate(layoutInflater)

}