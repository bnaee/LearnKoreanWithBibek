package com.genesiswtech.lkwb.ui.ubt

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentUbtTestBinding
import com.genesiswtech.lkwb.ui.beginTest.BeginTestActivity
import com.genesiswtech.lkwb.ui.mostPurchase.MostPurchaseActivity
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse
import com.genesiswtech.lkwb.ui.ubt.presenter.UBTTestPresenter
import com.genesiswtech.lkwb.ui.ubt.view.IUBTTestView
import com.genesiswtech.lkwb.ui.ubtBuy.UBTBuyActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class UBTTestFragment : Fragment(R.layout.fragment_ubt_test), IUBTTestView {

    private var allUBTTestList = ArrayList<UBTTestDataResponse>()
    private var ubtTestPresenter: UBTTestPresenter? = null
    private lateinit var fragmentUbtTestBinding: FragmentUbtTestBinding
    private var limit: Int? = LKWBConstants.LIMIT
    private var page: Int = 1
    private var type: String = "unpurchased"
    private var sort: String = "free"
    private var packageList = ArrayList<PackageDataResponse>()
    private lateinit var ubtTestAdapter: UBTTestAdapter
    private lateinit var packageAdapter: PackageAdapter
    private var loading: Boolean = true
    private var swipe: Boolean = false

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
//        AppUtils.actionBarFragment(requireActivity(), getString(R.string.ubt_test))
        val binding = FragmentUbtTestBinding.bind(itemView)
        fragmentUbtTestBinding = binding
        binding.ubtTestHandler = this
        initDependencies()
        setUBTTestAdapter()
        setPackageAdapter()
        updateUBTListFromInvoiceResult()

    }

    private fun allSetApiCall() {
        ubtTestPresenter!!.getUBTTest(
            requireContext(),
            page.toString(),
            limit.toString(),
            type,
            sort
        )
    }

    private fun allPackagesApiCall() {
        ubtTestPresenter!!.getAllPackages(requireContext())
    }

    private fun setPackageAdapter() {
        fragmentUbtTestBinding.packageRV.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        packageAdapter = PackageAdapter(packageList, requireActivity())
        fragmentUbtTestBinding.packageRV.adapter = packageAdapter
        packageAdapter.onItemClick =
            {
                packageIntent(it)
            }
        allPackagesApiCall()
    }

    private fun setUBTTestAdapter() {
        fragmentUbtTestBinding.recyclerViewNewUBTTest.layoutManager =
            LinearLayoutManager(activity)
        ubtTestAdapter = UBTTestAdapter(allUBTTestList, requireActivity())
        fragmentUbtTestBinding.recyclerViewNewUBTTest.adapter = ubtTestAdapter

        fragmentUbtTestBinding.ubtTestPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!loading) {
                    page++
                    allSetApiCall()
                    loading = true
                }
            }
        })

        fragmentUbtTestBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        fragmentUbtTestBinding.swipeRefresh.setOnRefreshListener {
            swipe = true
            page = 1
            allSetApiCall()
            onShowProgressBar(false)
        }

        ubtTestAdapter.onItemClick =
            {
                if (it.status == false) {
                    val intent = Intent(requireContext(), UBTBuyActivity::class.java)
                    intent.putExtra(LKWBConstants.BLOG_DATA, it)
                    startActivity(intent)
                } else {
                    val intent = Intent(requireContext(), BeginTestActivity::class.java)
                    intent.putExtra(LKWBConstants.BLOG_DATA, it)
                    startActivity(intent)
                }
            }
        allSetApiCall()
    }

    override fun initDependencies() {
        ubtTestPresenter = UBTTestPresenter(this, requireActivity().application)
    }

    private fun packageIntent(packageData: PackageDataResponse) {
        if (packageData.status == false) {
            val intent = Intent(requireContext(), UBTBuyActivity::class.java)
            intent.putExtra(LKWBConstants.BLOG_DATA, packageData)
            intent.putExtra(LKWBConstants.BLOG_PACKAGE, true)
            startActivity(intent)
        } else {
            val intent = Intent(activity, MostPurchaseActivity::class.java)
            intent.putExtra(
                LKWBConstants.DATA_TITLE,
                packageData.category + " " + getString(R.string.packag)
            )
            intent.putExtra(LKWBConstants.DATA_RESULT, packageData)
            startActivity(intent)
//            navigateToPurchaseTab()
        }
    }

    override fun onPackageSuccess(packageData: ArrayList<PackageDataResponse>) {
        packageAdapter.updateList(packageData)
    }

    override fun onSuccess(ubtTestResponse: UBTTestResponse) {
        if (swipe) {
            fragmentUbtTestBinding.swipeRefresh.isRefreshing = false
            ubtTestAdapter.clearList()
            swipe = false
        }
        ubtTestAdapter.updateList(ubtTestResponse.data)
        fragmentUbtTestBinding.ubtTestPage.visibility = View.VISIBLE
        loading = false
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), requireActivity())
    }

    override fun onShowProgressBar(status: Boolean) {
        fragmentUbtTestBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        fragmentUbtTestBinding.bottomProgressBar.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1)
            onShowProgressBar(status)
        else
            onShowBottomProgressBar(status)
    }

    private fun updateUBTListFromInvoiceResult() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateUBTListFromInvoice -> {
                    page = 1
                    packageAdapter.clearList()
                    ubtTestAdapter.clearList()
                    allSetApiCall()
                    allPackagesApiCall()
                }
                is LKWBEvents.UpdateUBTListFromResult -> {
                    page = 1
                    packageAdapter.clearList()
                    ubtTestAdapter.clearList()
                    allSetApiCall()
                    allPackagesApiCall()
                }
                else -> {}
            }
        }
    }

    private fun navigateToPurchaseTab() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.NavigateToPurchaseTab) }
    }

}