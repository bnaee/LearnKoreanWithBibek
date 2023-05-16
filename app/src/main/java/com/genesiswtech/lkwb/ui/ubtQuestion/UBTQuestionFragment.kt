package com.genesiswtech.lkwb.ui.ubtQuestion

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentUbtQuestionBinding
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.view.Callback
import com.genesiswtech.lkwb.ui.ubtQuestion.view.IQuestionDataPass
import com.genesiswtech.lkwb.ui.ubtQuestion.view.IQuestionView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.AppUtils.isDeviceMobileLandScape
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.google.android.material.chip.Chip


class UBTQuestionFragment : Fragment(R.layout.fragment_ubt_question), IQuestionView, Callback {

    private var list: ArrayList<UBTQuestionDataResponse> = ArrayList()
    private lateinit var fragmentUbtQuestionBinding: FragmentUbtQuestionBinding
    private var position: Int = 0

    private lateinit var questionDataPasser: IQuestionDataPass

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentUbtQuestionBinding.bind(itemView)
        fragmentUbtQuestionBinding = binding
        binding.ubtQuestionHandler = this
        getBundle()
        initViews()
    }

    private fun getBundle() {
        position = requireArguments().getInt(LKWBConstants.DATA_POSITION)
        list =
            requireArguments().getSerializable(LKWBConstants.DATA_VALUE) as ArrayList<UBTQuestionDataResponse>
    }

    private fun initViews() {
        if (!list[position].groupTitle.isNullOrEmpty())
            fragmentUbtQuestionBinding.groupTitleTV.text =
                Html.fromHtml(list[position].groupTitle, 0)
        else
            fragmentUbtQuestionBinding.groupTitleTV.visibility = View.GONE
        if (!list[position].mainTitle.isNullOrEmpty())
            fragmentUbtQuestionBinding.questionTitleTV.text =
                Html.fromHtml(list[position].mainTitle!!.trim(), 0).trim()
        else
            fragmentUbtQuestionBinding.questionTitleTV.visibility = View.GONE
        if (!list[position].subTitle.isNullOrEmpty()) {
            if (list[position].type == LKWBConstants.AUDIO) {
                fragmentUbtQuestionBinding.questionSoundSubTV!!.settings.domStorageEnabled = true
                fragmentUbtQuestionBinding.questionSoundSubTV!!.settings.setAppCacheEnabled(true)
                fragmentUbtQuestionBinding.questionSoundSubTV!!.settings.loadsImagesAutomatically =
                    true
                fragmentUbtQuestionBinding.questionSoundSubTV!!.settings.mixedContentMode =
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                fragmentUbtQuestionBinding.questionSoundSubTV!!.loadDataWithBaseURL(
                    null,
                    list[position].subTitle!!.toString().trim(),
                    "text/html",
                    "UTF-8",
                    null
                )
                fragmentUbtQuestionBinding.questionSubTV.visibility = View.GONE
                fragmentUbtQuestionBinding.webLinear.visibility = View.GONE
                fragmentUbtQuestionBinding.questionSoundSubTV!!.visibility = View.VISIBLE
                fragmentUbtQuestionBinding.webSoundLinear!!.visibility = View.VISIBLE
            } else {
                fragmentUbtQuestionBinding.questionSubTV.settings.domStorageEnabled = true
                fragmentUbtQuestionBinding.questionSubTV.settings.setAppCacheEnabled(true)
                fragmentUbtQuestionBinding.questionSubTV.settings.loadsImagesAutomatically = true
                fragmentUbtQuestionBinding.questionSubTV.settings.mixedContentMode =
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                fragmentUbtQuestionBinding.questionSubTV.loadDataWithBaseURL(
                    null,
                    list[position].subTitle!!.toString().trim(),
                    "text/html",
                    "UTF-8",
                    null
                )
                fragmentUbtQuestionBinding.questionSubTV.visibility = View.VISIBLE
                fragmentUbtQuestionBinding.webLinear.visibility = View.VISIBLE
                fragmentUbtQuestionBinding.questionSoundSubTV!!.visibility = View.GONE
                fragmentUbtQuestionBinding.webSoundLinear!!.visibility = View.GONE
            }

        } else {
            fragmentUbtQuestionBinding.questionSubTV.visibility = View.GONE
            fragmentUbtQuestionBinding.webLinear.visibility = View.GONE
            fragmentUbtQuestionBinding.questionSoundSubTV!!.visibility = View.GONE
            fragmentUbtQuestionBinding.webSoundLinear!!.visibility = View.GONE
        }
        desDisplay()
        experiment()
        optionDisplay(
            firstChoiceIV,
            secondChoiceIV,
            thirdChoiceIV,
            fourthChoiceIV,
            firstChoiceTV,
            secondChoiceTV,
            thirdChoiceTV,
            fourthChoiceTV
        )
        val pos = position + 1
        fragmentUbtQuestionBinding.numberTV.text = "$pos. "

    }


    lateinit var linear: LinearLayout

    lateinit var firstChoice: LinearLayout
    lateinit var firstChoiceRB: AppCompatRadioButton
    lateinit var firstChoiceIV: AppCompatImageView
    lateinit var firstChoiceTV: AppCompatTextView

    lateinit var secondChoice: LinearLayout
    lateinit var secondChoiceRB: AppCompatRadioButton
    lateinit var secondChoiceIV: AppCompatImageView
    lateinit var secondChoiceTV: AppCompatTextView

    lateinit var thirdChoice: LinearLayout
    lateinit var thirdChoiceRB: AppCompatRadioButton
    lateinit var thirdChoiceIV: AppCompatImageView
    lateinit var thirdChoiceTV: AppCompatTextView

    lateinit var fourthChoice: LinearLayout
    lateinit var fourthChoiceRB: AppCompatRadioButton
    lateinit var fourthChoiceIV: AppCompatImageView
    lateinit var fourthChoiceTV: AppCompatTextView

    private fun experiment() {
        if (AppUtils.isDeviceMobileLandScape(requireActivity() as AppCompatActivity)) {
            when (list[position].answers!!.type) {
                LKWBConstants.TEXT -> {
                    linear = layoutInflater.inflate(
                        R.layout.layout_radiogroup_text,
                        fragmentUbtQuestionBinding.linear,
                        false
                    ) as LinearLayout
                }
                else -> {
                    linear = layoutInflater.inflate(
                        R.layout.layout_rg_image,
                        fragmentUbtQuestionBinding.linear,
                        false
                    ) as LinearLayout
                }
            }
        } else {
            linear = layoutInflater.inflate(
                R.layout.layout_radiogroup_text,
                fragmentUbtQuestionBinding.linear,
                false
            ) as LinearLayout
        }


        firstChoice = linear.findViewById(R.id.firstChoice)
        firstChoiceIV = linear.findViewById(R.id.firstChoiceIV)
        firstChoiceTV = linear.findViewById(R.id.firstChoiceTV)
        firstChoiceRB = linear.findViewById(R.id.firstChoiceRB)
        firstChoice.setOnClickListener {
            firstChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        firstChoiceRB.setOnClickListener {
            firstChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        secondChoice = linear.findViewById(R.id.secondChoice)
        secondChoiceIV = linear.findViewById(R.id.secondChoiceIV)
        secondChoiceTV = linear.findViewById(R.id.secondChoiceTV)
        secondChoiceRB = linear.findViewById(R.id.secondChoiceRB)
        secondChoice.setOnClickListener {
            secondChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        secondChoiceRB.setOnClickListener {
            secondChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        thirdChoice = linear.findViewById(R.id.thirdChoice)
        thirdChoiceIV = linear.findViewById(R.id.thirdChoiceIV)
        thirdChoiceTV = linear.findViewById(R.id.thirdChoiceTV)
        thirdChoiceRB = linear.findViewById(R.id.thirdChoiceRB)
        thirdChoice.setOnClickListener {
            thirdChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        thirdChoiceRB.setOnClickListener {
            thirdChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        fourthChoice = linear.findViewById(R.id.fourthChoice)
        fourthChoiceIV = linear.findViewById(R.id.fourthChoiceIV)
        fourthChoiceTV = linear.findViewById(R.id.fourthChoiceTV)
        fourthChoiceRB = linear.findViewById(R.id.fourthChoiceRB)
        fourthChoice.setOnClickListener {
            fourthChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }

        fourthChoiceRB.setOnClickListener {
            fourthChoiceClick(
                firstChoiceRB,
                secondChoiceRB,
                thirdChoiceRB,
                fourthChoiceRB
            )
        }


        firstChoiceIV.setOnClickListener { firstChoiceSoundImage(firstChoiceIV) }
        secondChoiceIV.setOnClickListener { secondChoiceSoundImage(secondChoiceIV) }
        thirdChoiceIV.setOnClickListener { thirdChoiceSoundImage(thirdChoiceIV) }
        fourthChoiceIV.setOnClickListener { fourthChoiceSoundImage(fourthChoiceIV) }

        fragmentUbtQuestionBinding.linear.addView(linear as View)
    }


    private fun desDisplay() {
        val width: Int = this.resources.displayMetrics.widthPixels
        val height: Int = this.resources.displayMetrics.heightPixels
        when (list[position].type) {
            LKWBConstants.TEXT -> {
                fragmentUbtQuestionBinding.firstCV.visibility = View.GONE
            }
            LKWBConstants.IMAGE -> {
                fragmentUbtQuestionBinding.firstCV.visibility = View.VISIBLE
                fragmentUbtQuestionBinding.soundLL.visibility = View.GONE
                fragmentUbtQuestionBinding.questionIV.visibility = View.VISIBLE

                var imageWidth: Int = width / 3
                if (AppUtils.isDeviceMobileLandScape(requireActivity() as AppCompatActivity))
                    imageWidth = height / 6
                else
                    imageWidth = width / 3

                Glide.with(this)
                    .load(list[position].file)
                    .override(imageWidth, imageWidth)
                    .into(fragmentUbtQuestionBinding.questionIV)
            }
            LKWBConstants.AUDIO -> {
                fragmentUbtQuestionBinding.firstCV.visibility = View.VISIBLE
                fragmentUbtQuestionBinding.soundLL.visibility = View.VISIBLE
                fragmentUbtQuestionBinding.questionIV.visibility = View.GONE
            }
        }
    }


    private fun optionDisplay(
        firstChoiceIV: AppCompatImageView,
        secondChoiceIV: AppCompatImageView,
        thirdChoiceIV: AppCompatImageView,
        fourthChoiceIV: AppCompatImageView,
        firstChoiceTV: AppCompatTextView,
        secondChoiceTV: AppCompatTextView,
        thirdChoiceTV: AppCompatTextView,
        fourthChoiceTV: AppCompatTextView
    ) {
        if (list[position].answers!!.type == LKWBConstants.TEXT) {
            showImage(
                firstChoiceIV,
                secondChoiceIV,
                thirdChoiceIV,
                fourthChoiceIV, false
            )
            showText(
                firstChoiceTV,
                secondChoiceTV,
                thirdChoiceTV,
                fourthChoiceTV, true
            )
        }
        if (list[position].answers!!.type == LKWBConstants.IMAGE) {
            showImage(
                firstChoiceIV,
                secondChoiceIV,
                thirdChoiceIV,
                fourthChoiceIV, true
            )
            showText(
                firstChoiceTV,
                secondChoiceTV,
                thirdChoiceTV,
                fourthChoiceTV, false
            )
        }
    }

    private fun showImage(
        firstChoiceIV: AppCompatImageView,
        secondChoiceIV: AppCompatImageView,
        thirdChoiceIV: AppCompatImageView,
        fourthChoiceIV: AppCompatImageView,
        status: Boolean
    ) {
        val width: Int = this.resources.displayMetrics.widthPixels
        val height: Int = this.resources.displayMetrics.heightPixels
        firstChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        secondChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        thirdChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE
        fourthChoiceIV.visibility =
            if (status) View.VISIBLE else View.GONE

        var imageWidth: Int = width / 4
        if (AppUtils.isDeviceMobileLandScape(requireActivity() as AppCompatActivity))
            imageWidth = height / 5
        else
            imageWidth = width / 4


        Glide.with(this)
            .load(list[position].answers!!.options[0].option)
            .override(imageWidth, imageWidth)
            .into(firstChoiceIV)

        Glide.with(this)
            .load(list[position].answers!!.options[1].option)
            .override(imageWidth, imageWidth)
            .into(secondChoiceIV)

        Glide.with(this)
            .load(list[position].answers!!.options[2].option)
            .override(imageWidth, imageWidth)
            .into(thirdChoiceIV)

        Glide.with(this)
            .load(list[position].answers!!.options[3].option)
            .override(imageWidth, imageWidth)
            .into(fourthChoiceIV)
    }

    private fun showText(
        firstChoiceTV: AppCompatTextView,
        secondChoiceTV: AppCompatTextView,
        thirdChoiceTV: AppCompatTextView,
        fourthChoiceTV: AppCompatTextView, status: Boolean
    ) {
        firstChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE
        secondChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE
        thirdChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE
        fourthChoiceTV.visibility =
            if (status) View.VISIBLE else View.GONE

        firstChoiceTV.text =
            list[position].answers!!.options[0].option
        secondChoiceTV.text =
            list[position].answers!!.options[1].option
        thirdChoiceTV.text =
            list[position].answers!!.options[2].option
        fourthChoiceTV.text =
            list[position].answers!!.options[3].option
    }


    companion object {
        fun newInstance(): UBTQuestionFragment {
            return UBTQuestionFragment()
        }
    }

    override fun onSoundButtonClick(v: View?) {
        (activity as UBTQuestionActivity).playAudio(list[position].file.toString())
        fragmentUbtQuestionBinding.soundIBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_sound_exam_disable))
        fragmentUbtQuestionBinding.soundIBtn.isEnabled = false
        fragmentUbtQuestionBinding.soundIBtn.isClickable = false
    }

    override fun onQuestionImageClick(v: View?) {
        list[position].file?.let { showFullImage(it) }
    }

    override fun onFirstRBClick(v: View?) {
        firstChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onSecondRBClick(v: View?) {
        secondChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onThirdRBClick(v: View?) {
        thirdChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onFourthRBClick(v: View?) {
        fourthChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    private fun firstChoiceClick(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        passData(position, 0)
        firstChoice(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    private fun secondChoiceClick(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        passData(position, 1)
        secondChoice(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    private fun thirdChoiceClick(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        passData(position, 2)
        thirdChoice(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    private fun fourthChoiceClick(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        passData(position, 3)
        fourthChoice(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onFirstChoiceClick(v: View?) {
        firstChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onSecondChoiceClick(v: View?) {
        secondChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onThirdChoiceClick(v: View?) {
        thirdChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onFourthChoiceClick(v: View?) {
        fourthChoiceClick(firstChoiceRB, secondChoiceRB, thirdChoiceRB, fourthChoiceRB)
    }

    override fun onFirstChoiceSoundClick(v: View?) {
//        val url: String = list[position].answers!!.options[0].option.toString()
//        if (list[position].answers!!.type == LKWBConstants.IMAGE)
//            showFullImage(url)
//        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
//            (activity as UBTQuestionActivity).playAudio(url)
//            fragmentUbtQuestionBinding.firstChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
//            fragmentUbtQuestionBinding.firstChoiceIV.isEnabled = false
//            fragmentUbtQuestionBinding.firstChoiceIV.isClickable = false
//        }
    }

    override fun onSecondChoiceSoundClick(v: View?) {
//        val url: String = list[position].answers!!.options[1].option.toString()
//        if (list[position].answers!!.type == LKWBConstants.IMAGE)
//            showFullImage(url)
//        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
//            (activity as UBTQuestionActivity).playAudio(url)
//            fragmentUbtQuestionBinding.secondChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
//            fragmentUbtQuestionBinding.secondChoiceIV.isEnabled = false
//            fragmentUbtQuestionBinding.secondChoiceIV.isClickable = false
//        }
    }

    override fun onThirdChoiceSoundClick(v: View?) {
//        val url: String = list[position].answers!!.options[2].option.toString()
//        if (list[position].answers!!.type == LKWBConstants.IMAGE)
//            showFullImage(url)
//        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
//            (activity as UBTQuestionActivity).playAudio(url)
//            fragmentUbtQuestionBinding.thirdChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
//            fragmentUbtQuestionBinding.thirdChoiceIV.isEnabled = false
//            fragmentUbtQuestionBinding.thirdChoiceIV.isClickable = false
//        }
    }

    override fun onFourthChoiceSoundClick(v: View?) {
//        val url: String = list[position].answers!!.options[3].option.toString()
//        if (list[position].answers!!.type == LKWBConstants.IMAGE)
//            showFullImage(url)
//        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
//            (activity as UBTQuestionActivity).playAudio(url)
//            fragmentUbtQuestionBinding.fourthChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
//            fragmentUbtQuestionBinding.fourthChoiceIV.isEnabled = false
//            fragmentUbtQuestionBinding.fourthChoiceIV.isClickable = false
//        }
    }

    private fun firstChoiceSoundImage(firstChoiceIV: AppCompatImageView) {
        val url: String = list[position].answers!!.options[0].option.toString()
        if (list[position].answers!!.type == LKWBConstants.IMAGE)
            showFullImage(url)
        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
            (activity as UBTQuestionActivity).playAudio(url)
            firstChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
            firstChoiceIV.isEnabled = false
            firstChoiceIV.isClickable = false
        }
    }

    private fun secondChoiceSoundImage(secondChoiceIV: AppCompatImageView) {
        val url: String = list[position].answers!!.options[1].option.toString()
        if (list[position].answers!!.type == LKWBConstants.IMAGE)
            showFullImage(url)
        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
            (activity as UBTQuestionActivity).playAudio(url)
            secondChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
            secondChoiceIV.isEnabled = false
            secondChoiceIV.isClickable = false
        }
    }

    private fun thirdChoiceSoundImage(thirdChoiceIV: AppCompatImageView) {
        val url: String = list[position].answers!!.options[2].option.toString()
        if (list[position].answers!!.type == LKWBConstants.IMAGE)
            showFullImage(url)
        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
            (activity as UBTQuestionActivity).playAudio(url)
            thirdChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
            thirdChoiceIV.isEnabled = false
            thirdChoiceIV.isClickable = false
        }
    }

    private fun fourthChoiceSoundImage(fourthChoiceIV: AppCompatImageView) {
        val url: String = list[position].answers!!.options[3].option.toString()
        if (list[position].answers!!.type == LKWBConstants.IMAGE)
            showFullImage(url)
        else if (list[position].answers!!.type == LKWBConstants.AUDIO) {
            (activity as UBTQuestionActivity).playAudio(url)
            fourthChoiceIV.setImageDrawable(resources.getDrawable(R.drawable.ic_headphone_disable))
            fourthChoiceIV.isEnabled = false
            fourthChoiceIV.isClickable = false
        }
    }

    private fun firstChoiceRB(firstChoiceRB: AppCompatRadioButton, status: Boolean) {
        firstChoiceRB.isChecked = status
    }

    private fun secondChoiceRB(secondChoiceRB: AppCompatRadioButton, status: Boolean) {
        secondChoiceRB.isChecked = status
    }

    private fun thirdChoiceRB(thirdChoiceRB: AppCompatRadioButton, status: Boolean) {
        thirdChoiceRB.isChecked = status
    }

    private fun fourthChoiceRB(fourthChoiceRB: AppCompatRadioButton, status: Boolean) {
        fourthChoiceRB.isChecked = status

    }

    private fun firstChoice(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        firstChoiceRB(firstChoiceRB, true)
        secondChoiceRB(secondChoiceRB, false)
        thirdChoiceRB(thirdChoiceRB, false)
        fourthChoiceRB(fourthChoiceRB, false)
        choiceTextColor(R.color.header_text, R.color.black, R.color.black, R.color.black)
        if (isDeviceMobileLandScape(requireActivity() as AppCompatActivity))
            choiceBackgroundColor(
                R.drawable.option_selected_background,
                R.drawable.option_background,
                R.drawable.option_background,
                R.drawable.option_background
            )
    }

    private fun secondChoice(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        firstChoiceRB(firstChoiceRB, false)
        secondChoiceRB(secondChoiceRB, true)
        thirdChoiceRB(thirdChoiceRB, false)
        fourthChoiceRB(fourthChoiceRB, false)
        choiceTextColor(R.color.black, R.color.header_text, R.color.black, R.color.black)
        if (isDeviceMobileLandScape(requireActivity() as AppCompatActivity))
            choiceBackgroundColor(
                R.drawable.option_background,
                R.drawable.option_selected_background,
                R.drawable.option_background,
                R.drawable.option_background
            )
    }

    private fun thirdChoice(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        firstChoiceRB(firstChoiceRB, false)
        secondChoiceRB(secondChoiceRB, false)
        thirdChoiceRB(thirdChoiceRB, true)
        fourthChoiceRB(fourthChoiceRB, false)
        choiceTextColor(R.color.black, R.color.black, R.color.header_text, R.color.black)
        if (isDeviceMobileLandScape(requireActivity() as AppCompatActivity))
            choiceBackgroundColor(
                R.drawable.option_background,
                R.drawable.option_background,
                R.drawable.option_selected_background,
                R.drawable.option_background
            )
    }

    private fun fourthChoice(
        firstChoiceRB: AppCompatRadioButton,
        secondChoiceRB: AppCompatRadioButton,
        thirdChoiceRB: AppCompatRadioButton,
        fourthChoiceRB: AppCompatRadioButton
    ) {
        firstChoiceRB(firstChoiceRB, false)
        secondChoiceRB(secondChoiceRB, false)
        thirdChoiceRB(thirdChoiceRB, false)
        fourthChoiceRB(fourthChoiceRB, true)
        choiceTextColor(R.color.black, R.color.black, R.color.black, R.color.header_text)
        if (isDeviceMobileLandScape(requireActivity() as AppCompatActivity))
            choiceBackgroundColor(
                R.drawable.option_background,
                R.drawable.option_background,
                R.drawable.option_background,
                R.drawable.option_selected_background
            )
    }

    private fun choiceTextColor(first: Int, second: Int, third: Int, fourth: Int) {
        firstChoiceTV.setTextColor(resources.getColor(first))
        secondChoiceTV.setTextColor(resources.getColor(second))
        thirdChoiceTV.setTextColor(resources.getColor(third))
        fourthChoiceTV.setTextColor(resources.getColor(fourth))
    }

    private fun choiceBackgroundColor(first: Int, second: Int, third: Int, fourth: Int) {
        firstChoice.setBackgroundResource(first)
        secondChoice.setBackgroundResource(second)
        thirdChoice.setBackgroundResource(third)
        fourthChoice.setBackgroundResource(fourth)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        questionDataPasser = context as IQuestionDataPass
    }

    private fun passData(position: Int, answer: Int) {
        questionDataPasser.onQuestionDataPass(position, answer)
    }

    private fun showFullImage(imageUrl: String) {
        AppUtils.showImageDialog(requireContext(), imageUrl)
    }

    override fun onPageChanged() {
        Log.d("TAG", "Page Changed Swipe")
    }

}

