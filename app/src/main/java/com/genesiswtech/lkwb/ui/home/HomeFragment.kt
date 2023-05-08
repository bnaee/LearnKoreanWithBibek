package com.genesiswtech.lkwb.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentHomeBinding
import com.genesiswtech.lkwb.ui.beginTest.BeginTestActivity
import com.genesiswtech.lkwb.ui.blog.BlogActivity
import com.genesiswtech.lkwb.ui.graph.GraphActivity
import com.genesiswtech.lkwb.ui.home.model.*
import com.genesiswtech.lkwb.ui.home.presenter.HomePresenter
import com.genesiswtech.lkwb.ui.home.view.IHomeDataPass
import com.genesiswtech.lkwb.ui.home.view.IHomeView
import com.genesiswtech.lkwb.ui.mostPurchase.MostPurchaseActivity
import com.genesiswtech.lkwb.ui.ubt.UBTTestAdapter
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import com.genesiswtech.lkwb.ui.ubtBuy.UBTBuyActivity
import com.genesiswtech.lkwb.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import lecho.lib.hellocharts.model.*
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home), IHomeView {

    lateinit var homeDataPasser: IHomeDataPass

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private var homePresenter: HomePresenter? = null
    private lateinit var serviceList: ArrayList<Services>

    lateinit var rewardPointTV: AppCompatTextView
    lateinit var scoreTV: AppCompatTextView
    lateinit var lastScoredTV: AppCompatTextView
    lateinit var testScrorePB: ProgressBar

    lateinit var bannerUrl: String
    lateinit var servicesAdapter: ServicesAdapter
    lateinit var adRequest: AdRequest

    private val lkwbEventBus by inject<LKWBEventBus>()

    private var axisData: ArrayList<String>? = ArrayList()
    private var yAxisData: ArrayList<Float>? = ArrayList()

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentHomeBinding.bind(itemView)
        fragmentHomeBinding = binding
        binding.homeHandler = this
        initDependencies()
        initializeAd()

        Glide.with(requireContext()).load(R.drawable.sample_banner_first)
            .into(fragmentHomeBinding!!.imgHomeBannerFirst);
        Glide.with(requireContext()).load(R.drawable.sample_banner_first)
            .into(fragmentHomeBinding!!.imgHomeBannerSecond);

        rewardPointTV = itemView.findViewById(R.id.rewardPointTV)
        scoreTV = itemView.findViewById(R.id.scoreTV)
        lastScoredTV = itemView.findViewById(R.id.lastScoredTV)
        testScrorePB = itemView.findViewById(R.id.testScrorePB)

        mostBoughtApiCall()
        newTestApiCall();
        homePresenter!!.getServices(requireContext())
        homePresenter!!.getBanner(requireContext())
//        resultHistoryApiCall()
        lastTestScoreApiCall()
        updateUBTListFromInvoiceResult()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            if (task.result != null) {
                val token: String = task.result
                if (LKWBPreferencesManager.getString(LKWBConstants.PUSH_TOKEN) == null || LKWBPreferencesManager.getString(
                        LKWBConstants.PUSH_TOKEN
                    ) != token
                )
                    storePushToken(token, "android")
            }
        }
    }

    private fun initializeAd() {
        MobileAds.initialize(activity as AppCompatActivity)
        adRequest = AdRequest.Builder().build()
        fragmentHomeBinding!!.adView.loadAd(adRequest)
        fragmentHomeBinding!!.adView1.loadAd(adRequest)
    }

    fun firstWord(input: String): String? {
        return input.split(" ").toTypedArray()[0] // Create array of words and return the 0th word
    }

    private fun mostBoughtApiCall() {
        homePresenter!!.getMostBought(requireContext(), "1", "3")
    }

    private fun newTestApiCall() {
        homePresenter!!.getNewTest(requireContext(), "1", "3")
    }

    private fun lastTestScoreApiCall() {
        homePresenter!!.getLastTestScore(requireContext())
    }

    private fun resultHistoryApiCall() {
        homePresenter!!.getResultHistoryApp(requireContext())
    }

    private fun storePushToken(deviceId: String, deviceType: String) {
        homePresenter!!.postPushToken(requireContext(), deviceId, deviceType)
    }

    private fun updateTestScoreProgressBar(correctAnswer: Int, totalQuestion: Int) {
        var pro: Long = 0
        if (totalQuestion != 0)
            pro = ((correctAnswer * 100) / totalQuestion).toLong()
        testScrorePB.progress = pro.toInt()
    }

    private fun setScoreGraph(resultHistoryDataResponse: ArrayList<ResultHistoryDataResponse>) {
        axisData!!.clear()
        yAxisData!!.clear()
        if (resultHistoryDataResponse.size > 0) {
            for (data in resultHistoryDataResponse) {
                val strList = data.name!!.split(" ").toTypedArray()
                var finalTextOne = data.name!!
                if (strList.size > 1)
                    finalTextOne = strList[0] + " " + strList[1]
                else
                    finalTextOne = strList[0]
                axisData?.add(finalTextOne)
                yAxisData?.add(data.score!!.toFloat())
            }
            setChart(axisData, yAxisData)
        }
    }

    private fun setChart(axisData: ArrayList<String>?, yAxisData: ArrayList<Float>?) {
        if (axisData!!.size == 1) {
            axisData.add("")
            yAxisData?.add(0f)
        }
//        axisData?.reverse()
//        yAxisData?.reverse()

        val yAxisValues: ArrayList<PointValue> = ArrayList()
        val axisValues: ArrayList<AxisValue> = ArrayList()

        val line = Line(yAxisValues).setColor(Color.parseColor("#00437B"))
        line.shape = ValueShape.CIRCLE
        line.isCubic = true
        line.isFilled = true
        line.setHasLabels(true)
        line.setHasLabelsOnlyForSelected(true)
        line.setHasLines(true)
        line.setHasPoints(true)

        for (i in axisData!!.indices) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in yAxisData!!.indices) {
            yAxisValues.add(PointValue(i.toFloat(), yAxisData[i].toFloat()))
        }

        val lines: ArrayList<Line> = ArrayList()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines

        val axis = Axis()
        axis.values = axisValues
        axis.textSize = 12
        axis.textColor = Color.parseColor("#666666")
        data.axisXBottom = axis

        val yAxis = Axis()
        yAxis.textColor = Color.parseColor("#666666")
        yAxis.textSize = 12
        data.axisYLeft = yAxis

