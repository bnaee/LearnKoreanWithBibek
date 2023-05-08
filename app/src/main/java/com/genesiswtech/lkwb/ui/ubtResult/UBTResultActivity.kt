package com.genesiswtech.lkwb.ui.ubtResult

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.TotalQuestion
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.databinding.ActivityUbtResultBinding
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestDataResponse
import com.genesiswtech.lkwb.ui.ubtResult.view.IUBTResultView
import com.genesiswtech.lkwb.ui.ubtResultDetail.UBTResultDetailActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class UBTResultActivity : BaseActivity<ActivityUbtResultBinding>(), IUBTResultView {
    private lateinit var ubtResultBinding: ActivityUbtResultBinding

    private lateinit var listeningQuestionList: ArrayList<TotalQuestion>
    private lateinit var readingQuestionList: ArrayList<TotalQuestion>
    private var readingOpened = true
    private var listeningOpened = true
    private var beginTestDataResponse: BeginTestDataResponse? = null


    private var totalQuestionHashMap: HashMap<Int, Int> = HashMap()
    private var correctAnswerHashMap: HashMap<Int, Int> = HashMap()

    override fun onSaveInstanceState(InstanceState: Bundle) {
        super.onSaveInstanceState(InstanceState)
        InstanceState.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.actionbar(this)
        setTitle(R.string.result)
        ubtResultBinding = DataBindingUtil.setContentView(this, R.layout.activity_ubt_result)
        ubtResultBinding.ubtResultHandler = this

        totalQuestionHashMap =
            intent.getSerializableExtra(LKWBConstants.DATA_CORRECT_HASH) as HashMap<Int, Int>
        correctAnswerHashMap =
            intent.getSerializableExtra(LKWBConstants.DATA_SELECTED_HASH) as HashMap<Int, Int>
        beginTestDataResponse =
            intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? BeginTestDataResponse

        ubtResultBinding.readingTV.text =
            getString(R.string.listening_result) + " " + "(" + beginTestDataResponse!!.listening + " " + getString(
                R.string.question
            ) + ")"
        ubtResultBinding.listeningTV.text =
            getString(R.string.reading_result) + " " + "(" + beginTestDataResponse!!.reading + " " + getString(
                R.string.question
            ) + ")"

        val listeningLayoutManager = GridLayoutManager(this, 5)
        val readingLayoutManager = GridLayoutManager(this, 5)
        ubtResultBinding.listeningRV.layoutManager = listeningLayoutManager
        ubtResultBinding.readingRV.layoutManager = readingLayoutManager

        // ArrayList of class ItemsViewModel
        listeningQuestionList = ArrayList()
        readingQuestionList = ArrayList()

        val total: Int = beginTestDataResponse!!.listening!! + beginTestDataResponse!!.reading!!
        for (key in totalQuestionHashMap.keys) {
            println("Correct Result at key $key : ${totalQuestionHashMap[key]}" + " " + correctAnswerHashMap[key])
            if (key < beginTestDataResponse!!.listening!!) {
                if (correctAnswerHashMap[key] == totalQuestionHashMap[key])
                    listeningQuestionList.add(TotalQuestion(key, LKWBConstants.CORRECT))
                else if (totalQuestionHashMap[key] == 5)
                    listeningQuestionList.add(TotalQuestion(key, LKWBConstants.UNSOLVED))
                else
                    listeningQuestionList.add(TotalQuestion(key, LKWBConstants.WRONG))
            } else if (key < total) {
                if (correctAnswerHashMap[key] == totalQuestionHashMap[key])
                    readingQuestionList.add(TotalQuestion(key, LKWBConstants.CORRECT))
                else if (totalQuestionHashMap[key] == 5)
                    readingQuestionList.add(TotalQuestion(key, LKWBConstants.UNSOLVED))
                else
                    readingQuestionList.add(TotalQuestion(key, LKWBConstants.WRONG))
            }
        }
        val listeningAdapter = UBTResultAdapter(this,0,listeningQuestionList)
        val readingAdapter = UBTResultAdapter(this,beginTestDataResponse!!.listening!!,readingQuestionList)

//        // Setting the Adapter with the recyclerview
        ubtResultBinding.listeningRV.adapter = listeningAdapter
        ubtResultBinding.readingRV.adapter = readingAdapter


//        listeningAdapter.onItemClick = {
//            Log.d("TAG", it.toString())
//            val intent = Intent()
//            intent.putExtra("position", it - 1)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
//
//        readingAdapter.onItemClick = {
//            Log.d("TAG", it.toString())
//            val intent = Intent()
//            intent.putExtra("position", it + beginTestDataResponse!!.reading!! - 1)
//            setResult(RESULT_OK, intent)
//            finish()
//        }

        listeningAdapter.onItemClick = {
            val intent = Intent(this, UBTResultDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_POSITION, it - 1)
            intent.putExtra(LKWBConstants.DATA_CORRECT_HASH, correctAnswerHashMap)
            intent.putExtra(LKWBConstants.DATA_SELECTED_HASH, totalQuestionHashMap)
            startActivity(intent)
        }


        readingAdapter.onItemClick = {
            val intent = Intent(this, UBTResultDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_POSITION, it + beginTestDataResponse!!.reading!! - 1)
            intent.putExtra(LKWBConstants.DATA_CORRECT_HASH, correctAnswerHashMap)
            intent.putExtra(LKWBConstants.DATA_SELECTED_HASH, totalQuestionHashMap)
            startActivity(intent)
        }


//        ubtResultBinding.listeningExpandIBtn.setOnClickListener {
//            if (!listeningOpened) {
//                val animSlideDown=
//                    AnimationUtils.loadAnimation(this, R.anim.slide_down)
//                ubtResultBinding.listeningRV.startAnimation(animSlideDown)
////                AnimationUtils.slideUp(ubtResultBinding.listeningRV)
////                ubtResultBinding.listeningRV.visibility = View.VISIBLE
////                ubtResultBinding.listeningExpandIBtn.setImageResource(R.drawable.ic_arrow_up)
//
//            } else {
//                AnimationUtils.slideDown(ubtResultBinding.listeningRV)
//                ubtResultBinding.listeningRV.visibility = View.GONE
//                ubtResultBinding.listeningExpandIBtn.setImageResource(R.drawable.ic_arrow_down)
//            }
//            listeningOpened = !listeningOpened
//        }

//        ubtResultBinding.readingExpandIBtn.setOnClickListener {
//            if (!readingOpened) {
//                AnimationUtils.slideUp(ubtResultBinding.readingRV)
//                ubtResultBinding.readingRV.visibility = View.VISIBLE
//                ubtResultBinding.readingExpandIBtn.setImageResource(R.drawable.ic_arrow_up)
//            } else {
//                AnimationUtils.slideDown(ubtResultBinding.readingRV)
//                ubtResultBinding.readingRV.visibility = View.GONE
//                ubtResultBinding.readingExpandIBtn.setImageResource(R.drawable.ic_arrow_down)
//            }
//            readingOpened = !readingOpened
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_result_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_all_results -> {
                val intent = Intent(this, UBTResultDetailActivity::class.java)
                intent.putExtra(LKWBConstants.BLOG_DATA, beginTestDataResponse)
                intent.putExtra(LKWBConstants.DATA_CORRECT_HASH, correctAnswerHashMap)
                intent.putExtra(LKWBConstants.DATA_SELECTED_HASH, totalQuestionHashMap)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override val binding: ActivityUbtResultBinding
        get() = ActivityUbtResultBinding.inflate(layoutInflater)


}

