package com.genesiswtech.lkwb.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityMenuBinding
import com.genesiswtech.lkwb.ui.blog.BlogActivity
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.menu.model.ReclaimResponse
import com.genesiswtech.lkwb.ui.menu.presenter.MenuPresenter
import com.genesiswtech.lkwb.ui.menu.view.IMenuView
import com.genesiswtech.lkwb.ui.web.LKWBWebActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.DividerItemDecorator
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.Singleton


class MenuActivity : BaseActivity<ActivityMenuBinding>(), IMenuView {

    private lateinit var menuBinding: ActivityMenuBinding
    private var menuPresenter: MenuPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.statusBarMenu(this)
        menuBinding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        menuBinding.menuHandler = this
        if (!AppUtils.isLoggedOn()) {
            menuBinding.logOutBtn.visibility = View.GONE
            menuBinding.reclaim.visibility = View.GONE
        }
        initDependencies()
        setMenuAdapter()
    }

    private fun setMenuAdapter() {
        menuBinding.recyclerViewMenu.layoutManager = LinearLayoutManager(this)
        val menuData = ArrayList<String>()
        menuData.add(getString(R.string.home))
        menuData.add(getString(R.string.about_us))
        menuData.add(getString(R.string.services))
        menuData.add(getString(R.string.blog))
        val menuAdapter = MenuAdapter(menuData, this)
        menuBinding.recyclerViewMenu.adapter = menuAdapter
        menuBinding.recyclerViewMenu.addItemDecorationWithoutLastItem()

        menuAdapter.onItemClick = {
            when (it) {
                getString(R.string.about_us) -> {
                    callBrowser("https://lkwb.com.np/aboutus")
                }
                getString(R.string.services) -> {
                    callBrowser("https://lkwb.com.np/services")
                }
                getString(R.string.blog) -> {
                    val intent = Intent(this, BlogActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val a: Singleton? = Singleton.instance
                    a!!.setData(it)
                    finish()
                }
            }
        }
    }

    private fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager)
            return
        addItemDecoration(DividerItemDecorator(context))
    }

    override fun initDependencies() {
        menuPresenter = MenuPresenter(this, application)
    }

    override fun onCloseClick(v: View?) {
        finish()
    }

    override fun onUsefulLinksClick(v: View?) {
    }

    override fun onPrivacyPolicyClick(v: View?) {
        callBrowser("https://lkwb.com.np/privacy-policy")
    }

    override fun onTermsConditionsClick(v: View?) {
        callBrowser("https://lkwb.com.np/services")
    }

    override fun onFAQClick(v: View?) {
        callBrowser("https://lkwb.com.np/faq")
    }

    override fun onContactUsClick(v: View?) {
        callBrowser("https://lkwb.com.np/contactus")
    }

    override fun onGuideClick(v: View?) {
        callBrowser("https://lkwb.com.np/faq")
    }

    override fun onWordCardClick(v: View?) {
    }

    override fun onReclaimClick(v: View?) {
        AppUtils.showReclaimDialog(
            this,
            positiveButtonFunction = { txn_id: String, gateway: String ->
                if (txn_id.isBlank()) {
                    showSnackBar(getString(R.string.txn_required))
                } else {
                    menuPresenter!!.postReclaim(this, txn_id, gateway)

                }
            })
    }

    override fun onLogOutClick(v: View?) {
        showLogoutDialog()
    }

    override fun onReclaimSuccess(reclaimResponse: ReclaimResponse) {
        showSnackBar(getString(R.string.reclaim_success))
    }

    override fun onLogOutSuccess(logOutResponse: LogOutResponse) {
        AppUtils.logOutCall(this)
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), this)
    }

    override fun onShowProgressBar(status: Boolean) {
        menuBinding.llProgressBar.linear.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    private fun callBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun callLKWBBrowser(title: String, url: String) {
        val intent = Intent(this, LKWBWebActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, title)
        intent.putExtra(LKWBConstants.DATA_LINK, url)
        startActivity(intent)
    }

    private fun showLogoutDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.logout),
            getString(R.string.want_to_logout),
            titleOfPositiveButton = getString(R.string.logout),
            titleOfNegativeButton = getString(R.string.cancel),
            positiveButtonFunction = {
                menuPresenter!!.postLogOut(this)
            },
            negativeButtonFunction = { }
        )
    }

    override val binding: ActivityMenuBinding
        get() = ActivityMenuBinding.inflate(layoutInflater)

}