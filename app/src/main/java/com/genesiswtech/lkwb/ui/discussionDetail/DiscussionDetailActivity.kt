package com.genesiswtech.lkwb.ui.discussionDetail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityDiscussionDetailBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.commentReply.CommentReplyActivity
import com.genesiswtech.lkwb.ui.discussionDetail.model.*
import com.genesiswtech.lkwb.ui.discussionDetail.presenter.DiscussionDetailsPresenter
import com.genesiswtech.lkwb.ui.discussionDetail.view.IDiscussionDetailView
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class DiscussionDetailActivity : BaseActivity<ActivityDiscussionDetailBinding>(),
    IDiscussionDetailView {

    private lateinit var discussionDetailBinding: ActivityDiscussionDetailBinding
    private var discussionDetailPresenter: DiscussionDetailsPresenter? = null
    private var id: Int? = null
    private var page: Int = 1
    private var loading: Boolean = true
    private var swipe: Boolean = false
    private var limit: Int = LKWBConstants.LIMIT
    private var discussionDetailData: DiscussionDetailDataResponse? = null
    private var commentList = ArrayList<CommentDataResponse>()
    private lateinit var commentAdapter: CommentAdapter

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        AppUtils.actionbar(this)
        setTitle(R.string.comments)
        initDependencies()
        discussionDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_discussion_detail)
        discussionDetailBinding.discussionDetailHandler = this
        setCommentAdapter(commentList)
        discussionDetailPresenter!!.getDiscussionSingle(this, id!!)
        commentApiCall(page, limit)
        updateCountFromReply()
    }

    private fun commentApiCall(page: Int, limit: Int) {
        discussionDetailPresenter!!.getComments(this, id!!, page, limit)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initDependencies() {
        discussionDetailPresenter = DiscussionDetailsPresenter(this, application)
    }

    override fun onDiscussionEditClick(v: View?) {
        showEditDiscussionDialog(
            this,
            id!!,
            getString(R.string.edit_discussion),
            getString(R.string.edit_discussion),
            discussionDetailData!!.title.toString(),
            discussionDetailData!!.description.toString()
        )
    }

    override fun onDiscussionShareClick(v: View?) {
        AppUtils.shareText(
            this,
            discussionDetailData!!.shareLink.toString(),
            discussionDetailData!!.shareLink.toString()
        )
    }

    override fun onDiscussionDeleteClick(v: View?) {
        deleteDialog(LKWBConstants.DISCUSSION, id!!)
    }

    override fun onAddCommentClick(v: View?) {
        if (AppUtils.isLoggedOn()) {
            discussionDetailPresenter!!.postComment(
                this,
                id!!,
                discussionDetailBinding.commentEdt.text.toString()
            )
        } else
            AppUtils.showLoginDialog(this)

    }

    private fun setCommentAdapter(data: ArrayList<CommentDataResponse>) {
        discussionDetailBinding.commentRV.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(data, this)
        discussionDetailBinding.commentRV.adapter = commentAdapter
        discussionDetailBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        discussionDetailBinding.swipeRefresh.setOnRefreshListener {
            discussionDetailBinding.swipeRefresh.isRefreshing = false
//            swipe = true
//            page = 1
//            commentApiCall(page, limit)
//            onShowProgressBar(false)
        }

        discussionDetailBinding.discussionDetailPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (discussionDetailBinding.discussionDetailPage.getChildAt(0).bottom
                <= (discussionDetailBinding.discussionDetailPage.height + discussionDetailBinding.discussionDetailPage.scrollY)
            ) {
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
            editCommentDialog(commentData)
        }

        commentAdapter.onDeleteClick = {
            it.id?.let { it1 -> deleteDialog(LKWBConstants.COMMENT, it1) }
        }
    }

    override fun onSuccess(discussionDetailResponse: DiscussionDetailResponse) {
        discussionDetailData = discussionDetailResponse.data
        setData(discussionDetailData!!)

    }

    private fun setData(discussionDetailData: DiscussionDetailDataResponse) {
        Glide.with(this)
            .load(discussionDetailData.userImage)
            .into(discussionDetailBinding.picIV)
        discussionDetailBinding.titleTV.text = discussionDetailData.title
        discussionDetailBinding.postNameTV.text =
            getString(R.string.posted_by) + " " + discussionDetailData.user
        discussionDetailBinding.timeTV.text = discussionDetailData.displayDate

        if (discussionDetailData.description == null)
            discussionDetailBinding.descriptionTV.text = getString(R.string.na)
        else
            discussionDetailBinding.descriptionTV.text = discussionDetailData.description
        if (AppUtils.isLoggedOn()) {
            if (discussionDetailData.userId != LKWBPreferencesManager.getString(LKWBConstants.USER_ID)!!
                    .toInt()
            ) {
                discussionDetailBinding.editTV.visibility = View.GONE
                discussionDetailBinding.deleteTV.visibility = View.GONE
            }
        } else {
            discussionDetailBinding.editTV.visibility = View.GONE
            discussionDetailBinding.deleteTV.visibility = View.GONE
        }
        discussionDetailBinding.discussionDetailLL.visibility = View.VISIBLE
    }

    private fun setEditData(discussionDetailData: DiscussionEditDataResponse) {
        discussionDetailBinding.titleTV.text = discussionDetailData.title
        discussionDetailBinding.descriptionTV.text = discussionDetailData.description
    }

    override fun onDiscussionDeleteSuccess(discussionDetailResponse: DiscussionDetailResponse) {
        updateDiscussionListFromDetail()
        finish()
    }

    override fun onDiscussionEditSuccess(discussionDetailResponse: DiscussionEditResponse) {
        discussionDetailResponse.data?.let { setEditData(it) }
        updateDiscussionListFromDetail()
    }

    override fun onCommentPostSuccess(commentData: CommentDataResponse) {
//        commentAdapter.addComment(commentData)
        discussionDetailBinding.commentEdt.setText("")
        updateDiscussionListFromDetail()
        reloadList()
    }

    override fun onCommentSuccess(commentDataList: ArrayList<CommentDataResponse>) {
        if (swipe) {
            discussionDetailBinding.swipeRefresh.isRefreshing = false
            commentAdapter.removeAllItems()
            swipe = false
        }
        commentAdapter.updateList(commentDataList)
        loading = false

    }

    override fun onCommentDeleteSuccess(commentData: CommentResponse, id: Int) {
        commentAdapter.deleteComment()
        updateDiscussionListFromDetail()
    }

    override fun onCommentEditSuccess(commentDataResponse: CommentEditDataResponse, id: Int) {
        commentAdapter.updateComment(commentDataResponse.body.toString())
    }

    override fun onCommentEmpty() {
        showSnackBar(getString(R.string.comment_required))
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

    override fun onNoData(status: Boolean) {
        if (status)
            discussionDetailBinding.noDataTV.visibility = View.VISIBLE
        else
            discussionDetailBinding.noDataTV.visibility = View.GONE
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        discussionDetailBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        discussionDetailBinding.bottomProgressBar.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onChooseProgressBar(page: Int, status: Boolean) {
        if (page == 1)
            onShowProgressBar(status)
        else
            onShowBottomProgressBar(status)
    }

    private fun updateDiscussionListFromDetail() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateDiscussionFromDetail) }
    }

    private fun showEditDiscussionDialog(
        context: Context,
        id: Int,
        dialogTitleText: String,
        dialogBtnText: String,
        title: String,
        description: String
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_discussion)
        val dialogTitle = dialog.findViewById(R.id.dialogTitleTV) as AppCompatTextView
        val titleData = dialog.findViewById(R.id.titleEdt) as AppCompatEditText
        val descriptionData = dialog.findViewById(R.id.descriptionEdt) as AppCompatEditText
        val dialogPositiveButton = dialog.findViewById(R.id.addDiscussionBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton

        titleData.setText(title)
        descriptionData.setText(description)
        dialogTitle.text = dialogTitleText
        dialogPositiveButton.text = dialogBtnText
        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogPositiveButton.setOnClickListener {
            if (TextUtils.isEmpty(dialogTitle.text.toString())) {
                onTitleError()
            } else {
                discussionDetailPresenter!!.postEditDiscussion(
                    context,
                    id,
                    "PUT",
                    titleData.text.toString(),
                    descriptionData.text.toString()
                )
                dialog.dismiss()
            }

        }
        dialogNegativeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteDialog(type: String, id: Int) {
        AppUtils.showDialog(this,
            title = getString(R.string.sure_to_delete),
            "",
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
                if (type == LKWBConstants.DISCUSSION)
                    discussionDetailPresenter!!.postDeleteDiscussion(this, id, "DELETE")
                else if (type == LKWBConstants.COMMENT)
                    discussionDetailPresenter!!.postDeleteComment(this, id, "DELETE")
            },
            negativeButtonFunction = { }
        )
    }

    private fun editCommentDialog(data: CommentDataResponse) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment_edit)
        val dialogTitle = dialog.findViewById(R.id.fullNameEdt) as AppCompatEditText
        dialogTitle.setText(data.body.toString())
        val dialogPositiveButton = dialog.findViewById(R.id.updateBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton

        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogPositiveButton.setOnClickListener {
            AppUtils.hideKeyboard(this)
            if (dialogTitle.text.isNullOrEmpty()) showSnackBar(getString(R.string.enter_comment))
            else {
                discussionDetailPresenter!!.postEditComment(
                    this,
                    data.id!!.toInt(),
                    "PUT",
                    dialogTitle.text.toString()
                )
                dialog.dismiss()
            }

        }
        dialogNegativeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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

    override val binding: ActivityDiscussionDetailBinding
        get() = ActivityDiscussionDetailBinding.inflate(layoutInflater)

}