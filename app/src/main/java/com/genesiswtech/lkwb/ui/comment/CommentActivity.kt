package com.genesiswtech.lkwb.ui.comment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityCommentBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.comment.presenter.CommentPresenter
import com.genesiswtech.lkwb.ui.comment.view.ICommentView
import com.genesiswtech.lkwb.ui.commentReply.CommentReplyActivity
import com.genesiswtech.lkwb.ui.discussionDetail.CommentAdapter
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentDataResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentEditDataResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentResponse
import com.genesiswtech.lkwb.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CommentActivity : BaseActivity<ActivityCommentBinding>(), ICommentView {

    private lateinit var commentBinding: ActivityCommentBinding
    private var commentPresenter: CommentPresenter? = null
    private var id: Int? = null
    private var page: Int = 1
    private var loading: Boolean = true
    private var swipe: Boolean = false
    private var limit: Int = LKWBConstants.LIMIT
    private var commentList = ArrayList<CommentDataResponse>()
    private lateinit var commentAdapter: CommentAdapter

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        AppUtils.actionbar(this)
        setTitle(R.string.comments)
        initDependencies()
        commentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        commentBinding.commentHandler = this
        setCommentAdapter(commentList)
        commentApiCall(page, limit)
        updateCountFromReply()
    }

    private fun commentApiCall(page: Int, limit: Int) {
        commentPresenter!!.getComments(this, id!!, page, limit)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initDependencies() {
        commentPresenter = CommentPresenter(this, application)
    }

    override fun onAddCommentClick(v: View?) {
        if (AppUtils.isLoggedOn()) {
            showCommentDialog(
                LKWBConstants.POST,
                getString(R.string.add_comment),
                id!!,
                "",
                getString(R.string.add_comment),
                getString(R.string.cancel)
            )
        } else
            AppUtils.showLoginDialog(this)

    }

    private fun setCommentAdapter(data: ArrayList<CommentDataResponse>) {
        commentBinding.commentRV.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(data, this)
        commentBinding.commentRV.addItemDecorationWithoutLastItem()
        commentBinding.commentRV.adapter = commentAdapter

        commentBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        commentBinding.swipeRefresh.setOnRefreshListener {
            commentBinding.swipeRefresh.isRefreshing = false
//            swipe = true
//            page = 1
//            commentApiCall(page, limit)
//            onShowProgressBar(false)
        }

        commentBinding.commentPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (commentBinding.commentPage.getChildAt(0).bottom <= (commentBinding.commentPage.height + commentBinding.commentPage.scrollY)) {
                if (!loading) {
                    page++
                    commentApiCall(page, limit)
                    loading = true
                }
            }
        })

        commentAdapter.onItemClick = {
            val intent = Intent(this, CommentReplyActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            startActivity(intent)
        }

        commentAdapter.onEditClick = {
            val commentData: CommentDataResponse = it
            showCommentDialog(
                LKWBConstants.EDIT,
                getString(R.string.edit_comment),
                commentData.id!!.toInt(),
                commentData.body.toString(),
                getString(R.string.Update),
                getString(R.string.cancel)
            )
        }

        commentAdapter.onDeleteClick = {
            it.id?.let { it1 -> deleteDialog(it1) }
        }
    }

    override fun onCommentPostSuccess(commentData: CommentDataResponse) {
//        commentAdapter.addComment(commentData)
        updateBlogFromComment()
        if (commentAdapter.itemCount > 0)
            onNoData(false)
        reloadList()

    }

    override fun onSuccess(commentDataList: ArrayList<CommentDataResponse>) {
        if (swipe) {
            commentBinding.swipeRefresh.isRefreshing = false
            commentAdapter.removeAllItems()
            swipe = false
        }
        commentAdapter.updateList(commentDataList)
        loading = false

    }

    override fun onCommentDeleteSuccess(commentData: CommentResponse) {
        commentAdapter.deleteComment()
        updateBlogFromComment()
        if (commentAdapter.itemCount == 0)
            onNoData(true)
    }

    override fun onCommentEditSuccess(commentData: CommentEditDataResponse) {
        commentAdapter.updateComment(commentData.body.toString())
    }

    override fun onCommentEmpty() {
        showSnackBar(getString(R.string.comment_required))
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun onNoData(status: Boolean) {
        if (status) commentBinding.noDataTV.visibility = View.VISIBLE
        else commentBinding.noDataTV.visibility = View.GONE
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        commentBinding.llProgressBar.linear.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        commentBinding.bottomProgressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1) onShowProgressBar(status)
        else onShowBottomProgressBar(status)
    }

    private fun updateBlogFromComment() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateBlogFromComment) }
    }

    private fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager) return
        addItemDecoration(DividerItemDecoratorGrey(context))
    }

    private fun deleteDialog(id: Int) {
        AppUtils.showDialog(this,
            title = getString(R.string.sure_to_delete),
            "",
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
                commentPresenter!!.postDeleteComment(this, id, "DELETE")
            },
            negativeButtonFunction = { })
    }

    private fun showCommentDialog(
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
                if (it.isEmpty()) onCommentEmpty()
                else {
                    if (commentType == LKWBConstants.EDIT) commentPresenter!!.postEditComment(
                        this, commentId, "PUT", it
                    )
                    else if (commentType == LKWBConstants.POST) commentPresenter!!.postComment(
                        this, id!!, it
                    )
                }
            },
            negativeButtonFunction = { })

    }

    private fun updateCountFromReply() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateReplyCount -> {
                    reloadList()
                }
                else -> {}
            }
        }
    }

    private fun reloadList() {
        commentAdapter.removeAllItems()
        page = 1
        commentApiCall(page, limit)
    }

    override val binding: ActivityCommentBinding
        get() = ActivityCommentBinding.inflate(layoutInflater)

}