//        fragmentHomeBinding!!.chart.isHovered = true
        fragmentHomeBinding!!.chart.isZoomEnabled = false
        fragmentHomeBinding!!.chart.lineChartData = data
        val viewport = Viewport(fragmentHomeBinding!!.chart.maximumViewport)
        viewport.top = Collections.max(yAxisData)
        if (Collections.min(yAxisData) == 0f)
            viewport.bottom = -1f
        if (Collections.max(yAxisData) == 0f)
            viewport.top = 1f
        fragmentHomeBinding!!.chart.maximumViewport = viewport
        fragmentHomeBinding!!.chart.currentViewport = viewport
//        fragmentHomeBinding!!.graphCV.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field
        // if not needed.
        fragmentHomeBinding = null
        super.onDestroyView()
    }

    override fun initDependencies() {
        homePresenter = HomePresenter(this, requireActivity().application)
    }

    override fun onGraphViewClick(v: View?) {
        val intent = Intent(activity, GraphActivity::class.java)
        startActivity(intent)
    }

    override fun onMostBuyViewClick(v: View?) {
        val intent = Intent(activity, MostPurchaseActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.most_purchase_sets))
        startActivity(intent)
//        passData(getString(R.string.ubt_test))
    }

    override fun onNewTestViewClick(v: View?) {
        val intent = Intent(activity, MostPurchaseActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.new_test))
        startActivity(intent)
