package com.genesiswtech.lkwb.ui.mostPurchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentMostPurchaseBinding
import com.genesiswtech.lkwb.ui.beginTest.BeginTestActivity
import com.genesiswtech.lkwb.ui.home.model.MostBoughtResponse
import com.genesiswtech.lkwb.ui.mostPurchase.model.PackageListDataResponse
import com.genesiswtech.lkwb.ui.mostPurchase.presenter.MostPurchasePresenter
import com.genesiswtech.lkwb.ui.mostPurchase.view.IMostPurchaseView
import com.genesiswtech.lkwb.ui.ubt.UBTTestAdapter
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse
import com.genesiswtech.lkwb.ui.ubtBuy.UBTBuyActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import org.koin.android.ext.android.inject

class MostPurchaseFragment : Fragment(R.layout.fragment_most_purchase), IMostPurchaseView {

    private lateinit var fragmentMostPurchaseBinding: FragmentMostPurchaseBinding
    private var mostPurchasePresenter: MostPurchasePresenter? = null
    private var limit: Int? = 20
    private var page: Int? = 1
    private var type: String = "purchased"
    private var sort: String = "free"
    private val lkwbEventBus by inject<LKWBEventBus>()


    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentMostPurchaseBinding.bind(itemView)
        fragmentMostPurchaseBinding = binding
        initDependencies()
        purchasedApiCall()
        updateUBTListFromInvoiceResult()
    }

    private fun purchasedApiCall() {
        mostPurchasePresenter!!.getUBTTest(
            requireContext(),
            page.toString(),
            limit.toString(),
            type, sort
        )
    }

    override fun initDependencies() {
        mostPurchasePresenter = MostPurchasePresenter(this, requireActivity().application)
    }

    override fun onSuccess(ubtTestResponse: UBTTestResponse) {
        fragmentMostPurchaseBinding.mostPurchaseRV.layoutManager =
            LinearLayoutManager(requireContext())
        val mostPurchaseAdapter = UBTTestAdapter(ubtTestResponse.data, requireContext())
        fragmentMostPurchaseBinding.mostPurchaseRV.adapter = mostPurchaseAdapter
        mostPurchaseAdapter.onItemClick = {
            Log.d("TAG", it.title.toString())
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
        if (mostPurchaseAdapter.itemCount == 0)
            fragmentMostPurchaseBinding.noDataTV.visibility = View.VISIBLE
        else
            fragmentMostPurchaseBinding.noDataTV.visibility = View.GONE
    }

    override fun onMostPurchaseSuccess(mostBoughtResponse: MostBoughtResponse) {

    }

    override fun onPackageSuccess(packageData: PackageListDataResponse) {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), activity)
    }

    override fun onShowProgressBar(status: Boolean) {
        fragmentMostPurchaseBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {

    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {

    }

    private fun updateUBTListFromInvoiceResult() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateUBTListFromInvoice -> {
                    purchasedApiCall()
                }
                is LKWBEvents.UpdateUBTListFromResult -> {
                    purchasedApiCall()
                }
                else -> {}
            }
        }
    }
}