package com.genesiswtech.lkwb.ui.grammarWord

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.QuoteSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.databinding.ActivityGrammarWordBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordDataResponse
import com.genesiswtech.lkwb.ui.grammarWord.model.Meanings
import com.genesiswtech.lkwb.ui.grammarWord.model.RelatedWords
import com.genesiswtech.lkwb.ui.grammarWord.model.Rules
import com.genesiswtech.lkwb.ui.grammarWord.presenter.GrammarWordPresenter
import com.genesiswtech.lkwb.ui.grammarWord.view.IGrammarWordView
import com.genesiswtech.lkwb.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class GrammarWordActivity : BaseActivity<ActivityGrammarWordBinding>(), IGrammarWordView {

    private lateinit var grammarWordBinding: ActivityGrammarWordBinding
    private var grammarWordPresenter: GrammarWordPresenter? = null
    private var grammarWordData: GrammarWordDataResponse? = null
    private var id: Int? = null
    private var title: String? = null
    private lateinit var tts: TextToSpeech
    private lateinit var mTTS: TextToSpeech
    private var isFavourite: Boolean? = null
    lateinit var adRequest: AdRequest

    private val lkwbEventBus by inject<LKWBEventBus>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        AppUtils.actionBarWithTitle(this, title!!)
        grammarWordBinding = DataBindingUtil.setContentView(this, R.layout.activity_grammar_word)
        grammarWordBinding.grammarWordHandler = this
        initDependencies()
        initializeAd()
        grammarWordPresenter!!.getGrammarWord(this, id!!)

        mTTS = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) mTTS.language = Locale.KOREAN
        }
    }

    private fun initializeAd() {
        MobileAds.initialize(this)
        adRequest = AdRequest.Builder().build()
        grammarWordBinding.adView.loadAd(adRequest)
    }

    private fun addChipToGroup(relatedWords: RelatedWords) {
        val linear = layoutInflater.inflate(
            R.layout.layout_single_chip_related_words,
            grammarWordBinding.setChipGroup,
            false
        ) as LinearLayout
        val chip: Chip = linear.findViewById(R.id.chipCpp)
        chip.text = relatedWords.word
        chip.isClickable = false
        chip.isCheckable = false
        chip.isActivated = false
        chip.isFocusable = false
        chip.isEnabled = false
        linear.setOnClickListener {
            val intent = Intent(this, GrammarWordActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, relatedWords.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, relatedWords.word)
            startActivity(intent)
        }
        grammarWordBinding.setChipGroup.addView(linear as View)
        linear.setBackgroundResource(R.drawable.ubt_pink_background)
    }

    override fun initDependencies() {
        grammarWordPresenter = GrammarWordPresenter(this, application)
    }

    override fun onWordSoundButtonClick(v: View?) {
        mTTS.speak(title, TextToSpeech.QUEUE_FLUSH, null)
//        tts = TextToSpeech(this) {
//            if (it == TextToSpeech.SUCCESS) {
//                tts.language = Locale.KOREAN
//                tts.setSpeechRate(1.0f)
//                tts.speak(title, TextToSpeech.SUCCESS, null)
//            }
//        }
    }

    override fun onWordShareButtonClick(v: View?) {
        AppUtils.shareText(this, title!!, grammarWordData!!.shareLink.toString())
    }

    override fun onWordFavouriteButtonClick(v: View?) {
        if (isFavourite == true)
            grammarWordPresenter!!.postRemoveFavouriteGrammar(this, id!!, LKWBConstants.GRAMMAR)
        else
            grammarWordPresenter!!.postAddFavouriteGrammar(this, id!!, LKWBConstants.GRAMMAR)
    }


    override fun onSuccess(grammarWordDataResponse: GrammarWordDataResponse) {
        grammarWordData = grammarWordDataResponse
        isFavourite = grammarWordDataResponse.isFavorite
        grammarWordBinding.grammarWordTitleTV.text = grammarWordDataResponse.word
        setMeanings(grammarWordDataResponse.meanings!!)
        setRules(grammarWordDataResponse.rules!!)
        if (grammarWordDataResponse.examples != null)
            grammarWordBinding.examplesTV.text =
                Html.fromHtml(grammarWordDataResponse.examples, 0).trim()
        grammarWordBinding.grammarWordLinear.visibility = View.VISIBLE
        if (isFavourite == true)
            grammarWordBinding.wordFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite_click))
        for (items in grammarWordDataResponse.relatedWords)
            addChipToGroup(items)
        onShowProgressBar(false)
    }

    override fun onAddFavouriteSuccess(grammarFavouriteResponse: FavouriteResponse) {
        if (grammarFavouriteResponse.code == 200) {
            isFavourite = true
            grammarWordBinding.wordFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite_click))
            showSnackBar(getString(R.string.add_to_favourite))
