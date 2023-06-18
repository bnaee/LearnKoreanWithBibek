package com.genesiswtech.lkwb.ui.commentReply

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityCommentReplyBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyDataResponse
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyResponse
import com.genesiswtech.lkwb.ui.commentReply.presenter.CommentReplyPresenter
import com.genesiswtech.lkwb.ui.commentReply.view.ICommentReplyView
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CommentReplyActivity : BaseActivity<ActivityCommentReplyBinding>(), ICommentReplyView {

    private lateinit var replyBinding: ActivityCommentReplyBinding
    private var replyPresenter: CommentReplyPresenter? = null
    private var id: Int? = null
    private var page: Int = 1
    private var limit: Int = LKWBConstants.LIMIT
    private lateinit var replyAdapter: ReplyAdapter

    private var loading: Boolean = true
    private var swipe: Boolean = false
    private var replyList = ArrayList<CommentReplyDataResponse>()

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        AppUtils.actionbar(this)
        setTitle(R.string.replies)
        initDependencies()
        replyBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_comment_reply)
        replyBinding.replyHandler = this
        setReplyAdapter(replyList)
        replyApiCall(page, limit)
    }

    private fun replyApiCall(page: Int, limit: Int) {
        replyPresenter!!.getReplies(this, id!!, page, limit)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initDependencies() {
        replyPresenter = CommentReplyPresenter(this, application)
    }

    override fun onAddReplyClick(v: View?) {
        if (AppUtils.isLoggedOn()) {
            showReplyDialog(
                LKWBConstants.POST,
                getString(R.string.add_a_reply),
                id!!,
                "",
                getString(R.string.add_reply),
                getString(R.string.cancel)
            )
        } else
            AppUtils.showLoginDialog(this)

    }


    override fun onReplySuccess(replyDataList: ArrayList<CommentReplyDataResponse>) {
        if (swipe) {
            replyBinding.swipeRefresh.isRefreshing = false
            replyAdapter.removeAllItems()
            swipe = false
        }
        replyAdapter.updateList(replyDataList)
        loading = false
    }

    private fun setReplyAdapter(data: ArrayList<CommentReplyDataResponse>) {
        replyBinding.replyRV.layoutManager = LinearLayoutManager(this)
        replyAdapter = ReplyAdapter(data, this)
        replyBinding.replyRV.adapter = replyAdapter

        replyBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        replyBinding.swipeRefresh.setOnRefreshListener {
            replyBinding.swipeRefresh.isRefreshing = false
//            swipe = true
//            page = 1
//            replyApiCall(page, limit)
//            onShowProgressBar(false)
        }

        replyBinding.replyRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading) {
                        page++
                        replyApiCall(page, limit)
                        loading = true
                    }
                }
            }
        })

        replyAdapter.onEditClick = {
            val commentData: CommentReplyDataResponse = it
            showReplyDialog(
                LKWBConstants.EDIT,
                getString(R.string.edit_reply),
                commentData.id!!.toInt(),
                commentData.body.toString(),
                getString(R.string.Update),
                getString(R.string.cancel)
            )
        }

        replyAdapter.onDeleteClick = {
            it.id?.let { it1 -> deleteDialog(it1) }
        }
    }

    override fun onReplyPostSuccess(replyData: CommentReplyDataResponse) {
//        replyAdapter.addReply(replyData)
//        if (replyAdapter.itemCount > 0)
//            onNoData(false)
        updateReplyCount()
        reloadList()
    }

    override fun onReplyDeleteSuccess(replyData: CommentReplyResponse) {
        replyAdapter.deleteReply()
        if (replyAdapter.itemCount == 0)
            onNoData(true)
        updateReplyCount()
    }

    override fun onReplyEditSuccess(replyData: CommentReplyDataResponse) {
        replyAdapter.updateReply(replyData.body.toString())
    }

    override fun onReplyEmpty() {
        showSnackBar(getString(R.string.reply_required))
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun onNoData(status: Boolean) {
        if (status)
            replyBinding.noDataTV.visibility = View.VISIBLE
        else
            replyBinding.noDataTV.visibility = View.GONE
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        replyBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        replyBinding.bottomProgressBar.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1)
            onShowProgressBar(status)
        else
            onShowBottomProgressBar(status)
    }

    private fun deleteDialog(id: Int) {
        AppUtils.showDialog(this,
            title = getString(R.string.sure_to_delete),
            "",
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
                replyPresenter!!.postDeleteReply(this, id, "DELETE")
            },
            negativeButtonFunction = { }
        )
    }

    private fun showReplyDialog(
        commentType: String,
        dialogTitle: String,
        commentId: Int,
        comment: String,
        textPositiveButton: String,
        textNegativeButton: String
    ) {
        AppUtils.showCommentDialog(this,
            type = commentType,
            title = dialogTitle,
            comment,
            titleOfPositiveButton = textPositiveButton,
            titleOfNegativeButton = textNegativeButton,
            positiveButtonFunction = {
                AppUtils.hideKeyboard(this)
                if (it.isEmpty()) showSnackBar(getString(R.string.enter_comment))
                else {
                    if (commentType == LKWBConstants.EDIT) replyPresenter!!.postEditReply(
                        this, commentId, "PUT", it
                    )
                    else if (commentType == LKWBConstants.POST) replyPresenter!!.postReply(
                        this, id!!, it
                    )
                }
            },
            negativeButtonFunction = { })
    }

    private fun updateReplyCount() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateReplyCount) }
    }

    private fun reloadList() {
        replyAdapter.removeAllItems()
        page = 1
        replyApiCall(page, limit)
    }

    override val binding: ActivityCommentReplyBinding
        get() = ActivityCommentReplyBinding.inflate(layoutInflater)

}