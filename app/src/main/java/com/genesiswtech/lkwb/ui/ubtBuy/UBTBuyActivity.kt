package com.genesiswtech.lkwb.ui.ubtBuy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.esewa.android.sdk.payment.ESewaConfiguration
import com.esewa.android.sdk.payment.ESewaPayment
import com.esewa.android.sdk.payment.ESewaPaymentActivity
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.ESewaResponse
import com.genesiswtech.lkwb.databinding.ActivityUbtBuyBinding
import com.genesiswtech.lkwb.ui.invoice.InvoiceActivity
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubtBuy.model.UBTBuyResponse
import com.genesiswtech.lkwb.ui.ubtBuy.presenter.UBTBuyPresenter
import com.genesiswtech.lkwb.ui.ubtBuy.view.IUBTBuyView
import com.genesiswtech.lkwb.utils.*
import com.google.android.material.chip.Chip
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.primitives.Ints
import com.google.gson.Gson
import com.khalti.checkout.helper.Config.Builder
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.checkout.helper.OnCheckOutListener
import com.khalti.checkout.helper.PaymentPreference
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class UBTBuyActivity : BaseActivity<ActivityUbtBuyBinding>(), IUBTBuyView {

    private lateinit var ubtBuyBinding: ActivityUbtBuyBinding
    private var ubtTestData: UBTTestDataResponse? = null
    private var packageData: PackageDataResponse? = null
    private var ubtBuyPresenter: UBTBuyPresenter? = null

    private val REQUEST_CODE_PAYMENT = 1
    private var eSewaConfiguration: ESewaConfiguration? = null
    private lateinit var orderId: String
    private lateinit var khaltiCheckOut: KhaltiCheckOut
    private var packageType: Boolean = false

    private var price: Int = 0
    private var id: Int = 0
    private lateinit var title: String
    private var sets: String = "sets"

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageType = intent.getBooleanExtra(LKWBConstants.BLOG_PACKAGE, false)

        ubtBuyBinding = DataBindingUtil.setContentView(this, R.layout.activity_ubt_buy)
        ubtBuyBinding.ubtBuyHandler = this
        initDependencies()

        if (!packageType) {
            ubtBuyBinding.packageDetail.visibility = View.GONE
            ubtTestData =
                intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? UBTTestDataResponse
            AppUtils.actionBarWithTitle(this, ubtTestData!!.title!!)
            orderId = getSetOrderID(
                ubtTestData!!.id,
                LKWBPreferencesManager.getString(LKWBConstants.USER_ID),
                Date().time
            )
            sets = "sets"
            setData(ubtTestData!!)
            //khalti configuration for set
            makeKhaltiPayment(
                price,
                orderId,
                title
            )
            if (price == 0)
                ubtBuyBinding.rewardInfoTV.visibility = View.GONE

        } else {
            ubtBuyBinding.setDetail.visibility = View.GONE
            sets = "package"
            packageData =
                intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? PackageDataResponse
            AppUtils.actionBarWithTitle(this, getString(R.string.ubt_package))
            orderId = getPackageOrderID(
                packageData!!.id,
                LKWBPreferencesManager.getString(LKWBConstants.USER_ID),
                Date().time
            )
            setPackageData(packageData!!)
            makeKhaltiPayment(
                price,
                orderId,
                title
            )
//            ubtBuyBinding.rewardInfoTV.visibility = View.GONE
        }

        ubtBuyBinding.rewardPointTV.text = String.format(
            getString(R.string.ubt_buy_note), price
        )
        ubtBuyBinding.rewardPointTV1.text = String.format(
            getString(R.string.ubt_buy_note), price
        )

        //esewa configuration
        eSewaConfiguration = ESewaConfiguration().clientId(getString(R.string.esewa_merchant_id))
            .secretKey(getString(R.string.esewa_secret_key))
            .environment(ESewaConfiguration.ENVIRONMENT_PRODUCTION)

    }

    private fun getSetOrderID(setId: Int?, userId: String?, timeStamp: Long?): String {
        var orderId = ""
        orderId = "s-" + setId + "-" + "u-" + userId + "-" + timeStamp
        return orderId;
    }

    private fun getPackageOrderID(setId: Int?, userId: String?, timeStamp: Long?): String {
        var orderId = ""
        orderId = "p-" + setId + "-" + "u-" + userId + "-" + timeStamp
        return orderId;
    }

    private fun addChipToGroup(text: String, position: Int) {
        val linear = layoutInflater.inflate(
            R.layout.layout_single_chip,
            ubtBuyBinding.setChipGroup,
            false
        ) as LinearLayout
        val chip: Chip = linear.findViewById(R.id.chipCpp)
        chip.text = (text)
        chip.isClickable = false
        chip.isCheckable = false
        ubtBuyBinding.setChipGroup.addView(linear as View)

        if (position % 3 == 0) {
            linear.setBackgroundResource(R.drawable.ubt_blue_background)
        } else if ((position + 1) % 3 == 0) {
            linear.setBackgroundResource(R.drawable.ubt_pink_background)
        } else if ((position - 1) % 3 == 0) {
            linear.setBackgroundResource(R.drawable.ubt_yellow_background)
        }
    }

    private fun setPackageData(packageData: PackageDataResponse) {
        price = packageData.price!!.toInt()
        id = packageData.id!!.toInt()
        title = packageData.category.toString()
        ubtBuyBinding.packageNameTV.text = packageData.category
        val text = String.format(
            getString(R.string.this_package_has_sets), packageData.sets.size.toString()
        )
        ubtBuyBinding.packagePriceTV.text = getString(R.string.price_rs) + " " + packageData.price
        if (text.isEmpty()) ubtBuyBinding.packageDetailTV.text = getString(R.string.na)
        else ubtBuyBinding.packageDetailTV.text = text
        ubtBuyBinding.freeRB.isEnabled = false
        ubtBuyBinding.freeRB.visibility = View.GONE

        ubtBuyBinding.packageTitleTV.text = packageData.category + " " + getString(R.string.packag)
        ubtBuyBinding.packageCountTV.text = text
        ubtBuyBinding.packagePricesTV.text = getString(R.string.price_rs) + " " + packageData.price

        for (i in 0..(packageData.sets.size - 1))
            addChipToGroup(packageData.sets[i].title.toString(), i)

//        for (item in packageData.sets)
//            addChipToGroup(item.title.toString())
    }


    fun findIndex(arr: IntArray?, t: Int): Int {
        return Ints.indexOf(arr, t)
    }

    private fun setData(ubtTestDataResponse: UBTTestDataResponse) {
        price = ubtTestDataResponse.price!!.toInt()
        id = ubtTestDataResponse.id!!.toInt()
        title = ubtTestDataResponse.title.toString()
        ubtBuyBinding.packageNameTV.text = ubtTestDataResponse.title
        if (ubtTestDataResponse.description != null) ubtBuyBinding.packageDetailTV.text =
            Html.fromHtml(ubtTestDataResponse.description, 0).trim()
        else ubtBuyBinding.packageDetailTV.text = getString(R.string.na)
        if (ubtTestDataResponse.price == 0) {
            ubtBuyBinding.packagePriceTV.text = getString(R.string.free)
            ubtBuyBinding.freeRB.isChecked = true
            ubtBuyBinding.esewaRB.visibility = View.GONE
            ubtBuyBinding.khaltiRB.visibility = View.GONE
            ubtBuyBinding.rewardPointRB.visibility = View.GONE
        } else {
            ubtBuyBinding.freeRB.isEnabled = false
            ubtBuyBinding.freeRB.visibility = View.GONE
            ubtBuyBinding.packagePriceTV.text =
                getString(R.string.price_rs) + " " + ubtTestDataResponse.price
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onProceedButtonClick(v: View?) {
        val id: Int = ubtBuyBinding.radioGroup.checkedRadioButtonId
        if (id == -1) {
            purchaseErrorDialog()
        } else {
            purchaseConfirmationDialog()
        }
    }

    private fun makeKhaltiPayment(price: Int, productId: String, productName: String) {
        val priceInPaisa: Long = price.toLong() * 100
        val builder =
            Builder(
                getString(R.string.khalti_merchant_secret),
                productId,
                productName,
                priceInPaisa,
                object : OnCheckOutListener {
                    override fun onError(action: String, errorMap: Map<String, String>) {
                        Log.i(action, errorMap.toString())
                        showSnackBar(action)
                    }

                    override fun onSuccess(data: Map<String, Any>) {
                        Log.i("success New", data.toString())
                        val price: String = data["amount"].toString()
                        val pp = price.toInt() / 100
                        buyKhaltiApiCall(
                            sets,
                            id,
                            LKWBConstants.KHALTI,
                            pp.toString(),
                            true,
                            data["idx"].toString(),
                            data["product_identity"].toString(),
                            data["product_name"].toString(),
                            data["token"].toString(),
                            data["mobile"].toString()
                        )
                    }
                })
                .paymentPreferences(object : ArrayList<PaymentPreference?>() {
                    init {
                        add(PaymentPreference.KHALTI)

                    }
                })

        val config = builder.build()
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val xmlView = inflater.inflate(R.layout.layout_khalti, null, false)
        ubtBuyBinding.KhaltiBtn.setCustomView(xmlView)
        khaltiCheckOut = KhaltiCheckOut(this, config)
    }

    private fun makeEsewaPayment(
        price: String, productName: String, productId: String, callBackUrl: String
    ) {
        val eSewaPayment = ESewaPayment(
            price, productName, productId, callBackUrl
        )
        val intent = Intent(this, ESewaPaymentActivity::class.java)
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration)
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment)
        startActivityForResult(intent, REQUEST_CODE_PAYMENT)
    }

    override fun onBackButtonClick(v: View?) {
        finish()
    }

    override fun initDependencies() {
        ubtBuyPresenter = UBTBuyPresenter(this, application)
    }

    override fun onSuccess(ubtBuyResponse: UBTBuyResponse) {
        if (ubtBuyResponse.code == 200) {
            if (ubtBuyResponse.data!!.paymentMethod == "free") {
                updateUBTList()
                navigateToPurchaseTab()
                finish()
            } else {
                val intent = Intent(this, InvoiceActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_RESULT, ubtBuyResponse.data)
                intent.putExtra(LKWBConstants.BLOG_DATA, packageData)
                intent.putExtra(LKWBConstants.DATA_TITLE, title)
                intent.putExtra(LKWBConstants.DATA_TYPE, sets)
                intent.putExtra(LKWBConstants.DATA_PURCHASE, true)
                finish()
                startActivity(intent)
            }

        }
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
        ubtBuyBinding.llProgressBar.linear.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                val s = data!!.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s.toString())
                val gson = Gson()
                var esewaData = gson.fromJson(s.toString(), ESewaResponse::class.java)
