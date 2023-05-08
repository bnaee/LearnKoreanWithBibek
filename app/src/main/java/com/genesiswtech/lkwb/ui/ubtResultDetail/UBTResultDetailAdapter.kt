package com.genesiswtech.lkwb.ui.ubtResultDetail

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListUbtResultDetailBinding
import com.genesiswtech.lkwb.ui.ubtQuestion.model.Options
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class UBTResultDetailAdapter @Inject constructor(
    private val context: Context,
    private val ubtQuestionDataResponseList: ArrayList<UBTQuestionDataResponse>,
    private var totalQuestionHashMap: HashMap<Int, Int>,
    private var correctAnswerHashMap: HashMap<Int, Int>
) : RecyclerView.Adapter<UBTResultDetailAdapter.UBTResultDetailViewHolder>() {

    private var ubtResultDetailBinding: ListUbtResultDetailBinding? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onBindViewHolder(holder: UBTResultDetailViewHolder, position: Int) {
        val data = ubtQuestionDataResponseList[position]
        initViews(holder, data, position)
    }

    inner class UBTResultDetailViewHolder(val ubtResultDetailBinding: ListUbtResultDetailBinding) :
        RecyclerView.ViewHolder(ubtResultDetailBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UBTResultDetailViewHolder {
        ubtResultDetailBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_ubt_result_detail,
                parent,
                false
            )
        return UBTResultDetailViewHolder(ubtResultDetailBinding!!)
    }

    override fun getItemCount(): Int {
        return ubtQuestionDataResponseList.size
    }

    private fun initViews(
        holder: UBTResultDetailAdapter.UBTResultDetailViewHolder,
        data: UBTQuestionDataResponse,
        position: Int
    ) {
        releaseMediaPlayer()
        val pos = position + 1
        if (data.groupTitle !== null) holder.ubtResultDetailBinding.groupTitleTV.text =
            context.getString(R.string.ques) + " " + pos + ": " + Html.fromHtml(
                data.groupTitle, 0
            ).replaceFirst("\\[[^\\]]+\\]".toRegex(), "").trim()
        else holder.ubtResultDetailBinding.groupTitleTV.visibility = View.GONE
        if (data.mainTitle !== null) holder.ubtResultDetailBinding.questionTitleTV.text =
            Html.fromHtml(data.mainTitle, 0).trim()
        else holder.ubtResultDetailBinding.questionTitleTV.visibility = View.GONE
        if (data.subTitle !== null) {
//            val width = context.resources.displayMetrics.widthPixels
//            val height = context.resources.displayMetrics.heightPixels
//            holder.ubtResultDetailBinding.questionSubTV.layoutParams.height = height / 2
            holder.ubtResultDetailBinding.questionSubTV.settings.domStorageEnabled = true;
            holder.ubtResultDetailBinding.questionSubTV.settings.setAppCacheEnabled(true);
            holder.ubtResultDetailBinding.questionSubTV.settings.loadsImagesAutomatically = true;
            holder.ubtResultDetailBinding.questionSubTV.settings.mixedContentMode =
                WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
            holder.ubtResultDetailBinding.questionSubTV.loadDataWithBaseURL(
                null, data.subTitle!!.toString().trim(), "text/html", "UTF-8", null
            )
        } else {
            holder.ubtResultDetailBinding.questionSubTV.visibility = View.GONE
            holder.ubtResultDetailBinding.webLinear.visibility = View.GONE
        }
        desDisplay(holder, data)
        optionDisplay(holder, data)
        showCorrectDisplay(holder, data.answers!!.options)
        showWrongDisplay(holder, position, data.answers!!.options)
    }

    private fun desDisplay(
        holder: UBTResultDetailViewHolder, data: UBTQuestionDataResponse
    ) {
        val width: Int = context.resources.displayMetrics.widthPixels
        if (data.type == LKWBConstants.TEXT) {
            holder.ubtResultDetailBinding.firstCV.visibility = View.GONE
        } else if (data.type == LKWBConstants.IMAGE) {
            holder.ubtResultDetailBinding.firstCV.visibility = View.VISIBLE
            holder.ubtResultDetailBinding.soundIBtn.visibility = View.GONE
            holder.ubtResultDetailBinding.questionIV.visibility = View.VISIBLE
            Glide.with(context).load(data.file).override(width / 3, width / 3)
                .into(holder.ubtResultDetailBinding.questionIV)
            holder.ubtResultDetailBinding.questionIV.setOnClickListener { showFullImage(data.file.toString()) }
        } else if (data.type == LKWBConstants.AUDIO) {
            holder.ubtResultDetailBinding.firstCV.visibility = View.VISIBLE
            holder.ubtResultDetailBinding.soundIBtn.visibility = View.VISIBLE
            holder.ubtResultDetailBinding.questionIV.visibility = View.GONE
            holder.ubtResultDetailBinding.soundIBtn.setOnClickListener {
                playAudio(data.file.toString())
            }
        }
    }

    fun stopAudio() {
        releaseMediaPlayer()
    }

    private fun optionDisplay(
        holder: UBTResultDetailViewHolder, data: UBTQuestionDataResponse
    ) {
        if (data.answers!!.type == LKWBConstants.TEXT) {
            showImage(false, holder, data)
            showText(true, holder, data)
        }
        if (data.answers!!.type == LKWBConstants.IMAGE) {
            showImage(true, holder, data)
            showText(false, holder, data)
        }
        if (data.answers!!.type == LKWBConstants.AUDIO) {
            showText(false, holder, data)
            showAudio(true, holder, data)
        }

    }

    private fun showImage(
        status: Boolean, holder: UBTResultDetailViewHolder, data: UBTQuestionDataResponse
    ) {
        val width: Int = context.resources.displayMetrics.widthPixels
        holder.ubtResultDetailBinding.firstChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.secondChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.thirdChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.fourthChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE

        Glide.with(context).load(data.answers!!.options[0].option).override(width / 4, width / 4)
            .into(holder.ubtResultDetailBinding.firstChoiceIV)

        Glide.with(context).load(data.answers!!.options[1].option).override(width / 4, width / 4)
            .into(holder.ubtResultDetailBinding.secondChoiceIV)

        Glide.with(context).load(data.answers!!.options[2].option).override(width / 4, width / 4)
            .into(holder.ubtResultDetailBinding.thirdChoiceIV)

        Glide.with(context).load(data.answers!!.options[3].option).override(width / 4, width / 4)
            .into(holder.ubtResultDetailBinding.fourthChoiceIV)

        holder.ubtResultDetailBinding.firstChoiceIV.setOnClickListener {
            showFullImage(data.answers!!.options[0].option.toString())
        }
        holder.ubtResultDetailBinding.secondChoiceIV.setOnClickListener {
            showFullImage(data.answers!!.options[1].option.toString())
        }
        holder.ubtResultDetailBinding.thirdChoiceIV.setOnClickListener {
            showFullImage(data.answers!!.options[2].option.toString())
        }
        holder.ubtResultDetailBinding.fourthChoiceIV.setOnClickListener {
            showFullImage(data.answers!!.options[3].option.toString())
        }
    }


    private fun showAudio(
        status: Boolean, holder: UBTResultDetailViewHolder, data: UBTQuestionDataResponse
    ) {
        holder.ubtResultDetailBinding.firstChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.secondChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.thirdChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.fourthChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE

        holder.ubtResultDetailBinding.firstChoiceIV.setOnClickListener {
            playAudio(data.answers!!.options[0].option.toString())
//            holder.firstChoiceIV.isEnabled = false
//            holder.firstChoiceIV.isClickable = false
        }
        holder.ubtResultDetailBinding.secondChoiceIV.setOnClickListener {
            playAudio(data.answers!!.options[1].option.toString())
//            holder.secondChoiceIV.isEnabled = false
//            holder.secondChoiceIV.isClickable = false
        }
        holder.ubtResultDetailBinding.thirdChoiceIV.setOnClickListener {
            playAudio(data.answers!!.options[2].option.toString())
//            holder.thirdChoiceIV.isEnabled = false
//            holder.thirdChoiceIV.isClickable = false
        }
        holder.ubtResultDetailBinding.fourthChoiceIV.setOnClickListener {
            playAudio(data.answers!!.options[3].option.toString())
//            holder.fourthChoiceIV.isEnabled = false
//            holder.fourthChoiceIV.isClickable = false
        }
    }

    private fun playAudio(audioUrl: String) {
        Executors.newSingleThreadScheduledExecutor().schedule({
            releaseMediaPlayer()
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer!!.setDataSource(audioUrl)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 0, TimeUnit.SECONDS)
    }

    private fun releaseMediaPlayer() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            try {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.stop()
                }
                mediaPlayer!!.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 0, TimeUnit.SECONDS)
    }

    private fun showText(
        status: Boolean, holder: UBTResultDetailViewHolder, data: UBTQuestionDataResponse
    ) {
        holder.ubtResultDetailBinding.firstChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.secondChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.thirdChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE
        holder.ubtResultDetailBinding.fourthChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE

        holder.ubtResultDetailBinding.firstChoiceTV.text = data.answers!!.options[0].option
        holder.ubtResultDetailBinding.secondChoiceTV.text = data.answers!!.options[1].option
        holder.ubtResultDetailBinding.thirdChoiceTV.text = data.answers!!.options[2].option
        holder.ubtResultDetailBinding.fourthChoiceTV.text = data.answers!!.options[3].option
    }

    private fun showCorrectDisplay(
        holder: UBTResultDetailViewHolder, options: ArrayList<Options>
    ) {
        for (j in 0..3) {
            if (options[0].isCorrect == 1) {
                holder.ubtResultDetailBinding.firstChoiceCorrectRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.firstChoiceWrongRB.visibility = View.GONE
                holder.ubtResultDetailBinding.firstChoiceRB.visibility = View.GONE
                break
            } else if (options[1].isCorrect == 1) {
                holder.ubtResultDetailBinding.secondChoiceCorrectRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.secondChoiceWrongRB.visibility = View.GONE
                holder.ubtResultDetailBinding.secondChoiceRB.visibility = View.GONE
                break
            } else if (options[2].isCorrect == 1) {
                holder.ubtResultDetailBinding.thirdChoiceCorrectRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.thirdChoiceWrongRB.visibility = View.GONE
                holder.ubtResultDetailBinding.thirdChoiceRB.visibility = View.GONE
                break
            } else if (options[3].isCorrect == 1) {
                holder.ubtResultDetailBinding.fourthChoiceCorrectRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.fourthChoiceWrongRB.visibility = View.GONE
                holder.ubtResultDetailBinding.fourthChoiceRB.visibility = View.GONE
                break
            }
            break
        }
    }

    private fun showWrongDisplay(
        holder: UBTResultDetailViewHolder, position: Int, options: ArrayList<Options>
    ) {
//        for (key in totalQuestionHashMap.keys) {
        println("Element at selected key  $position : ${totalQuestionHashMap[position]}")
        println("Element at correct key $position : ${correctAnswerHashMap[position]}")
//        }

        if (totalQuestionHashMap[position] != correctAnswerHashMap[position]) {
            if (totalQuestionHashMap[position] == 0) {
                holder.ubtResultDetailBinding.firstChoiceWrongRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.firstChoiceCorrectRB.visibility = View.GONE
                holder.ubtResultDetailBinding.firstChoiceRB.visibility = View.GONE
            }
            if (totalQuestionHashMap[position] == 1) {
                holder.ubtResultDetailBinding.secondChoiceWrongRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.secondChoiceCorrectRB.visibility = View.GONE
                holder.ubtResultDetailBinding.secondChoiceRB.visibility = View.GONE
            }
            if (totalQuestionHashMap[position] == 2) {
                holder.ubtResultDetailBinding.thirdChoiceWrongRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.thirdChoiceCorrectRB.visibility = View.GONE
                holder.ubtResultDetailBinding.thirdChoiceRB.visibility = View.GONE
            }
            if (totalQuestionHashMap[position] == 3) {
                holder.ubtResultDetailBinding.fourthChoiceWrongRB.visibility = View.VISIBLE
                holder.ubtResultDetailBinding.fourthChoiceCorrectRB.visibility = View.GONE
                holder.ubtResultDetailBinding.fourthChoiceRB.visibility = View.GONE
            }
        }

    }

//    private fun hashMapCorrectData(
//        ubtQuestionDataList: ArrayList<UBTQuestionDataResponse>, total_number: Int
//    ) {
//        val num = total_number - 1
//        for (i in 0..num) {
//            val options = ubtQuestionDataList!![i].answers!!.options
//            for (j in 0..3) {
//                if (options[j].isCorrect == 1) {
//                    Log.d("TAG", "Correct Answer: " + i + " " + j + " " + options[j].isCorrect)
//                    correctAnswerHashMap[i] = j
//                    break
//                }
//            }
//        }
//    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun showFullImage(imageUrl: String) {
        AppUtils.showImageDialog(context, imageUrl)
    }
}