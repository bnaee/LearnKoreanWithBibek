package com.genesiswtech.lkwb.ui.blog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityBlogBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.blog.model.BlogResponse
import com.genesiswtech.lkwb.ui.blog.presenter.BlogPresenter
import com.genesiswtech.lkwb.ui.blog.view.IBlogView
import com.genesiswtech.lkwb.ui.blogDetail.BlogDetailActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class BlogActivity : BaseActivity<ActivityBlogBinding>(), IBlogView {

    private lateinit var blogBinding: ActivityBlogBinding
    private var blogPresenter: BlogPresenter? = null
    private var blogDataList: ArrayList<BlogDataResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppUtils.actionbar(this)
        setTitle(R.string.blog)
        blogBinding = DataBindingUtil.setContentView(this, R.layout.activity_blog)
        blogBinding.blogHandler = this

        initDependencies()
        blogPresenter!!.getBlogs(this)

    }

    private fun setBlogAdapter(data: ArrayList<BlogDataResponse>) {
        blogBinding.recyclerViewBlog.layoutManager = LinearLayoutManager(this)
        val blogAdapter = BlogAdapter(data, this)
        blogBinding.recyclerViewBlog.adapter = blogAdapter
        blogAdapter.onItemClick = {
            val intent = Intent(this, BlogDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.title)
            startActivity(intent)
        }
    }


    override fun initDependencies() {
        blogPresenter = BlogPresenter(this, application)
    }

    override fun onSuccess(blogResponse: BlogResponse) {
        onShowProgressBar(false)
        blogDataList = blogResponse.data
        setBlogAdapter(blogResponse.data)
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), this)
    }

    override fun onShowProgressBar(status: Boolean) {
        blogBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override val binding: ActivityBlogBinding
        get() = ActivityBlogBinding.inflate(layoutInflater)

}