package com.genesiswtech.lkwb.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivitySearchBinding
import com.genesiswtech.lkwb.ui.blog.BlogAdapter
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.blogDetail.BlogDetailActivity
import com.genesiswtech.lkwb.ui.dictionarySearch.DictionarySearchAdapter
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.DictionaryWordActivity
import com.genesiswtech.lkwb.ui.discussion.DiscussionAdapter
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionDataResponse
import com.genesiswtech.lkwb.ui.discussionDetail.DiscussionDetailActivity
import com.genesiswtech.lkwb.ui.grammarWord.GrammarWordActivity
import com.genesiswtech.lkwb.ui.search.model.GrammarSearchDataResponse
import com.genesiswtech.lkwb.ui.search.presenter.SearchPresenter
import com.genesiswtech.lkwb.ui.search.view.ISearchView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants


class SearchActivity : BaseActivity<ActivitySearchBinding>(), ISearchView {

    private lateinit var searchBinding: ActivitySearchBinding
    private var searchPresenter: SearchPresenter? = null
    private var title: String? = null
    private var type: String? = null
    private var text: String? = ""
    private var page: Int = 1
    private var limit: Int = LKWBConstants.LIMIT
    private var loading: Boolean = true
    private var swipe: Boolean = false

    private var grammarList = ArrayList<GrammarSearchDataResponse>()
    private var dictionaryList = ArrayList<DictionarySearchDataResponse>()
    private var blogList = ArrayList<BlogDataResponse>()
    private var discussionList = ArrayList<DiscussionDataResponse>()

    private lateinit var grammarCategoryAdapter: GrammarSearchAdapter
    private lateinit var dictionarySearchAdapter: DictionarySearchAdapter
    private lateinit var blogAdapter: BlogAdapter
    private lateinit var discussionAdapter: DiscussionAdapter

    private var textChangedHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        type = intent.getStringExtra(LKWBConstants.DATA_TYPE)
        AppUtils.actionBarWithTitle(
            this, getString(R.string.search)
        )
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchBinding.grammarSearchHandler = this
        initDependencies()
        setAdapter(type!!)

