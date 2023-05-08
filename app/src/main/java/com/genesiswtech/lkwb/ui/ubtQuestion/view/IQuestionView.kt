package com.genesiswtech.lkwb.ui.ubtQuestion.view

import android.view.View

interface IQuestionView {
    fun onSoundButtonClick(v: View?)
    fun onQuestionImageClick(v: View?)
    fun onFirstRBClick(v: View?)
    fun onSecondRBClick(v: View?)
    fun onThirdRBClick(v: View?)
    fun onFourthRBClick(v: View?)

    fun onFirstChoiceClick(v: View?)
    fun onSecondChoiceClick(v: View?)
    fun onThirdChoiceClick(v: View?)
    fun onFourthChoiceClick(v: View?)

    fun onFirstChoiceSoundClick(v: View?)
    fun onSecondChoiceSoundClick(v: View?)
    fun onThirdChoiceSoundClick(v: View?)
    fun onFourthChoiceSoundClick(v: View?)
}