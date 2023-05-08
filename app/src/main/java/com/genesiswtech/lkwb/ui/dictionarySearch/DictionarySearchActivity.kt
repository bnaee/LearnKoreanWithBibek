package com.genesiswtech.lkwb.ui.dictionarySearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityDictionarySearchBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.presenter.DictionarySearchPresenter
import com.genesiswtech.lkwb.ui.dictionarySearch.view.IDictionarySearchView
import com.genesiswtech.lkwb.ui.dictionaryWord.DictionaryWordActivity
import com.genesiswtech.lkwb.ui.search.SearchActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class DictionarySearchActivity : BaseActivity<ActivityDictionarySearchBinding>(), IDictionarySearchView {

    private lateinit var dictionarySearchBinding: ActivityDictionarySearchBinding
    private var dictionarySearchPresenter: DictionarySearchPresenter? = null
    private var title: String? = null

    private var page: Int = 1
    private var limit: Int = LKWBConstants.LIMIT
    private var loading: Boolean = true
    private var swipe: Boolean = false
    private var dictionaryList = ArrayList<DictionarySearchDataResponse>()
    private lateinit var dictionarySearchAdapter: DictionarySearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)
        AppUtils.actionBarWithTitle(
            this,
            title!!
        )
        dictionarySearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dictionary_search)
        dictionarySearchBinding.dictionarySearchHandler = this
        initDependencies()
        setDictionaryAdapter(dictionaryList)

    }

    override fun onDictionarySearchClick(v: View?) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, title)
        intent.putExtra(LKWBConstants.DATA_TYPE, LKWBConstants.DICTIONARY)
        startActivity(intent)
    }

    override fun initDependencies() {
        dictionarySearchPresenter = DictionarySearchPresenter(this, application)
    }

    private fun setDictionaryAdapter(data: ArrayList<DictionarySearchDataResponse>) {
        dictionarySearchBinding.recyclerViewWordDiscover.layoutManager = LinearLayoutManager(this)
        dictionarySearchAdapter = DictionarySearchAdapter(data, this, title!!)
        dictionarySearchBinding.recyclerViewWordDiscover.adapter = dictionarySearchAdapter

        dictionarySearchBinding.dictionaryPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (dictionarySearchBinding.dictionaryPage.getChildAt(0).bottom
                <= (dictionarySearchBinding.dictionaryPage.height + dictionarySearchBinding.dictionaryPage.scrollY)
            ) {
                if (!loading) {
                    page++
                    apiCall(page)
                    loading = true
                }
            }
        })
        dictionarySearchAdapter.onItemClick = {
            val intent = Intent(this, DictionaryWordActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.word)
            startActivity(intent)
        }

        apiCall(page)
    }

    private fun apiCall(page: Int) {
        dictionarySearchPresenter!!.getDictionaries(this, page, limit)
    }

    override fun onSuccess(dictionarySearchDataResponseList: ArrayList<DictionarySearchDataResponse>) {
        dictionarySearchAdapter.updateList(dictionarySearchDataResponseList)
        loading = false
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
        dictionarySearchBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        dictionarySearchBinding.bottomProgressBar.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1)
            onShowProgressBar(status)
        else
            onShowBottomProgressBar(status)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override val binding: ActivityDictionarySearchBinding
        get() = ActivityDictionarySearchBinding.inflate(layoutInflater)
}