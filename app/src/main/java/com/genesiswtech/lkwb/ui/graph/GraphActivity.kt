package com.genesiswtech.lkwb.ui.graph

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityGraphBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.graph.presenter.GraphPresenter
import com.genesiswtech.lkwb.ui.graph.view.IGraphView
import com.genesiswtech.lkwb.ui.home.model.ResultHistoryDataResponse
import com.genesiswtech.lkwb.utils.AppUtils
import lecho.lib.hellocharts.model.*
import java.util.*
import kotlin.collections.ArrayList

class GraphActivity : BaseActivity<ActivityGraphBinding>(), IGraphView {

    private lateinit var graphBinding: ActivityGraphBinding
    private var graphPresenter: GraphPresenter? = null

    private var axisData: java.util.ArrayList<String>? = java.util.ArrayList()
    private var yAxisData: java.util.ArrayList<Float>? = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.actionbar(this)
        graphBinding = DataBindingUtil.setContentView(this, R.layout.activity_graph)
        graphBinding.graphHandler = this
        initDependencies()
        graphPresenter!!.getResultHistoryApp(this)
    }

    override fun initDependencies() {
        graphPresenter = GraphPresenter(this, application)
    }

    override fun onResultHistoryAppSuccess(resultHistoryDataResponse: ArrayList<ResultHistoryDataResponse>) {
        setScoreGraph(resultHistoryDataResponse)
    }

    private fun setScoreGraph(resultHistoryDataResponse: java.util.ArrayList<ResultHistoryDataResponse>) {
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

    private fun setChart(
        axisData: java.util.ArrayList<String>?,
        yAxisData: java.util.ArrayList<Float>?
    ) {
        if (axisData!!.size == 1) {
            axisData.add("")
            yAxisData?.add(0f)
        }
//        axisData?.reverse()
//        yAxisData?.reverse()

        val yAxisValues: java.util.ArrayList<PointValue> = java.util.ArrayList()
        val axisValues: java.util.ArrayList<AxisValue> = java.util.ArrayList()

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

        val lines: java.util.ArrayList<Line> = java.util.ArrayList()
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
        graphBinding!!.chart.isZoomEnabled = false
        graphBinding!!.chart.lineChartData = data
        val viewport = Viewport(graphBinding!!.chart.maximumViewport)
        viewport.top = Collections.max(yAxisData)
        if (Collections.min(yAxisData) == 0f)
            viewport.bottom = -1f
        if (Collections.max(yAxisData) == 0f)
            viewport.top = 1f
        graphBinding!!.chart.maximumViewport = viewport
        graphBinding!!.chart.currentViewport = viewport
        graphBinding!!.graphCV.visibility = View.VISIBLE

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
        graphBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override val binding: ActivityGraphBinding
        get() = ActivityGraphBinding.inflate(layoutInflater)

}