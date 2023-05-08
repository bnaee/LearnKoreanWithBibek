package com.genesiswtech.lkwb.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityNotificationBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.commentReply.CommentReplyActivity
import com.genesiswtech.lkwb.ui.discussionDetail.DiscussionDetailActivity
import com.genesiswtech.lkwb.ui.notification.model.NotificationDataResponse
import com.genesiswtech.lkwb.ui.notification.model.ReadNotificationResponse
import com.genesiswtech.lkwb.ui.notification.presenter.NotificationPresenter
import com.genesiswtech.lkwb.ui.notification.view.INotificationView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.DividerItemDecoratorGrey
import com.genesiswtech.lkwb.utils.LKWBConstants

class NotificationActivity : BaseActivity<ActivityNotificationBinding>(), INotificationView {

    private lateinit var notificationBinding: ActivityNotificationBinding
    private var notificationPresenter: NotificationPresenter? = null

    private var page: Int = 1
    private var limit: Int = LKWBConstants.LIMIT
    private var loading: Boolean = true
    private var swipe: Boolean = false

    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationDataList = ArrayList<NotificationDataResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppUtils.actionbar(this)
        setTitle(R.string.notification)
        notificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        notificationBinding.notificationHandler = this

        initDependencies()
        setNotificationAdapter(notificationDataList)
        notificationApiCall(page, limit)

    }

    private fun notificationApiCall(page: Int, limit: Int) {
        notificationPresenter!!.getNotifications(this, page, limit)
    }

    private fun setNotificationAdapter(data: ArrayList<NotificationDataResponse>) {
        notificationBinding.notificationRV.layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationAdapter(data, this)
        notificationBinding.notificationRV.addItemDecorationWithoutLastItem()
        notificationBinding.notificationRV.adapter = notificationAdapter
        notificationAdapter.onItemClick = {
            notificationPresenter!!.readNotification(this, it.id!!.toInt())
            if (it.notificationType == "Comment" && it.notificationForResponse!!.type == "Discussion") {
                val intent = Intent(this, DiscussionDetailActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_ID, it.notificationForResponse!!.id)
                startActivity(intent)
            }
            if (it.notificationType == "Reply") {
                val intent = Intent(this, CommentReplyActivity::class.java)
                intent.putExtra(LKWBConstants.DATA_ID, it.notificationForResponse!!.id)
                startActivity(intent)
            }
        }

        notificationBinding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.button_blue)
        )

        notificationBinding.swipeRefresh.setOnRefreshListener {
            swipe = true
            page = 1
            notificationApiCall(page, limit)
            onShowProgressBar(false)
        }

        notificationBinding.notificationPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!loading) {
                    page++
                    notificationApiCall(page, limit)
                    loading = true
                }
            }
        })

    }

    override fun initDependencies() {
        notificationPresenter = NotificationPresenter(this, application)
    }

    override fun onSuccess(notificationDataResponse: ArrayList<NotificationDataResponse>) {
        if (swipe) {
            notificationBinding.swipeRefresh.isRefreshing = false
            notificationAdapter.removeAllItems()
            swipe = false
        }
        notificationAdapter.updateList(notificationDataResponse)
        loading = false
    }

    override fun onReadSuccess(readNotificationResponse: ReadNotificationResponse) {
        page = 1
        swipe = true
        notificationApiCall(page, limit)
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun onNoData(status: Boolean) {
        if (status)
            notificationBinding.noDataTV.visibility = View.VISIBLE
        else
            notificationBinding.noDataTV.visibility = View.GONE
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), this)
    }

    override fun onShowProgressBar(status: Boolean) {
        notificationBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onShowBottomProgressBar(status: Boolean) {
        notificationBinding.bottomProgressBar.visibility =
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

    private fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager)
            return
        addItemDecoration(DividerItemDecoratorGrey(context))
    }

    override val binding: ActivityNotificationBinding
        get() = ActivityNotificationBinding.inflate(layoutInflater)
}