package com.genesiswtech.lkwb.ui.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityInvoiceBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.invoice.view.IInvoiceView
import com.genesiswtech.lkwb.ui.mostPurchase.MostPurchaseActivity
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubtBuy.model.UBTBuyDataResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class InvoiceActivity : BaseActivity<ActivityInvoiceBinding>(), IInvoiceView {

    private lateinit var invoiceBinding: ActivityInvoiceBinding
    private var packageData: PackageDataResponse? = null
    private var ubtTestData: UBTTestDataResponse? = null
    private var buyData: UBTBuyDataResponse? = null
    private var purchase: Boolean = false
    private lateinit var title: String
    private lateinit var sets: String

    private val lkwbEventBus by inject<LKWBEventBus>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.statusBarInvoice(this)
        buyData = intent.getSerializableExtra(LKWBConstants.DATA_RESULT) as? UBTBuyDataResponse

        title = intent.getStringExtra(LKWBConstants.DATA_TITLE).toString()
        sets = intent.getStringExtra(LKWBConstants.DATA_TYPE).toString()
        if (sets == "sets")
            ubtTestData =
                intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? UBTTestDataResponse
        else
            packageData =
                intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? PackageDataResponse
        purchase = intent.getBooleanExtra(LKWBConstants.DATA_PURCHASE, false)
        invoiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_invoice)
        invoiceBinding.invoicehandler = this

        setData()
        updateUBTList()
    }

    private fun setData() {
        if (buyData!!.invoiceNumber.isNullOrEmpty()) invoiceBinding.invoiceTV.text =
            getString(R.string.na)
        else invoiceBinding.invoiceTV.text = buyData!!.invoiceNumber
        invoiceBinding.purchaseDateTV.text = buyData!!.purchaseDate
        if (sets == "package")
            invoiceBinding.purchaseNameTV.text = title + " " + getString(R.string.packag)
        else
            invoiceBinding.purchaseNameTV.text = title
        invoiceBinding.paymentMethodTV.text = buyData!!.paymentMethod
        invoiceBinding.totalAmountTV.text = buyData!!.amount

        if (sets == "sets" && buyData!!.paymentMethod != "free") {
            invoiceBinding.messageTV.text =
                invoiceBinding.messageTV.text.toString().replace("free test package", "set")
            invoiceBinding.rewardPointTV.text = String.format(
                getString(R.string.earned_five_points), "5"
            )
        }

        if (buyData!!.paymentMethod == "free")
            invoiceBinding.rewardPointTV.text = String.format(
                getString(R.string.earned_five_points), "5"
            )

        if (sets == "package") {
            invoiceBinding.messageTV.text =
                invoiceBinding.messageTV.text.toString().replace("Your free test", title)
            invoiceBinding.rewardPointTV.text = String.format(
                getString(R.string.earned_five_points), "100"
            )
        }

        if (buyData!!.paymentMethod == "reward-point" || buyData!!.paymentMethod == "rewardPoint")
            invoiceBinding.rewardInfo.visibility = View.GONE
    }

    private fun navigateToList() {
        if (sets == "sets") {
            navigateToPurchaseTab()
            finish()
        } else {
            val intent = Intent(this, MostPurchaseActivity::class.java)
            intent.putExtra(
                LKWBConstants.DATA_TITLE,
                title + " " + getString(R.string.packag)
            )
            intent.putExtra(LKWBConstants.DATA_RESULT, packageData)
            finish()
            startActivity(intent)
        }
    }

    override fun onCloseClick(v: View?) {
        navigateToList()
    }

    override fun onDoneClick(v: View?) {
        navigateToList()
    }

    private fun updateUBTList() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateUBTListFromInvoice) }
    }

    private fun navigateToPurchaseTab() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.NavigateToPurchaseTab) }
    }

    override val binding: ActivityInvoiceBinding
        get() = ActivityInvoiceBinding.inflate(layoutInflater)


}