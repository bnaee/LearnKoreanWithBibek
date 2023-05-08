package com.genesiswtech.lkwb.ui.blog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentBlogBinding
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.blog.model.BlogResponse
import com.genesiswtech.lkwb.ui.blog.presenter.BlogPresenter
import com.genesiswtech.lkwb.ui.blog.view.IBlogView
import com.genesiswtech.lkwb.ui.blogDetail.BlogDetailActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants

class BlogFragment : Fragment(R.layout.fragment_blog), IBlogView {

    private lateinit var fragmentBlogBinding: FragmentBlogBinding
    private var blogPresenter: BlogPresenter? = null
    private var blogDataList: ArrayList<BlogDataResponse>? = null
    private var firstRun: Boolean? = false

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
//        AppUtils.actionBarFragment(requireActivity(), getString(R.string.blog))
        val binding = FragmentBlogBinding.bind(itemView)
        fragmentBlogBinding = binding
        initDependencies()

    }

    private fun setBlogAdapter(data: ArrayList<BlogDataResponse>) {
        fragmentBlogBinding.recyclerViewBlog.layoutManager = LinearLayoutManager(activity)
        val blogAdapter = BlogAdapter(data, requireActivity())
        fragmentBlogBinding.recyclerViewBlog.adapter = blogAdapter
        blogAdapter.onItemClick = {
            val intent = Intent(requireContext(), BlogDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.title)
            startActivity(intent)
        }
    }


    override fun initDependencies() {
        blogPresenter = BlogPresenter(this, requireActivity().application)
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
        AppUtils.showSnackBar(message.toString(), requireActivity())
    }

    override fun onShowProgressBar(status: Boolean) {
        fragmentBlogBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && firstRun == false) {
            blogPresenter!!.getBlogs(requireContext())
            firstRun = true
        }
    }

}