//            addFavouritesToList(grammarFavouriteResponse.data)
        }
    }

    override fun onRemoveFavouriteSuccess(grammarFavouriteResponse: FavouriteResponse) {
        if (grammarFavouriteResponse.code == 200) {
            isFavourite = false
            grammarWordBinding.wordFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite))
            showSnackBar(getString(R.string.remove_from_favourite))
            removeFavouritesFromList(id!!)
        }
    }

    private fun setMeanings(meanings: Meanings) {
        if (meanings.kr != null)
            grammarWordBinding.meaningKoreanTV.text = Html.fromHtml(meanings.kr, 0).trim()
        if (meanings.en != null) {
            Log.d("TAG", "HTML Text: " + meanings.en)
            grammarWordBinding.meaningEnglishTV.text =
                Html.fromHtml(meanings.en, Html.FROM_HTML_MODE_LEGACY).trim()
//            grammarWordBinding.meaningEnglishTV.text =meanings.en
//            displayHtml(meanings.en.toString())
        }
        if (meanings.np != null)
            grammarWordBinding.meaningNepaliTV.text = Html.fromHtml(meanings.np, 0).trim()

    }

    private fun setRules(rules: Rules) {
        if (rules.kr != null)
            grammarWordBinding.ruleKoreanTV.text = Html.fromHtml(rules.kr, 0).trim()
        if (rules.en != null)
            grammarWordBinding.ruleEnglishTV.text = Html.fromHtml(rules.en, 0).trim()
        if (rules.np != null)
            grammarWordBinding.ruleNepaliTV.text = Html.fromHtml(rules.np, 0).trim()
    }


    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        grammarWordBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun removeFavouritesFromList(id: Int) {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.RemoveFavourites(id)) }
    }

    private fun addFavouritesToList(grammar: Grammar) {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.AddGrammarFavourites(grammar)) }
    }


    private fun displayHtml(text: String) {


//        val imageGetter = ImageGetter(resources, html_viewer)
        val styledText = HtmlCompat.fromHtml(
            text,    //Instead of copying pasting, I kept it as a string
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            null,
            null
        )
//        ImageClick(styledText as Spannable)
        //replaceQuoteSpans(styledText as Spannable)

        grammarWordBinding.meaningNepaliTV.text = styledText
        grammarWordBinding.meaningNepaliTV.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun replaceQuoteSpans(spannable: Spannable) {

        val quoteSpans: Array<QuoteSpan> =
            spannable.getSpans(0, spannable.length - 1, QuoteSpan::class.java)

        for (quoteSpan in quoteSpans) {
            val start: Int = spannable.getSpanStart(quoteSpan)
            val end: Int = spannable.getSpanEnd(quoteSpan)
            val flags: Int = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(
                QuoteSpanClass(
                    ContextCompat.getColor(this, R.color.line_divider_grey),
                    ContextCompat.getColor(this, R.color.colorAccent),
                    10F,
                    50F
                ),
                start,
                end,
                flags
            )
        }

    }

    override val binding: ActivityGrammarWordBinding
        get() = ActivityGrammarWordBinding.inflate(layoutInflater)


}