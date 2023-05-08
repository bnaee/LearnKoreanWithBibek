package com.genesiswtech.lkwb.ui.favouriteDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityFavouriteDetailBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.blog.BlogAdapter
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.blogDetail.BlogDetailActivity
import com.genesiswtech.lkwb.ui.dictionarySearch.DictionarySearchAdapter
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.DictionaryWordActivity
import com.genesiswtech.lkwb.ui.favouriteDetail.presenter.FavouriteDetailPresenter
import com.genesiswtech.lkwb.ui.favouriteDetail.view.IFavouriteDataPass
import com.genesiswtech.lkwb.ui.favouriteDetail.view.IFavouriteDetailView
import com.genesiswtech.lkwb.ui.grammar.GrammarCategoryAdapter
import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import com.genesiswtech.lkwb.ui.grammarWord.GrammarWordActivity
import com.genesiswtech.lkwb.utils.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class FavouriteDetailActivity : BaseActivity<ActivityFavouriteDetailBinding>(),
    IFavouriteDetailView, IFavouriteDataPass {

    private lateinit var favouriteDetailBinding: ActivityFavouriteDetailBinding
    private var favouriteDetailPresenter: FavouriteDetailPresenter? = null
    private var title: String? = null
    lateinit var favouriteDataPasser: IFavouriteDataPass
    private val lkwbEventBus by inject<LKWBEventBus>()

    private var dictionaryAdapter: DictionarySearchAdapter? = null
    private var blogAdapter: BlogAdapter? = null
    private var grammarAdapter: GrammarCategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        AppUtils.actionBarWithTitle(this, title!!)

        favouriteDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_favourite_detail)
        favouriteDetailBinding.favouriteDetailHandler = this

        initDependencies()
        if (getString(R.string.blogs) == title)
            favouriteDetailPresenter!!.getFavouriteBlog(this)
        if (getString(R.string.grammars) == title)
            favouriteDetailPresenter!!.getFavouriteGrammar(this)
        if (getString(R.string.dictionarys) == title)
            favouriteDetailPresenter!!.getFavouriteDictionary(this)

        removeFavouriteFromList()

    }

    private fun setBlogAdapter(data: ArrayList<BlogDataResponse>) {
        favouriteDetailBinding.favouriteDetailRV.layoutManager = LinearLayoutManager(this)
        blogAdapter = BlogAdapter(data, this)
        favouriteDetailBinding.favouriteDetailRV.adapter = blogAdapter
        blogAdapter!!.onItemClick = {
            val intent = Intent(this, BlogDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.title)
            startActivity(intent)
        }
        onNoData(blogAdapter!!.itemCount)
    }

    private fun setGrammarAdapter(data: ArrayList<Grammar>) {
        favouriteDetailBinding.favouriteDetailRV.layoutManager = LinearLayoutManager(this)
        grammarAdapter = GrammarCategoryAdapter(this, data)
        favouriteDetailBinding.favouriteDetailRV.adapter = grammarAdapter
        grammarAdapter!!.onItemClick = {
            val intent = Intent(this, GrammarWordActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.word)
            startActivity(intent)
        }
        onNoData(grammarAdapter!!.itemCount)
    }

    private fun setDictionaryAdapter(data: ArrayList<DictionarySearchDataResponse>) {
        favouriteDetailBinding.favouriteDetailRV.layoutManager = LinearLayoutManager(this)
        dictionaryAdapter = DictionarySearchAdapter(data, this, "")
        favouriteDetailBinding.favouriteDetailRV.adapter = dictionaryAdapter
        dictionaryAdapter!!.onItemClick = {
            val intent = Intent(this, DictionaryWordActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.word)
            startActivity(intent)
        }
        onNoData(dictionaryAdapter!!.itemCount)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initDependencies() {
        favouriteDetailPresenter = FavouriteDetailPresenter(this, application)
    }


    override fun onFavouriteGrammarSuccess(grammarList: ArrayList<Grammar>) {
        setGrammarAdapter(grammarList)
    }

    override fun onFavouriteDictionarySuccess(dictionaryList: ArrayList<DictionarySearchDataResponse>) {
        setDictionaryAdapter(dictionaryList)
    }

    override fun onFavouriteBlogSuccess(blogList: ArrayList<BlogDataResponse>) {
        setBlogAdapter(blogList)
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onNoData(size: Int) {
        if (size == 0)
            favouriteDetailBinding.noDataTV.visibility = View.VISIBLE
        else
            favouriteDetailBinding.noDataTV.visibility = View.GONE
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        favouriteDetailBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onFavouriteDataPass(id: Int, type: String) {
        if (type == LKWBConstants.DICTIONARY) {
            Log.d("TAG", "Data Pass success:" + id)
        }
    }

    private fun removeFavouriteFromList() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.RemoveFavourites -> {
                    removeFromList(lkwbEvent.id)
                }
                is LKWBEvents.AddBlogFavourites -> {
                    addToList(lkwbEvent.blogDataResponse)
                }
                is LKWBEvents.MyEventWithoutData -> {
                    // Perform action here
                }
                else -> {}
            }
        }
    }

    private fun removeFromList(id: Int) {
        if (LKWBConstants.BLOG == title!!.lowercase(Locale.getDefault()))
            blogAdapter!!.removeBlogList(id)
        if (LKWBConstants.GRAMMAR == title!!.lowercase(Locale.getDefault()))
            favouriteDetailPresenter!!.getFavouriteGrammar(this)
        if (LKWBConstants.DICTIONARY == title!!.lowercase(Locale.getDefault()))
            favouriteDetailPresenter!!.getFavouriteDictionary(this)
    }

    private fun addToList(blogDataResponse: BlogDataResponse) {
        if (LKWBConstants.BLOG == title!!.lowercase(Locale.getDefault()))
            blogAdapter!!.addBlogList(blogDataResponse)
//        if (LKWBConstants.GRAMMAR == title!!.lowercase(Locale.getDefault()))
//            favouriteDetailPresenter!!.getFavouriteGrammar(this)
//        if (LKWBConstants.DICTIONARY == title!!.lowercase(Locale.getDefault()))
//            favouriteDetailPresenter!!.getFavouriteDictionary(this)
    }

    override val binding: ActivityFavouriteDetailBinding
        get() = ActivityFavouriteDetailBinding.inflate(layoutInflater)
}