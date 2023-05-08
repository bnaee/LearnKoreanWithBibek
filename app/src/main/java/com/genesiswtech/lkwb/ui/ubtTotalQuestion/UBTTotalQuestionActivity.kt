package com.genesiswtech.lkwb.ui.ubtTotalQuestion

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.databinding.ActivityUbtTotalQuestionBinding
import com.genesiswtech.lkwb.ui.ubtTotalQuestion.view.IUBTTotalQuestionView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants


class UBTTotalQuestionActivity : BaseActivity<ActivityUbtTotalQuestionBinding>(), IUBTTotalQuestionView {

    private lateinit var ubtTotalQuestionBinding: ActivityUbtTotalQuestionBinding
    private var listen: Int? = null
    private var read: Int? = null
    private var totalQuestionHashMap: HashMap<Int, Int> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listen = intent.getIntExtra(LKWBConstants.BLOG_DATA_Listen, 0)
        read = intent.getIntExtra(LKWBConstants.BLOG_DATA_READ, 0)
        totalQuestionHashMap =
            intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as HashMap<Int, Int>
        AppUtils.actionbar(this);
        setTitle(R.string.all_questions)
        ubtTotalQuestionBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_ubt_total_question)
        ubtTotalQuestionBinding.ubtTotalQuestionHandler = this
        setAdapter()
    }

    private fun setAdapter() {
        val listeningLayoutManager = GridLayoutManager(this, 5)
        val readingLayoutManager = GridLayoutManager(this, 5)
        ubtTotalQuestionBinding.recyclerViewListening.layoutManager = listeningLayoutManager
        ubtTotalQuestionBinding.recyclerViewReading.layoutManager = readingLayoutManager

        val listeningQuestionList: HashMap<Int, Int> = HashMap()
        val readingQuestionList: HashMap<Int, Int> = HashMap()
        for (i in 0 until listen!!) {
            listeningQuestionList[i] = totalQuestionHashMap[i]!!
        }
        for (i in 0 until read!!) {
            readingQuestionList[i] = totalQuestionHashMap[read!! + i]!!
        }

        // This loop will create 20 Views containing
        // the image with the count of view
        // This will pass the ArrayList to our Adapter
        val listeningAdapter = UBTTotalQuestionAdapter(this,0, listeningQuestionList)
        val readingAdapter = UBTTotalQuestionAdapter(this,listen!!, readingQuestionList)

        // Setting the Adapter with the recyclerview
        ubtTotalQuestionBinding.recyclerViewListening.adapter = listeningAdapter
        ubtTotalQuestionBinding.recyclerViewReading.adapter = readingAdapter

        listeningAdapter.onItemClick = {
            val intent = Intent()
            intent.putExtra("position", it)
            setResult(RESULT_OK, intent)
            finish()
        }

        readingAdapter.onItemClick = {
            val intent = Intent()
            intent.putExtra("position", listeningQuestionList.size + it)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSubmitButtonClick(v: View?) {
    }

    override val binding: ActivityUbtTotalQuestionBinding
        get() = ActivityUbtTotalQuestionBinding.inflate(layoutInflater)

}