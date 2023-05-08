package com.genesiswtech.lkwb.ui.ubtQuestion

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.databinding.ActivityUbtQuestionBinding
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestDataResponse
import com.genesiswtech.lkwb.ui.ubtCalculate.UBTCalculateActivity
import com.genesiswtech.lkwb.ui.ubtQuestion.model.StoreResultDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.presenter.UBTQuestionPresenter
import com.genesiswtech.lkwb.ui.ubtQuestion.view.Callback
import com.genesiswtech.lkwb.ui.ubtQuestion.view.IQuestionDataPass
import com.genesiswtech.lkwb.ui.ubtQuestion.view.IUBTQuestionView
import com.genesiswtech.lkwb.ui.ubtTotalQuestion.UBTTotalQuestionActivity
import com.genesiswtech.lkwb.utils.*
import com.genesiswtech.lkwb.utils.AppUtils.actionBarWithTitleElevation
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


class UBTQuestionActivity : BaseActivity<ActivityUbtQuestionBinding>(), IUBTQuestionView,
    IQuestionDataPass {

    private lateinit var ubtQuestionBinding: ActivityUbtQuestionBinding
    private var beginTestDataResponse: BeginTestDataResponse? = null
    private var ubtQuestionPresenter: UBTQuestionPresenter? = null
    private var totalNumber: Int? = null
    private var ubtQuestionFragmentAdapter: UBTQuestionFragmentAdapter? = null
    private var ubtQuestionDataList: ArrayList<UBTQuestionDataResponse>? = null

    private var totalQuestionHashMap: HashMap<Int, Int> = HashMap()
    private var correctAnswerHashMap: HashMap<Int, Int> = HashMap()

    private var min: Int? = 0
    private var mediaPlayer: MediaPlayer? = null
    private var packageId: String? = null
    private var examCancel: Boolean = false
    var loadText: String? = null

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onSaveInstanceState(InstanceState: Bundle) {
        super.onSaveInstanceState(InstanceState)
        InstanceState.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadText = resources.getString(R.string.please_wait)
        packageId = intent.getStringExtra(LKWBConstants.BLOG_PACKAGE)
        beginTestDataResponse =
            intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? BeginTestDataResponse

        if (AppUtils.isDeviceMobileLandScape(this)) {
            actionBarWithTitleElevation(this, beginTestDataResponse!!.title!!)
            AppUtils.centerTitle(this)
        } else
            AppUtils.actionBarWithTitle(this, beginTestDataResponse!!.title!!)
        ubtQuestionBinding = DataBindingUtil.setContentView(this, R.layout.activity_ubt_question)
        ubtQuestionBinding.ubtQuestionHandler = this
        initDependencies()
        totalNumber = beginTestDataResponse!!.reading!! + beginTestDataResponse!!.listening!!
        hashMapData(totalNumber!!)
        questionCount(1, totalNumber!!)
        val id: Int = beginTestDataResponse!!.id!!
        ubtQuestionPresenter!!.getUBTQuestion(this, id)
        val time: Long = (beginTestDataResponse!!.time!!.toLong()) * 1000 * 60
        ubtTimer(time)


    }


    private fun ubtTimer(time: Long) {
        object : CountDownTimer(time, 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
//                 val hours = millisUntilFinished / (1000 * 60 * 60) % 24
                val minutes = millisUntilFinished / (1000 * 60) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                if (minutes.toString().length == 1 && seconds.toString().length == 2) {
                    ubtQuestionBinding.timerTV.text = " 0$minutes : $seconds"
                } else if (minutes.toString().length == 2 && seconds.toString().length == 1) {
                    ubtQuestionBinding.timerTV.text = " $minutes : 0$seconds"
                } else if (minutes.toString().length == 1 && seconds.toString().length == 1) {
                    ubtQuestionBinding.timerTV.text = " 0$minutes : 0$seconds"
                } else {
                    ubtQuestionBinding.timerTV.text = " $minutes : $seconds"
                }

                val time: Int? = beginTestDataResponse!!.time!!.toIntOrNull()
                min = time?.minus(minutes.toInt())
            }

            override fun onFinish() {
                storeResult()
            }
        }.start()
    }

    private fun hashMapData(total_number: Int) {
        for (i in 0..total_number) {
            totalQuestionHashMap[i] = 5
        }
    }

    private fun setViewPager(
        total_number: Int,
        ubtQuestionDataResponseList: ArrayList<UBTQuestionDataResponse>
    ) {

        ubtQuestionFragmentAdapter = UBTQuestionFragmentAdapter(
            supportFragmentManager,
            total_number, ubtQuestionDataResponseList
        )
        ubtQuestionBinding.viewPagerQuestion.adapter = ubtQuestionFragmentAdapter
        ubtQuestionBinding.viewPagerQuestion.offscreenPageLimit = total_number
        ubtQuestionBinding.viewPagerQuestion.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            private var currentPage = 0
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

                questionCount(ubtQuestionBinding.viewPagerQuestion.currentItem + 1, totalNumber!!)
                buttonLayoutChange()
                val fragment: Fragment = ubtQuestionFragmentAdapter!!.getItem(currentPage)

                if (fragment is Callback &&
                    currentPage !== position
                ) {
                    (ubtQuestionFragmentAdapter!!.getItem(currentPage) as Callback).onPageChanged()
                }
                currentPage = position
                releaseMediaPlayer()
            }

        })
    }


    override fun initDependencies() {
        ubtQuestionPresenter = UBTQuestionPresenter(this, application)
    }

    override fun onPreviousButtonClick(v: View?) {
        buttonLayoutChange()
        questionCount(ubtQuestionBinding.viewPagerQuestion.currentItem - 1, totalNumber!!)
        ubtQuestionBinding.viewPagerQuestion.setCurrentItem(getNextPossibleItemIndex(-1), true)
    }

    override fun onNextButtonClick(v: View?) {
        buttonLayoutChange()
        if (ubtQuestionBinding.viewPagerQuestion.currentItem == (totalNumber!! - 1)) {
//            storeResult()
        } else {
            questionCount(ubtQuestionBinding.viewPagerQuestion.currentItem + 1, totalNumber!!)
            ubtQuestionBinding.viewPagerQuestion.setCurrentItem(getNextPossibleItemIndex(1), true)
        }
    }

    private fun buttonLayoutChange() {
        if (ubtQuestionBinding.viewPagerQuestion.currentItem == 0) {
//            (ubtQuestionBinding.previousBtn.layoutParams as LinearLayout.LayoutParams).weight = 0f
//            (ubtQuestionBinding.nextBtn.layoutParams as LinearLayout.LayoutParams).weight = 2f
            ubtQuestionBinding.previousBtn.visibility = View.GONE
//            ubtQuestionBinding.view.visibility = View.GONE
        } else if (ubtQuestionBinding.viewPagerQuestion.currentItem == (totalNumber!! - 1)) {
//            (ubtQuestionBinding.previousBtn.layoutParams as LinearLayout.LayoutParams).weight = 2f
//            (ubtQuestionBinding.nextBtn.layoutParams as LinearLayout.LayoutParams).weight = 0f
            ubtQuestionBinding.nextBtn.visibility = View.GONE
//            ubtQuestionBinding.view.visibility = View.GONE
        } else {
//            (ubtQuestionBinding.previousBtn.layoutParams as LinearLayout.LayoutParams).weight = 1f
//            (ubtQuestionBinding.nextBtn.layoutParams as LinearLayout.LayoutParams).weight = 1f
            ubtQuestionBinding.nextBtn.visibility = View.VISIBLE
            ubtQuestionBinding.previousBtn.visibility = View.VISIBLE
//            ubtQuestionBinding.view.visibility = View.VISIBLE
        }
    }

    private fun questionAttemptCount(): Int {
        var count = 0
        for (key in totalQuestionHashMap.keys) {
//            println("Element at key $key : ${totalQuestionHashMap[key]}")
            if (totalQuestionHashMap[key] != 5)
                count++
        }
        return count
    }

    private fun questionUnsolvedCount(): Int {
        var count = 0
        for (key in totalQuestionHashMap.keys) {
            if (totalQuestionHashMap[key] == 5)
                count++
        }
        return count - 1
    }

    private fun questionTimeCount(): Int {
        return min!!
    }

    private fun questionCorrectCount(): Int {
        var count = 0
        for (key in totalQuestionHashMap.keys) {
            if (correctAnswerHashMap[key] == totalQuestionHashMap[key])
                count++
        }
        return count
    }

    private fun questionScoreCount(): Double {
        return questionCorrectCount() * 2.5
    }

    private fun getNextPossibleItemIndex(change: Int): Int {
        val currentIndex: Int = ubtQuestionBinding.viewPagerQuestion.currentItem
        val total: Int = ubtQuestionBinding.viewPagerQuestion.adapter!!.count
        return if (currentIndex + change < 0) {
            0
        } else ((currentIndex + change) % total)
    }

    private fun questionCount(current_page: Int, total_page: Int) {
        ubtQuestionBinding.viewPagerQuestion.currentItem
        if (current_page > 0) {
            ubtQuestionBinding.questionCountTV1!!.text =
                String.format(getString(R.string.question_of), current_page, total_page)
            ubtQuestionBinding.questionCountTV.text =
                getString(R.string.question_no) + " " + current_page + "/" + total_page
        }
    }

    private fun navigateToResult(storeResultDataResponse: StoreResultDataResponse) {
        val intent = Intent(this, UBTCalculateActivity::class.java)
        intent.putExtra(LKWBConstants.BLOG_DATA, beginTestDataResponse)
        intent.putExtra(LKWBConstants.DATA_ID, beginTestDataResponse!!.id)
        intent.putExtra(LKWBConstants.DATA_TITLE, beginTestDataResponse!!.title)
        intent.putExtra(LKWBConstants.DATA_QUESTION_TOTAL, totalNumber)
        intent.putExtra(LKWBConstants.DATA_QUESTION_ATTEMPT, questionAttemptCount())
        intent.putExtra(LKWBConstants.DATA_QUESTION_UNSOLVED, questionUnsolvedCount())
        intent.putExtra(LKWBConstants.DATA_QUESTION_TIME, questionTimeCount())
        intent.putExtra(LKWBConstants.DATA_QUESTION_CORRECT, questionCorrectCount())
        intent.putExtra(LKWBConstants.DATA_QUESTION_SCORE, questionScoreCount())
        intent.putExtra(LKWBConstants.DATA_CORRECT_HASH, correctAnswerHashMap)
        intent.putExtra(LKWBConstants.DATA_SELECTED_HASH, totalQuestionHashMap)
        intent.putExtra(LKWBConstants.DATA_RESULT, storeResultDataResponse)
        intent.putExtra(LKWBConstants.BLOG_RESULT, storeResultDataResponse.audio)
        startActivity(intent)
        onShowProgressBar(false)
        finish()
    }

    override fun onAllQuestionButtonClick(v: View?) {
        navigateToAllQuestion()
    }

    override fun onSubmitButtonClick(v: View?) {
        AppUtils.showDialog(this,
            title = getString(R.string.want_to_submit),
            getString(R.string.revert_back),
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
//                navigateToResultTest()//This is for test only
                storeResult()//This is main call
            },
            negativeButtonFunction = { }
        )
    }

    private fun storeResult() {
        if (packageId == null)
            packageId = ""
        ubtQuestionPresenter!!.postStoreResult(
            this,
            questionAttemptCount().toString(),
            questionUnsolvedCount().toString(),
            questionTimeCount().toString(),
            questionCorrectCount().toString(),
            questionScoreCount().toString(),
            beginTestDataResponse!!.id.toString(),
            packageId!!
        )
    }

    override fun onStoreResultSuccess(storeResultDataResponse: StoreResultDataResponse) {
        if (examCancel) {
            updateUBTList()
            releaseMediaPlayer()
            super.onBackPressed()
        } else
            navigateToResult(storeResultDataResponse)
    }

    override fun onSuccess(ubtQuestionResponse: UBTQuestionResponse) {
        LKWBPreferencesManager.put(ubtQuestionResponse, "KEY_SET")
        setViewPager(ubtQuestionResponse.data.size, ubtQuestionResponse.data)
        ubtQuestionDataList = ubtQuestionResponse.data
        ubtQuestionBinding.ubtQuestionPage.visibility = View.VISIBLE
        hashMapCorrectData(ubtQuestionResponse.data, totalNumber!!)
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
        ubtQuestionBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val value = it.data?.getIntExtra("position", 0)
//                Log.d("TAG", "Selected Position: " + value)
                if (value != null) {
                    ubtQuestionBinding.viewPagerQuestion.setCurrentItem(value - 1, true)
                }
            }
        }

    override fun onQuestionDataPass(position: Int, answer: Int) {
        totalQuestionHashMap[position] = answer
    }

    private fun hashMapCorrectData(
        ubtQuestionDataList: ArrayList<UBTQuestionDataResponse>,
        total_number: Int
    ) {
        val num = total_number - 1
        for (i in 0..num) {
            val options = ubtQuestionDataList[i].answers!!.options
            for (j in 0..3) {
                if (options[j].isCorrect == 1) {
//                    Log.d("TAG", "Correct Answer: " + i + " " + j + " " + options[j].isCorrect)
                    correctAnswerHashMap[i] = j
                    break
                }
            }
        }
    }

    private fun showBackDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.really_want_go_back),
            getString(R.string.you_revert),
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
                examCancel = true
                storeResult()
            },
            negativeButtonFunction = { }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        showBackDialog()
        return true
    }

    override fun onBackPressed() {
        showBackDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!AppUtils.isDeviceMobileLandScape(this))
            menuInflater.inflate(R.menu.menu_ubt_question, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_all_question -> {
                navigateToAllQuestion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToAllQuestion() {
        if (beginTestDataResponse != null) {
            val intent = Intent(this, UBTTotalQuestionActivity::class.java)
            intent.putExtra(LKWBConstants.BLOG_DATA_READ, beginTestDataResponse!!.reading)
            intent.putExtra(
                LKWBConstants.BLOG_DATA_Listen,
                beginTestDataResponse!!.listening
            )
            intent.putExtra(LKWBConstants.BLOG_DATA, totalQuestionHashMap)
            getResult.launch(intent)
        }
    }

    fun playAudio(audioUrl: String) {
        Executors.newSingleThreadScheduledExecutor().schedule({
            releaseMediaPlayer()
            mediaPlayer = MediaPlayer()
//            mediaPlayer!!.setAudioAttributes(AudioAttributes.)
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            Log.d("TAG", "Audio Play: " + audioUrl)
            try {
                mediaPlayer!!.setDataSource(audioUrl)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            } catch (e: Exception) {
                Log.d("TAG", "Audio Play Error: " + e.message)
                e.printStackTrace()
                if (e.message == null)
                    playAudio(audioUrl)
            }
        }, 0, TimeUnit.SECONDS)

    }

    fun releaseMediaPlayer() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            try {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.pause()
                    mediaPlayer!!.stop()
                }
                mediaPlayer!!.release()
//                mediaPlayer!!.reset()
//                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }, 0, TimeUnit.SECONDS)
    }

    override val binding: ActivityUbtQuestionBinding
        get() = ActivityUbtQuestionBinding.inflate(layoutInflater)


    private fun updateUBTList() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateUBTListFromResult) }
    }

}