package com.genesiswtech.lkwb.ui.ubtResultDetail

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.databinding.ActivityUbtResultDetailBinding
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse
import com.genesiswtech.lkwb.ui.ubtResultDetail.view.IUBTResultDetailView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.DividerItemDecorator
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager


class UBTResultDetailActivity : BaseActivity<ActivityUbtResultDetailBinding>(), IUBTResultDetailView {

    private lateinit var ubtResultDetailBinding: ActivityUbtResultDetailBinding

    private var beginTestDataResponse: BeginTestDataResponse? = null
    private var totalQuestionHashMap: HashMap<Int, Int> = HashMap()
    private var correctAnswerHashMap: HashMap<Int, Int> = HashMap()
    private var position: Int? = 0
    var uBTResultDetailAdapter: UBTResultDetailAdapter? = null


    override fun onSaveInstanceState(InstanceState: Bundle) {
        super.onSaveInstanceState(InstanceState)
        InstanceState.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppUtils.actionbar(this)
        setTitle(R.string.result)
        ubtResultDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_ubt_result_detail)
        ubtResultDetailBinding.ubtResultDetailHandler = this

        position = intent.getIntExtra(LKWBConstants.DATA_POSITION, 0)
        beginTestDataResponse =
            intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? BeginTestDataResponse
        totalQuestionHashMap =
            intent.getSerializableExtra(LKWBConstants.DATA_SELECTED_HASH) as HashMap<Int, Int>
        correctAnswerHashMap =
            intent.getSerializableExtra(LKWBConstants.DATA_CORRECT_HASH) as HashMap<Int, Int>

        val testData =
            LKWBPreferencesManager.get<UBTQuestionResponse>("KEY_SET")
        setResultAdapter(testData!!.data)
    }

    private fun setResultAdapter(ubtQuestionDataResponseList: ArrayList<UBTQuestionDataResponse>) {
        ubtResultDetailBinding.resultRV.layoutManager = LinearLayoutManager(this)
        uBTResultDetailAdapter =
            UBTResultDetailAdapter(
                this,
                ubtQuestionDataResponseList,
                totalQuestionHashMap,
                correctAnswerHashMap
            )
        ubtResultDetailBinding.resultRV.adapter = uBTResultDetailAdapter
        ubtResultDetailBinding.resultRV.addItemDecorationWithoutLastItem()
        ubtResultDetailBinding.resultRV.scrollToPosition(position!!)
    }

    private fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager)
            return
        addItemDecoration(DividerItemDecorator(context))
    }

    override fun onSupportNavigateUp(): Boolean {
        uBTResultDetailAdapter!!.stopAudio()
        onBackPressed()
        return true
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_result_detail, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_all_results -> {
//                val intent = Intent(this, UBTResultActivity::class.java)
//                intent.putExtra(LKWBConstants.BLOG_DATA, beginTestDataResponse)
//                intent.putExtra(LKWBConstants.DATA_CORRECT_HASH, correctAnswerHashMap)
//                intent.putExtra(LKWBConstants.DATA_SELECTED_HASH, totalQuestionHashMap)
//                getResult.launch(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val value = it.data?.getIntExtra("position", 0)
                if (value != null) {
                    ubtResultDetailBinding.resultRV.scrollToPosition(value!!)
                }
            }
        }
    override val binding: ActivityUbtResultDetailBinding
        get() = ActivityUbtResultDetailBinding.inflate(layoutInflater)

}