//        passData(getString(R.string.ubt_test))
    }

    override fun onHomeBannerClick(v: View?) {
        AppUtils.showBannerImageDialog(requireContext(), bannerUrl)
    }

    override fun onMostBoughtSuccess(mostBoughtResponse: MostBoughtResponse) {
        Log.d(tag, "Response Bought Success: $mostBoughtResponse")
        onShowProgressBar(false)
        setMostBoughtAdapter(mostBoughtResponse.data)
        fragmentHomeBinding!!.mostBuyCV.visibility = View.VISIBLE
    }

    override fun onNewTestSuccess(newTestResponse: MostBoughtResponse) {
        setNewTestAdapter(newTestResponse.data)
        fragmentHomeBinding!!.newUBTCV.visibility = View.VISIBLE
    }

    override fun onServicesSuccess(serviceResponse: ServiceResponse) {
        onShowProgressBar(false)
        serviceList = serviceResponse.data!!.services
        setServiceAdapter(serviceList)
        fragmentHomeBinding!!.serviceCV.visibility = View.VISIBLE
    }

    override fun onBannerSuccess(bannerDataResponse: BannerDataResponse) {
        bannerUrl = bannerDataResponse.image.toString()
        Glide.with(requireContext()).load(bannerDataResponse.image)
            .into(fragmentHomeBinding!!.imgHomeBanner)

        fragmentHomeBinding!!.bannerHeaderTV.text = bannerDataResponse.topText
        fragmentHomeBinding!!.bannerSubTV.text = bannerDataResponse.headerText
        fragmentHomeBinding!!.homeBannerCV.visibility = View.VISIBLE
    }

    override fun onLastTestScoreSuccess(testScoreResponse: TestScoreDataResponse) {
        lastScoredTV.text =
            testScoreResponse.correctAnswers.toString() + "/" + testScoreResponse.numberOfQuestions.toString()
        val rewardPoint: Double = testScoreResponse.rewardPoint!!.toDouble()
        val rating: Double = testScoreResponse.outOf100!!.toDouble()
        if (BigDecimal(rewardPoint).scale() <= 0)
            rewardPointTV.text = rewardPoint.toInt().toString()
        else rewardPointTV.text = rewardPoint.toString()

        if (BigDecimal(rating).scale() <= 0)
            scoreTV.text = rating.toInt().toString() + "/" + "100"
        else scoreTV.text = rating.toString() + "/" + "100"

//        scoreTV.text = testScoreResponse.rating.toString()
        updateTestScoreProgressBar(
            testScoreResponse.correctAnswers!!.toInt(),
            testScoreResponse.numberOfQuestions!!.toInt()
        )
        fragmentHomeBinding!!.scoreCV.visibility = View.VISIBLE

    }

    override fun onResultHistoryAppSuccess(resultHistoryDataResponse: ArrayList<ResultHistoryDataResponse>) {
        setScoreGraph(resultHistoryDataResponse)
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
        fragmentHomeBinding!!.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    private fun setMostBoughtAdapter(data: ArrayList<UBTTestDataResponse>) {
        fragmentHomeBinding!!.recyclerViewMostBuySets.layoutManager = LinearLayoutManager(activity)
        val mostBoughtAdapter = UBTTestAdapter(data, requireActivity())
        fragmentHomeBinding!!.recyclerViewMostBuySets.adapter = mostBoughtAdapter
        mostBoughtAdapter.onItemClick = {
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
    }

    private fun setNewTestAdapter(data: ArrayList<UBTTestDataResponse>) {
        fragmentHomeBinding!!.recyclerViewNewTest.layoutManager = LinearLayoutManager(activity)
        val newTestAdapter = UBTTestAdapter(data, requireActivity())
        fragmentHomeBinding!!.recyclerViewNewTest.adapter = newTestAdapter
        newTestAdapter.onItemClick = {
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
    }

    private fun setServiceAdapter(data: ArrayList<Services>) {
        val layoutManager = GridLayoutManager(activity, 3)
        fragmentHomeBinding!!.recyclerViewServices.layoutManager = layoutManager
        servicesAdapter = ServicesAdapter(data, requireActivity())
        fragmentHomeBinding!!.recyclerViewServices.adapter = servicesAdapter
        servicesAdapter.onItemClick = {
            Log.d("TAG", it.title.toString())
            if (it.title.toString().contains(getString(R.string.blog))) {
                val intent = Intent(requireContext(), BlogActivity::class.java)
                startActivity(intent)
            } else
                passData(it.title.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeDataPasser = context as IHomeDataPass
    }

    private fun passData(data: String) {
        homeDataPasser.onHomeDataPass(data)
    }

    private fun updateUBTListFromInvoiceResult() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateUBTListFromInvoice -> {
                    mostBoughtApiCall()
                    newTestApiCall()
                }
                is LKWBEvents.UpdateUBTListFromResult -> {
                    mostBoughtApiCall()
                    newTestApiCall()
                    lastTestScoreApiCall()
                    resultHistoryApiCall()
                }
                else -> {}
            }
        }
    }

}

