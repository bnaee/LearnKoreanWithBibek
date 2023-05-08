package com.genesiswtech.lkwb.ui.dictionary


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentDictionaryBinding
import com.genesiswtech.lkwb.databinding.FragmentGrammarBinding
import com.genesiswtech.lkwb.ui.dictionary.presenter.DictionaryPresenter
import com.genesiswtech.lkwb.ui.dictionary.view.IDictionaryView
import com.genesiswtech.lkwb.ui.dictionarySearch.DictionarySearchActivity
import com.genesiswtech.lkwb.ui.dictionaryWord.DictionaryWordActivity
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayDataResponse
import com.genesiswtech.lkwb.ui.grammar.presenter.GrammarPresenter
import com.genesiswtech.lkwb.ui.grammarWord.GrammarWordActivity
import com.genesiswtech.lkwb.ui.search.SearchActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import java.util.*


class DictionaryFragment : Fragment(R.layout.fragment_dictionary), IDictionaryView {

    private lateinit var fragmentDictionaryBinding: FragmentDictionaryBinding
    private lateinit var wordDayDataResponse: GrammarDayDataResponse
    private var dictionaryPresenter: DictionaryPresenter? = null
    private var firstRun: Boolean? = false
    private lateinit var tts: TextToSpeech

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
//        AppUtils.actionBarFragment(requireActivity(), getString(R.string.dictionary))
        val binding = FragmentDictionaryBinding.bind(itemView)
        fragmentDictionaryBinding = binding
        binding.dictionaryHandler = this
        initDependencies()
        addHash()
        dictionaryPresenter!!.getWordDay(requireContext())
    }

    override fun onDictionarySearchClick(v: View?) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.search))
        intent.putExtra(LKWBConstants.DATA_TYPE, LKWBConstants.DICTIONARY)
        startActivity(intent)
    }


    override fun onDictionaryKERelClick(v: View?) {
        val intent = Intent(context, DictionarySearchActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.korean_to_english))
        startActivity(intent)
    }

    override fun onDictionaryKNRelClick(v: View?) {
        val intent = Intent(context, DictionarySearchActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.korean_to_nepali))
        startActivity(intent)

    }

    override fun initDependencies() {
        dictionaryPresenter = DictionaryPresenter(this, requireActivity().application)
    }

    override fun onWordDayClick(v: View?) {
        val intent = Intent(context, DictionaryWordActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_ID, wordDayDataResponse.dictionary!!.id)
        intent.putExtra(LKWBConstants.DATA_TITLE, wordDayDataResponse.dictionary!!.word)
        startActivity(intent)
    }

    override fun onWordTitleClick(v: View?) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.KOREAN
                tts.setSpeechRate(1.0f)
                tts.speak(wordDayDataResponse.dictionary!!.word, TextToSpeech.SUCCESS, null)
            }
        }
    }

    override fun onSuccess(wordDayData: GrammarDayDataResponse) {
        this.wordDayDataResponse = wordDayData
        fragmentDictionaryBinding.wordDayTitleTV.text = wordDayDataResponse.dictionary!!.word
        if (wordDayDataResponse.dictionary!!.meaning!!.npMeaning != null) {
            fragmentDictionaryBinding.wordDaySubTV.text =
                Html.fromHtml(wordDayDataResponse.dictionary!!.meaning!!.npMeaning, 0)
        } else {
            if (wordDayDataResponse.dictionary!!.meaning!!.enMeaning != null)
                fragmentDictionaryBinding.wordDaySubTV.text =
                    Html.fromHtml(wordDayDataResponse.dictionary!!.meaning!!.enMeaning, 0)
        }
//        fragmentDictionaryBinding.wordDayEngTV.text =
//            Html.fromHtml(wordDayDataResponse.dictionary!!.meaning!!.npMeaning, 0)
        fragmentDictionaryBinding.wordDayCV.visibility = View.VISIBLE
    }

    private fun addHash() {
        val arr = arrayOf("Who", "This", "What")
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (i in 1 until arr.size) {
            val rowView: View = inflater.inflate(R.layout.layout_dictionary_tag, null)
            val toTV = rowView.findViewById<AppCompatTextView>(R.id.hashTV)
            toTV.text = arr[i]
            if (i == 1) {
                toTV.tag = "Clicked"
                toTV.setTextColor(resources.getColor(R.color.white))
                toTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue_background));
            } else
                toTV.tag = "Unclicked"

            toTV.setOnClickListener {
                if (toTV.tag == "Clicked") {
                    toTV.tag = "Unclicked"
                    toTV.setTextColor(resources.getColor(R.color.black))
                    toTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.dictionary_hash_background_unpressed));
                } else {
                    toTV.tag = "Clicked"
                    toTV.setTextColor(resources.getColor(R.color.white))
                    toTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue_background));
                }

            }
            fragmentDictionaryBinding.hashLinear.orientation =
                LinearLayout.HORIZONTAL //Setting layout orientation
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 0, 10, 0)
            rowView.layoutParams = params
            fragmentDictionaryBinding.hashLinear.addView(rowView)
        }
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
        fragmentDictionaryBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser && firstRun == false) {
//            dictionaryPresenter!!.getWordDay(requireContext())
//            firstRun = true
//        }
//    }

}