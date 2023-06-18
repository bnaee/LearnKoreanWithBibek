package com.genesiswtech.lkwb.ui.dictionaryWord

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.databinding.ActivityDictionaryWordBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordDataResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.presenter.DictionaryWordPresenter
import com.genesiswtech.lkwb.ui.dictionaryWord.view.IDictionaryWordView
import com.genesiswtech.lkwb.ui.favouriteDetail.view.IFavouriteDataPass
import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class DictionaryWordActivity : BaseActivity<ActivityDictionaryWordBinding>(), IDictionaryWordView {

    private lateinit var dictionaryWordBinding: ActivityDictionaryWordBinding
    private var dictionaryWordPresenter: DictionaryWordPresenter? = null
    private var dictionaryWordData: DictionaryWordDataResponse? = null
    private var id: Int? = null
    private lateinit var tts: TextToSpeech
    private var title: String? = null
    private var isFavourite: Boolean? = null

    private lateinit var mTTS: TextToSpeech
    lateinit var adRequest: AdRequest

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(
        savedInstanceState:
        Bundle?
    ) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        AppUtils.actionBarWithTitle(
            this,
            title!!
        )
        dictionaryWordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dictionary_word)
        dictionaryWordBinding.dictionaryWordHandler = this
        initDependencies()
        initializeAd()
        dictionaryWordPresenter!!.getSingleDictionary(this, id!!)
        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.KOREAN
            }
        })
    }

    private fun initializeAd() {
        MobileAds.initialize(this)
        adRequest = AdRequest.Builder().build()
        dictionaryWordBinding.adView.loadAd(adRequest)
        dictionaryWordBinding.adView1.loadAd(adRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initDependencies() {
        dictionaryWordPresenter = DictionaryWordPresenter(this, application)
    }

    override fun onFavouriteButtonClick(v: View?) {
        if (AppUtils.isLoggedOn()) {
            if (isFavourite == true)
                dictionaryWordPresenter!!.postRemoveFavouriteDictionary(
                    this,
                    id!!,
                    LKWBConstants.DICTIONARY
                )
            else
                dictionaryWordPresenter!!.postAddFavouriteDictionary(
                    this,
                    id!!,
                    LKWBConstants.DICTIONARY
                )
        } else
            AppUtils.showLoginDialog(this)

    }

    override fun onShareButtonClick(v: View?) {
        AppUtils.shareText(this, title!!, dictionaryWordData!!.shareLink.toString())
    }

    override fun onSoundButtonClick(v: View?) {
//        mTTS.speak(title, TextToSpeech.QUEUE_FLUSH, null)
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.KOREAN
                tts.setSpeechRate(1.0f)
                tts.speak(title, TextToSpeech.SUCCESS, null)
            }
        }
    }

    override fun onSuccess(dictionaryWordDataResponse: DictionaryWordDataResponse) {
        dictionaryWordData = dictionaryWordDataResponse
        isFavourite = dictionaryWordDataResponse.isFavorite
        dictionaryWordBinding.dictionaryWordLinear.visibility = View.VISIBLE
        dictionaryWordBinding.dictionaryWordTitle.text =
            Html.fromHtml(dictionaryWordDataResponse.word, 0)
        if (dictionaryWordDataResponse.meaning!!.npMeaning != null)
            dictionaryWordBinding.dictionaryWordNepaliTV.text =
                Html.fromHtml(dictionaryWordDataResponse.meaning!!.npMeaning, 0)
        if (dictionaryWordDataResponse.meaning!!.enMeaning != null)
            dictionaryWordBinding.dictionaryWordEngTV.text =
                Html.fromHtml(dictionaryWordDataResponse.meaning!!.enMeaning, 0)
        if (dictionaryWordDataResponse.examples != null)
            dictionaryWordBinding.dictionaryWordExampleTV.text =
                Html.fromHtml(dictionaryWordDataResponse.examples, 0)

        if (isFavourite == true)
            dictionaryWordBinding.dictionaryWordFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite_click))

    }

    override fun onAddFavouriteSuccess(dictionaryFavouriteResponse: FavouriteResponse) {
        if (dictionaryFavouriteResponse.code == 200) {
            isFavourite = true
            dictionaryWordBinding.dictionaryWordFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite_click))
            showSnackBar(getString(R.string.add_to_favourite))
//            addFavouritesToList(dictionaryFavouriteResponse.data)
        }
    }

    override fun onRemoveFavouriteSuccess(dictionaryFavouriteResponse: FavouriteResponse) {
        if (dictionaryFavouriteResponse.code == 200) {
            isFavourite = false
            dictionaryWordBinding.dictionaryWordFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite))
            showSnackBar(getString(R.string.remove_from_favourite))
            removeFavouritesFromList(id!!)
        }
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
        dictionaryWordBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    private fun removeFavouritesFromList(id: Int) {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.RemoveFavourites(id)) }
    }

    private fun addFavouritesToList(dictionarySearchDataResponse: DictionarySearchDataResponse) {
        lifecycleScope.launch {
            lkwbEventBus.emit(
                LKWBEvents.AddDictionaryFavourites(
                    dictionarySearchDataResponse
                )
            )
        }
    }

    override val binding: ActivityDictionaryWordBinding
        get() = ActivityDictionaryWordBinding.inflate(layoutInflater)

}