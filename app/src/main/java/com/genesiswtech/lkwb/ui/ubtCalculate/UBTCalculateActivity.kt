package com.genesiswtech.lkwb.ui.ubtCalculate

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.databinding.ActivityUbtCalculateBinding
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestDataResponse
import com.genesiswtech.lkwb.ui.ubtCalculate.view.IUBTCalculateView
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse
import com.genesiswtech.lkwb.ui.ubtResult.UBTResultActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.math.BigDecimal


class UBTCalculateActivity : BaseActivity<ActivityUbtCalculateBinding>(), IUBTCalculateView {

    private lateinit var ubtCalculateBinding: ActivityUbtCalculateBinding
    private var beginTestDataResponse: BeginTestDataResponse? = null
    private var ubtQuestionDataList: ArrayList<UBTQuestionDataResponse>? = null
    private var title: String? = null
    private var totalNumber: Int? = null
    private var attempt: Int? = null
    private var unsolved: Int? = null
    private var time: Int? = null
    private var correct: Int? = null
    private var score: Double? = null
    private var id: Int? = null
    private var audio: String? = null

    private lateinit var mediaPlayer: MediaPlayer

    private var totalQuestionHashMap: HashMap<Int, Int> = HashMap()
    private var correctAnswerHashMap: HashMap<Int, Int> = HashMap()

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onSaveInstanceState(InstanceState: Bundle) {
        super.onSaveInstanceState(InstanceState)
        InstanceState.clear()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        audio = intent.getStringExtra(LKWBConstants.BLOG_RESULT)
        time = intent.getIntExtra(LKWBConstants.DATA_QUESTION_TIME, 0)
        totalNumber = intent.getIntExtra(LKWBConstants.DATA_QUESTION_TOTAL, 0)
        attempt = intent.getIntExtra(LKWBConstants.DATA_QUESTION_ATTEMPT, 0)
        unsolved = intent.getIntExtra(LKWBConstants.DATA_QUESTION_UNSOLVED, 0)
        correct = intent.getIntExtra(LKWBConstants.DATA_QUESTION_CORRECT, 0)
        score = intent.getDoubleExtra(LKWBConstants.DATA_QUESTION_SCORE, 0.0)

        totalQuestionHashMap =
            intent.getSerializableExtra(LKWBConstants.DATA_CORRECT_HASH) as HashMap<Int, Int>
        correctAnswerHashMap =
            intent.getSerializableExtra(LKWBConstants.DATA_SELECTED_HASH) as HashMap<Int, Int>
        beginTestDataResponse =
            intent.getSerializableExtra(LKWBConstants.BLOG_DATA) as? BeginTestDataResponse
//        ubtQuestionDataList =  intent.getSerializableExtra(LKWBConstants.BLOG_LIST_DATA) as? ArrayList<UBTQuestionDataResponse>
        AppUtils.actionBarWithTitle(this, title!!)
        ubtCalculateBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_ubt_calculate)
        ubtCalculateBinding.ubtCalculateHandler = this

        updateUBTList()
        setData()

//        val height = DisplayMetrics().heightPixels
        val width = DisplayMetrics().widthPixels
//        ubtCalculateBinding.attemptedRel.layoutParams =
//            LinearLayout.LayoutParams(width / 2, width / 2)
//        ubtCalculateBinding.attemptedRel.layoutParams.width = width/2
//        ubtCalculateBinding.unsolvedRel.layoutParams.width = width/2
//        ubtCalculateBinding.correctRel.layoutParams.width = width/2
//        ubtCalculateBinding.scoredRel.layoutParams.width = width/2
//        ubtCalculateBinding.attemptedRel.layoutParams.height = width/2
//        ubtCalculateBinding.unsolvedRel.layoutParams.height = width/2
//        ubtCalculateBinding.correctRel.layoutParams.height = width/2
//        ubtCalculateBinding.scoredRel.layoutParams.height = width/2

    }

    private fun updateProgressBar(number: Long, total: Long): Int {
        val pro: Long = ((number * 100) / total).toLong()
        return pro.toInt()
    }

    private fun setData() {
        mediaPlayer = MediaPlayer()
        Log.d("TAG", "Audio Url: " + audio)
        if (audio != null)
            playAudio(audio!!)
        ubtCalculateBinding.attemptedPB.progress =
            updateProgressBar(attempt!!.toLong(), totalNumber!!.toLong())
        ubtCalculateBinding.unsolvedPB.progress =
            updateProgressBar(unsolved!!.toLong(), totalNumber!!.toLong())
        ubtCalculateBinding.correctPB.progress =
            updateProgressBar(correct!!.toLong(), totalNumber!!.toLong())
        ubtCalculateBinding.scoredPB.progress =
            updateProgressBar(score!!.toLong(), (totalNumber!!.toLong() * 2.5).toLong())

        ubtCalculateBinding.timeSpentTV.text =
            getString(R.string.you_have_spent) + " " + time + " " + getString(R.string.minutes_)
        ubtCalculateBinding.attemptedTV.text = attempt.toString() + "/" + totalNumber
        ubtCalculateBinding.unsolvedTV.text = unsolved.toString() + "/" + totalNumber
        ubtCalculateBinding.correctTV.text = correct.toString() + "/" + totalNumber


        if (BigDecimal(score!!).scale() <= 0)
            ubtCalculateBinding.scoredTV.text = score!!.toInt().toString()
        else ubtCalculateBinding.scoredTV.text = score.toString()
        val rewardPoint: Double = (correct!!.toDouble() / 2).toDouble()
        ubtCalculateBinding.rewardPointTV.text =
            String.format(getString(R.string.congratulation_earned), rewardPoint)

    }

    override fun onSoundButtonClick(v: View?) {
        if (audio != null)
            playAudio(audio!!)
    }

    private fun playAudio(audioUrl: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "Audio Url Error: " + e.message)
        }
    }

    private fun releaseMediaPlayer() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("TAG", "Audio Url Error: " + e.message)
        }
    }

    override fun onCloseButtonClick(v: View?) {
        showDialog()
    }

    override fun onResultsButtonClick(v: View?) {
        releaseMediaPlayer()
        val intent = Intent(this, UBTResultActivity::class.java)
        intent.putExtra(LKWBConstants.BLOG_DATA, beginTestDataResponse)
        intent.putExtra(LKWBConstants.BLOG_LIST_DATA, ubtQuestionDataList)
        intent.putExtra(LKWBConstants.DATA_CORRECT_HASH, correctAnswerHashMap)
        intent.putExtra(LKWBConstants.DATA_SELECTED_HASH, totalQuestionHashMap)
        startActivity(intent)
    }

    private fun showDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.sure_close_result),
            getString(R.string.revert_back),
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
                releaseMediaPlayer()
                super.onBackPressed()
            },
            negativeButtonFunction = { }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        showDialog()
        return true
    }

    private fun updateUBTList() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateUBTListFromResult) }
    }

    override val binding: ActivityUbtCalculateBinding
        get() = ActivityUbtCalculateBinding.inflate(layoutInflater)
}