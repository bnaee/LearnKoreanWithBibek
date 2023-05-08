package com.genesiswtech.lkwb.ui.grammar

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentGrammarBinding
import com.genesiswtech.lkwb.ui.grammar.model.GrammarCategoryDataResponse
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayDataResponse
import com.genesiswtech.lkwb.ui.grammar.presenter.GrammarPresenter
import com.genesiswtech.lkwb.ui.grammar.view.IGrammarView
import com.genesiswtech.lkwb.ui.grammarWord.GrammarWordActivity
import com.genesiswtech.lkwb.ui.search.SearchActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.google.android.material.tabs.TabLayout
import java.util.*


class GrammarFragment : Fragment(R.layout.fragment_grammar), IGrammarView {

    private lateinit var grammarDayDataResponse: GrammarDayDataResponse
    private var grammarPresenter: GrammarPresenter? = null
    private lateinit var fragmentGrammarBinding: FragmentGrammarBinding
    private var firstRun: Boolean? = false
    private lateinit var tts: TextToSpeech
    private var tts1: TextToSpeech? = null
    private var itemHeight: Int = 200

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentGrammarBinding.bind(itemView)
        fragmentGrammarBinding = binding
        binding.grammarHandler = this
        initDependencies()
        grammarPresenter!!.getGrammarDay(requireContext())
        grammarPresenter!!.getGrammarCategory(requireContext())
    }

    override fun initDependencies() {
        grammarPresenter = GrammarPresenter(this, requireActivity().application)
    }

    private fun setupViewPager(grammarCategoryDataResponseList: ArrayList<GrammarCategoryDataResponse>) {
        fragmentGrammarBinding.grammarCategoryVP.apply {
            adapter = GrammarCategoryFragmentAdapter(
                requireActivity().supportFragmentManager,
                fragmentGrammarBinding.grammarCategoryTab.tabCount,
                grammarCategoryDataResponseList
            )
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(fragmentGrammarBinding.grammarCategoryTab))
        }


        fragmentGrammarBinding.grammarCategoryVP.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //don't forget remove this listener
                view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                //get item height here
                itemHeight = fragmentGrammarBinding.grammarCategoryVP.height + 80
                Log.d("TAG", "Grammar Height: " + itemHeight)
                fragmentGrammarBinding.grammarCategoryVP.layoutParams.height =
                    (itemHeight * grammarCategoryDataResponseList[0].grammars.size) + (grammarCategoryDataResponseList[0].grammars.size * 25)
            }
        })
    }

    private fun setupTabLayout(grammarCategoryDataResponseList: ArrayList<GrammarCategoryDataResponse>) {
        fragmentGrammarBinding.grammarCategoryTab.apply {

            for (i in 0 until grammarCategoryDataResponseList.size) {
                addTab(
                    this.newTab().setText("  " + grammarCategoryDataResponseList[i].title + "  ")
                )
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        fragmentGrammarBinding.grammarCategoryVP.currentItem = it
                        fragmentGrammarBinding.grammarCategoryVP.layoutParams.height =
                            (itemHeight * grammarCategoryDataResponseList[it].grammars.size) + (grammarCategoryDataResponseList[it].grammars.size * 25)
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }


    override fun onGrammarDayClick(v: View?) {
        val intent = Intent(context, GrammarWordActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_ID, grammarDayDataResponse.grammar!!.id)
        intent.putExtra(LKWBConstants.DATA_TITLE, grammarDayDataResponse.grammar!!.word)
        startActivity(intent)
    }

    override fun onGrammarTitleClick(v: View?) {
//        installVoiceData()
//        val installIntent = Intent()
//        installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
//        val languages = ArrayList<String>()
//        languages.add("hin-IND") // hin - hindi, IND - INDIA
//
//        installIntent.putStringArrayListExtra(
//            TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR, languages
//        )
//        startActivity(installIntent)

        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.KOREAN
                tts.setSpeechRate(1.0f)
                tts.speak(grammarDayDataResponse.grammar!!.word, TextToSpeech.SUCCESS, null)
            }
        }
    }

    private fun installVoiceData() {
        val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.tts" /*replace with the package name of the target TTS engine*/)
        try {
            Log.v("TAG", "Installing voice data: " + intent.toUri(0))
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Log.e("TAG", "Failed to install TTS data, no acitivty found for $intent)")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.d("TAG1", "Voice Available")
//                tts = TextToSpeech(this, this)
            } else {
                Log.d("TAG1", "Voice Not Available")
                val installTTSIntent = Intent()
                installTTSIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                val languages = ArrayList<String>()
//                languages.add("hin-IND");
                languages.add("ko-KOR") // non sure if "it" is the right abbr for italian
                installTTSIntent.putStringArrayListExtra(
                    TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR,
                    languages
                )
                startActivity(installTTSIntent)
            }
        }
    }

    override fun onGrammarSearchClick(v: View?) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.search))
        intent.putExtra(LKWBConstants.DATA_TYPE, LKWBConstants.GRAMMAR)
        startActivity(intent)
    }

    override fun onSuccess(grammarDayDataResponse: GrammarDayDataResponse) {
        this.grammarDayDataResponse = grammarDayDataResponse
        fragmentGrammarBinding.grammarDayTitleTV.text = grammarDayDataResponse.grammar!!.word
        fragmentGrammarBinding.grammarDaySubTV.text =
            Html.fromHtml(grammarDayDataResponse.grammar!!.description, 0)
        fragmentGrammarBinding.grammarDayEngTV.text =
            Html.fromHtml(grammarDayDataResponse.grammar!!.description, 0)
        fragmentGrammarBinding.grammarDayCV.visibility = View.VISIBLE
    }

    override fun onGrammarCategorySuccess(grammarCategoryDataResponseList: ArrayList<GrammarCategoryDataResponse>) {
        setupTabLayout(grammarCategoryDataResponseList)
        setupViewPager(grammarCategoryDataResponseList)
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
        fragmentGrammarBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser && firstRun == false) {
//            try {
//                grammarPresenter!!.getGrammarDay(requireContext())
//                grammarPresenter!!.getGrammarCategory(requireContext())
//                firstRun = true
//            } catch (ex: Exception) {
//                Log.d("TAG", "Exception error: " + ex)
////                requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit();
//                requireFragmentManager().beginTransaction().detach(this).attach(this)
//                    .commit();
//            }
//
//        }
//    }
}