        searchBinding.searchEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            true
        }

    }

    private fun setAdapter(type: String) {
        searchBinding.searchRV.layoutManager = LinearLayoutManager(this)
        if (type == LKWBConstants.GRAMMAR) {
            searchBinding.searchDescriptionTV.text = getString(R.string.search_grammar)
            grammarCategoryAdapter = GrammarSearchAdapter(this,grammarList)
            searchBinding.searchRV.adapter = grammarCategoryAdapter
            grammarCategoryAdapter.onItemClick = {
                val intent = Intent(this, GrammarWordActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_ID, it.id)
                intent.putExtra(LKWBConstants.DATA_TITLE, it.word)
                startActivity(intent)
            }
        }
        if (type == LKWBConstants.DICTIONARY) {
            searchBinding.searchDescriptionTV.text = getString(R.string.search_word)
            dictionarySearchAdapter = DictionarySearchAdapter(dictionaryList, this, title!!)
            searchBinding.searchRV.adapter = dictionarySearchAdapter
            dictionarySearchAdapter.onItemClick = {
                val intent = Intent(this, DictionaryWordActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_ID, it.id)
                intent.putExtra(LKWBConstants.DATA_TITLE, it.word)
                startActivity(intent)
            }

        }
        if (type == LKWBConstants.BLOG) {
            blogAdapter = BlogAdapter(blogList, this)
            searchBinding.searchRV.adapter = blogAdapter
            blogAdapter.onItemClick = {
                val intent = Intent(this, BlogDetailActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_ID, it.id)
                intent.putExtra(LKWBConstants.DATA_TITLE, it.title)
                startActivity(intent)
            }

        }
        if (type == LKWBConstants.DISCUSSION) {
            searchBinding.searchDescriptionTV.text = getString(R.string.search_discussion)
            discussionAdapter = DiscussionAdapter(discussionList, this)
            searchBinding.searchRV.adapter = discussionAdapter
            discussionAdapter.onItemClick = {
                val intent = Intent(this, DiscussionDetailActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_ID, it.id)
                startActivity(intent)
            }

        }

        searchBinding.searchPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (searchBinding.searchPage.getChildAt(0).bottom
                <= (searchBinding.searchPage.height + searchBinding.searchPage.scrollY)
            ) {
                if (!loading) {
                    page++
                    apiCall(page, type, text!!)
                    loading = true
                }
            }
        })

        searchBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        searchBinding.swipeRefresh.setOnRefreshListener {
            searchBinding.swipeRefresh.isRefreshing = false
//            swipe = true
//            page = 1
//            apiCall(page, type, text!!)
//            onShowProgressBar(false)
        }

        apiCall(page, type, "")
    }

    private fun apiCall(page: Int, type: String, text: String) {
        if (type == LKWBConstants.GRAMMAR) {
            searchPresenter!!.postGrammarSearch(this, page, limit, type, text)
        }
        if (type == LKWBConstants.DICTIONARY) {
            searchPresenter!!.postDictionarySearch(this, page, limit, type, text)
        }
        if (type == LKWBConstants.BLOG) {
            searchPresenter!!.postBlogSearch(this, page, limit, type, text)
        }
        if (type == LKWBConstants.DISCUSSION) {
            searchPresenter!!.postDiscussionSearch(this, page, limit, type, text)
        }
        AppUtils.hideKeyboard(this)
    }

    override fun initDependencies() {
        searchPresenter = SearchPresenter(this, application)
    }

    var runnable = Runnable {
        page = 1
        apiCall(page, type!!, text!!)
        if (type == LKWBConstants.GRAMMAR) {
            grammarCategoryAdapter.clearList()
        }
        if (type == LKWBConstants.DICTIONARY) {
            dictionarySearchAdapter.removeAllItems()
        }
        if (type == LKWBConstants.BLOG) {
            blogAdapter.removeAllItems()
        }
        if (type == LKWBConstants.DISCUSSION) {
            discussionAdapter.removeAllItems()
        }

    }

    private fun search()
    {
        page = 1
        apiCall(page, type!!, text!!)
        if (type == LKWBConstants.GRAMMAR) {
            grammarCategoryAdapter.clearList()
        }
        if (type == LKWBConstants.DICTIONARY) {
            dictionarySearchAdapter.removeAllItems()
        }
        if (type == LKWBConstants.BLOG) {
            blogAdapter.removeAllItems()
        }
        if (type == LKWBConstants.DISCUSSION) {
            discussionAdapter.removeAllItems()
        }
    }


    override fun onEditTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textChangedHandler.removeCallbacksAndMessages(null)
            }

            override fun afterTextChanged(s: Editable) {
                text = s.toString()
//                textChangedHandler.postDelayed(runnable, 1200)
            }
        }
    }

    override fun onGrammarSearchSuccess(grammarSearchDataList: ArrayList<GrammarSearchDataResponse>) {
        if (swipe) {
            searchBinding.swipeRefresh.isRefreshing = false
            grammarCategoryAdapter.clearList()
            swipe = false
        }
        grammarCategoryAdapter.updateList(grammarSearchDataList)
        loading = false
    }

    override fun onDictionarySearchSuccess(dictionarySearchDataList: ArrayList<DictionarySearchDataResponse>) {
        if (swipe) {
            searchBinding.swipeRefresh.isRefreshing = false
            dictionarySearchAdapter.removeAllItems()
            swipe = false
        }
        dictionarySearchAdapter.updateList(dictionarySearchDataList)
        loading = false
    }

    override fun onBlogSearchSuccess(blogSearchDataList: ArrayList<BlogDataResponse>) {
    }

    override fun onDiscussionSearchSuccess(discussionSearchDataList: ArrayList<DiscussionDataResponse>) {
        if (swipe) {
            searchBinding.swipeRefresh.isRefreshing = false
            discussionAdapter.removeAllItems()
            swipe = false
        }
        discussionAdapter.updateList(discussionSearchDataList)
        loading = false
    }

    override fun onNoData(status: Boolean) {
        if (page == 1)
            if (status) searchBinding.noDataTV.visibility = View.VISIBLE
            else searchBinding.noDataTV.visibility = View.GONE
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
        searchBinding.llProgressBar.linear.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        searchBinding.bottomProgressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1) onShowProgressBar(status)
        else onShowBottomProgressBar(status)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override val binding: ActivitySearchBinding
        get() = ActivitySearchBinding.inflate(layoutInflater)
}