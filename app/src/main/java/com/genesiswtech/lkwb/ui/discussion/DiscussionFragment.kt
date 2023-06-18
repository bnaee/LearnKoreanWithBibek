package com.genesiswtech.lkwb.ui.discussion

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentDiscussionBinding
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionDataResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionPostResponse
import com.genesiswtech.lkwb.ui.discussion.presenter.DiscussionPresenter
import com.genesiswtech.lkwb.ui.discussion.view.IDiscussionView
import com.genesiswtech.lkwb.ui.discussionDetail.DiscussionDetailActivity
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.ui.search.SearchActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import org.koin.android.ext.android.inject

class DiscussionFragment : Fragment(R.layout.fragment_discussion), IDiscussionView {

    private var discussionList = ArrayList<DiscussionDataResponse>()
    private var discussionPresenter: DiscussionPresenter? = null
    private lateinit var discussionBinding: FragmentDiscussionBinding
    private var firstRun: Boolean? = false
    private var discussionSortList = arrayOf(
        "Latest First",
        "Oldest First",
        "Highest Comment",
        "Lowest Comment",
    )

    private var discussionCallList = arrayOf(
        "latest",
        "oldest",
        "highest_comments",
        "lowest_comments",
    )

    private lateinit var discussionAdapter: DiscussionAdapter
    private var selectedPosition: Int = 0
    private var limit: Int = LKWBConstants.LIMIT
    private var page: Int = 1
    private var loading: Boolean = true
    private var swipe: Boolean = false

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentDiscussionBinding.bind(itemView)
        discussionBinding = binding
        binding.discussionHandler = this
        initDependencies()
        setSpinner(requireActivity())
        setDiscussionAdapter(discussionList)
        updateListFromDetail()
    }

    private fun setSpinner(activity: Activity) {
        val adapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, discussionSortList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        discussionBinding.sortSpinner.adapter = adapter

    }

    private fun setDiscussionAdapter(data: ArrayList<DiscussionDataResponse>) {
        discussionBinding.discussionRV.layoutManager = LinearLayoutManager(activity)
        discussionAdapter = DiscussionAdapter(data, requireActivity())
        discussionBinding.discussionRV.adapter = discussionAdapter

        discussionBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        discussionBinding.swipeRefresh.setOnRefreshListener {
//            discussionBinding.swipeRefresh.isRefreshing = false
            swipe = true
            page = 1
            discussionApiCall(page, limit, selectedPosition)
            onShowProgressBar(false)
        }

        discussionBinding.discussionPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!loading) {
                    page++
                    discussionApiCall(page, limit, selectedPosition)
                    loading = true
                }
            }
        })

        discussionAdapter.onItemClick = {
            val intent = Intent(requireContext(), DiscussionDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            startActivity(intent)
        }
    }

    override fun initDependencies() {
        discussionPresenter = DiscussionPresenter(this, requireActivity().application)
    }

    override fun onDiscussionSearchClick(v: View?) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_TITLE, getString(R.string.search))
        intent.putExtra(LKWBConstants.DATA_TYPE, LKWBConstants.DISCUSSION)
        startActivity(intent)
    }

    override fun onAddDiscussionClick(v: View?) {
        if (AppUtils.isLoggedOn()) {
            showAddDiscussionDialog(requireContext())
        } else
            AppUtils.showLoginDialog(requireContext())
    }

    override fun onSuccess(discussionDataResponse: ArrayList<DiscussionDataResponse>) {
        if (swipe) {
            discussionBinding.swipeRefresh.isRefreshing = false
            discussionAdapter.removeAllItems()
            swipe = false
        }
        discussionAdapter.updateList(discussionDataResponse)
        loading = false
    }

    override fun onNewDiscussionSuccess(discussionPostResponse: DiscussionPostResponse) {
        page = 1
        discussionApiCall(page, limit, selectedPosition)
    }

    override fun onTitleError() {
        showSnackBar(getString(R.string.title_required))
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
        discussionBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        discussionBinding.bottomProgressBar.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1)
            onShowProgressBar(status)
        else
            onShowBottomProgressBar(status)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && firstRun == false) {
            discussionBinding.sortSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        page = 1
                        discussionAdapter.removeAllItems()
                        selectedPosition = position
                        discussionApiCall(page, limit, position)
                    }

                }
            discussionApiCall(page, limit, selectedPosition)
            firstRun = true
        }
    }

    private fun showAddDiscussionDialog(
        context: Context
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_discussion)
        val dialogTitle = dialog.findViewById(R.id.titleEdt) as AppCompatEditText
        val dialogSubTitle = dialog.findViewById(R.id.descriptionEdt) as AppCompatEditText
        val dialogPositiveButton = dialog.findViewById(R.id.addDiscussionBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton

        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogPositiveButton.setOnClickListener {
            if (TextUtils.isEmpty(dialogTitle.text.toString())) {
                onTitleError()
            } else {
                discussionPresenter!!.postNewDiscussion(
                    context,
                    dialogTitle.text.toString(),
                    dialogSubTitle.text.toString()
                )
                dialog.dismiss()
            }

        }
        dialogNegativeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateListFromDetail() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateDiscussionFromDetail -> {
                    swipe = false
                    discussionBinding.swipeRefresh.isRefreshing = false
                    page = 1
                    discussionApiCall(page, limit, selectedPosition)
                }
                else -> {}
            }
        }
    }

    private fun discussionApiCall(page: Int, limit: Int, position: Int) {
        if (page == 1)
            discussionAdapter.removeAllItems()
        discussionPresenter!!.getDiscussions(
            requireContext(),
            page.toString(),
            limit.toString(),
            discussionCallList[position]
        )
    }

}