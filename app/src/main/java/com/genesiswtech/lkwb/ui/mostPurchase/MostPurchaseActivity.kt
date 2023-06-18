package com.genesiswtech.lkwb.ui.mostPurchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityMostPurchaseBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.beginTest.BeginTestActivity
import com.genesiswtech.lkwb.ui.home.model.MostBoughtResponse
import com.genesiswtech.lkwb.ui.mostPurchase.model.PackageListDataResponse
import com.genesiswtech.lkwb.ui.mostPurchase.presenter.MostPurchasePresenter
import com.genesiswtech.lkwb.ui.mostPurchase.view.IMostPurchaseView
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.ui.ubt.UBTTestAdapter
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse
import com.genesiswtech.lkwb.ui.ubtBuy.UBTBuyActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import org.koin.android.ext.android.inject

class MostPurchaseActivity : BaseActivity<ActivityMostPurchaseBinding>(), IMostPurchaseView {

    private lateinit var mostPurchaseBinding: ActivityMostPurchaseBinding
    private var mostPurchasePresenter: MostPurchasePresenter? = null
    private var title: String? = null
    private val lkwbEventBus by inject<LKWBEventBus>()
    private var limit: Int = LKWBConstants.LIMIT
    private var page: Int = 1

    private var allUBTTestList = ArrayList<UBTTestDataResponse>()
    private lateinit var mostPurchaseAdapter: UBTTestAdapter
    private var loading: Boolean = true
    private var swipe: Boolean = false
    private var packageData: PackageDataResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        AppUtils.actionBarWithTitle(
            this,
            title!!
        )
        packageData =
            intent.getSerializableExtra(LKWBConstants.DATA_RESULT) as? PackageDataResponse
        mostPurchaseBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_most_purchase)
        mostPurchaseBinding.mostPurchaseHandler = this
        initDependencies()
        setMostPurchaseAdapter(allUBTTestList)
        updateUBTListFromInvoiceResult()
    }

    private fun apiCall(page: Int) {
        if (title == getString(R.string.new_test))
            mostPurchasePresenter!!.getNewSets(this, page.toString(), limit.toString())
        else if (title == getString(R.string.most_purchase_sets))
            mostPurchasePresenter!!.getMostBought(this, page.toString(), limit.toString())
        else {
            mostPurchasePresenter!!.getPackageList(
                this,
                packageData!!.id!!.toInt(),
                page.toString(),
                limit.toString()
            )
        }
    }

    override fun initDependencies() {
        mostPurchasePresenter = MostPurchasePresenter(this, application)
    }

    override fun onSuccess(ubtTestResponse: UBTTestResponse) {
        setUBTData(ubtTestResponse.data)
    }

    override fun onMostPurchaseSuccess(mostBoughtResponse: MostBoughtResponse) {
        setUBTData(mostBoughtResponse.data)
    }

    override fun onPackageSuccess(packageData: PackageListDataResponse) {
        setUBTData(packageData.sets)
    }

    private fun setUBTData(dataList: ArrayList<UBTTestDataResponse>) {
        if (swipe) {
            mostPurchaseBinding.swipeRefresh.isRefreshing = false
            mostPurchaseAdapter.clearList()
            swipe = false
        }
        mostPurchaseAdapter.updateList(dataList)
        loading = false
    }

    private fun setMostPurchaseAdapter(data: ArrayList<UBTTestDataResponse>) {
        mostPurchaseBinding.mostPurchaseRV.layoutManager = LinearLayoutManager(this)
        if (packageData != null)
            mostPurchaseAdapter = UBTTestAdapter(data, this, true)
        else
            mostPurchaseAdapter = UBTTestAdapter(data, this)
        mostPurchaseBinding.mostPurchaseRV.adapter = mostPurchaseAdapter

        mostPurchaseBinding.mostPurchasePage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (mostPurchaseBinding.mostPurchasePage.getChildAt(0).bottom
                <= (mostPurchaseBinding.mostPurchasePage.height + mostPurchaseBinding.mostPurchasePage.scrollY)
            ) {
                if (title == getString(R.string.new_test) || title == getString(R.string.most_purchase_sets))
                    if (!loading) {
                        page++
                        apiCall(page)
                        loading = true
                    }
            }
        })

        mostPurchaseBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        mostPurchaseBinding.swipeRefresh.setOnRefreshListener {
            swipe = true
            page = 1
            apiCall(page)
            onShowProgressBar(false)
        }

        mostPurchaseAdapter.onItemClick = {
            Log.d("TAG", it.title.toString())
            if (AppUtils.isLoggedOn()) {
                if (it.status == false) {
                    val intent = Intent(this, UBTBuyActivity::class.java)
                    intent.putExtra(LKWBConstants.BLOG_DATA, it)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, BeginTestActivity::class.java)
                    intent.putExtra(LKWBConstants.BLOG_DATA, it)
                    if (title == getString(R.string.new_test) || title == getString(R.string.most_purchase_sets))
                        intent.putExtra(LKWBConstants.BLOG_PACKAGE, "")
                    else
                        intent.putExtra(LKWBConstants.BLOG_PACKAGE, packageData!!.id.toString())
                    startActivity(intent)
                }
            } else
                AppUtils.showLoginDialog(this)
        }
        apiCall(page)
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
        mostPurchaseBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        mostPurchaseBinding.bottomProgressBar.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1)
            onShowProgressBar(status)
        else
            onShowBottomProgressBar(status)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateUBTListFromInvoiceResult() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateUBTListFromInvoice -> {
                    page = 1
                    mostPurchaseAdapter.clearList()
                    apiCall(page)
                }
                is LKWBEvents.UpdateUBTListFromResult -> {
                    page = 1
                    mostPurchaseAdapter.clearList()
                    apiCall(page)
                }
                is LKWBEvents.NavigateToPurchaseTab -> {
                    finish()
                }
                else -> {}
            }
        }
    }

    override val binding: ActivityMostPurchaseBinding
        get() = ActivityMostPurchaseBinding.inflate(layoutInflater)
}