//                s-9-u-3-113415573749596
                buyApiCall(
                    sets,
                    id,
                    LKWBConstants.ESEWA,
                    orderId,
                    price.toString(),
                    esewaData.transactionDetails!!.referenceId.toString(), true
                )
            } else if (resultCode == RESULT_CANCELED) {
                showSnackBar(getString(R.string.payment_canceled))
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                val s = data!!.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                showSnackBar(getString(R.string.payment_failed))
                Log.i("Proof of Payment", s.toString())
            }
        }
    }

    private fun purchaseErrorDialog() {
        AppUtils.showPurchaseDialog(this,
            title = getString(R.string.no_purchase_method),
            getString(R.string.select_any_method),
            titleOfPositiveButton = getString(R.string.ok),
            positiveButtonFunction = {})
    }

    private fun purchaseConfirmationDialog() {
        var subtitle = getString(R.string.buy_test_free)
        if (!packageType) {
            subtitle = getString(R.string.buy_test_free)
            if (ubtBuyBinding.esewaRB.isChecked)
                subtitle = getString(R.string.buy_test_esewa)
            if (ubtBuyBinding.rewardPointRB.isChecked)
                subtitle = getString(R.string.buy_test_reward)
            if (ubtBuyBinding.khaltiRB.isChecked)
                subtitle = getString(R.string.buy_test_khalti)
        } else {
            subtitle = getString(R.string.buy_package_free)
            if (ubtBuyBinding.esewaRB.isChecked)
                subtitle = getString(R.string.buy_package_esewa)
            if (ubtBuyBinding.rewardPointRB.isChecked)
                subtitle = getString(R.string.buy_package_reward)
            if (ubtBuyBinding.khaltiRB.isChecked)
                subtitle = getString(R.string.buy_package_khalti)
        }

        AppUtils.showDialog(this,
            title = title,
            subtitle,
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {

                if (ubtBuyBinding.freeRB.isChecked) {
                    buyApiCall(
                        sets,
                        id,
                        LKWBConstants.FREE,
                        orderId,
                        price.toString(),
                        "",
                        true
                    )
                } else if (ubtBuyBinding.esewaRB.isChecked) {
                    makeEsewaPayment(
                        price.toString(),
                        title,
                        id.toString(),
                        getString(R.string.esewa_callback_url)
                    )
                } else if (ubtBuyBinding.rewardPointRB.isChecked) {
                    buyApiCall(
                        sets,
                        id,
                        LKWBConstants.REWARDPOINT,
                        orderId,
                        price.toString(),
                        "",
                        true
                    )
                } else if (ubtBuyBinding.khaltiRB.isChecked) {
                    khaltiCheckOut.show()
                }

            },
            negativeButtonFunction = { })
    }


    private fun buyApiCall(
        sets: String,
        packageId: Int,
        paymentType: String,
        oid: String,
        amount: String,
        refId: String, mobile: Boolean
    ) {
        ubtBuyPresenter!!.postUBTBuy(
            applicationContext, sets, packageId, paymentType, oid, amount, refId, mobile
        )
    }

    private fun buyKhaltiApiCall(
        sets: String,
        id: Int,
        type: String,
        amt: String,
        isMobile: Boolean,
        pidx: String,
        poid: String,
        pon: String,
        txn_id: String,
        mobile: String
    ) {
        ubtBuyPresenter!!.postKhaltiUBTBuy(
            applicationContext, sets, id, type, amt, isMobile, pidx, poid, pon, txn_id, mobile
        )
    }

    private fun updateUBTList() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateUBTListFromInvoice) }
    }

    private fun navigateToPurchaseTab() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.NavigateToPurchaseTab) }
    }

    override val binding: ActivityUbtBuyBinding
        get() = ActivityUbtBuyBinding.inflate(layoutInflater)

}