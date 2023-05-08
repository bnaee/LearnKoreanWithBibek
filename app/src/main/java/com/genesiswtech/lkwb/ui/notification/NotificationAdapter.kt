package com.genesiswtech.lkwb.ui.notification

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListNotificationBinding
import com.genesiswtech.lkwb.ui.notification.model.NotificationDataResponse
import com.genesiswtech.lkwb.utils.TimeAgo
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NotificationAdapter @Inject constructor(
    private val notificationList: ArrayList<NotificationDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notificationBinding: ListNotificationBinding? = null

    var onItemClick: ((NotificationDataResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]

        if (notification.notificationType == "Referral") {
            holder.notificationBinding.titleTV.text =
                context.getString(R.string.notification_referral)
            holder.notificationBinding.picIV.setImageDrawable(context.resources.getDrawable(R.drawable.ic_referral))
        } else if (notification.notificationType == "Comment") {
            holder.notificationBinding.titleTV.text = Html.fromHtml(
                String.format(
                    context.getString(R.string.notification_comment),
                    notification.notificationByResponse!!.name,
                    notification.notificationForResponse!!.type!!.lowercase(Locale.getDefault()),
                    notification.notificationForResponse!!.title
                ), 0
            )
            setListImage(
                holder.notificationBinding.picIV,
                notification.notificationByResponse!!.avatar.toString()
            )
        } else if (notification.notificationType == "Reply") {
            holder.notificationBinding.titleTV.text = Html.fromHtml(
                String.format(
                    context.getString(R.string.notification_reply),
                    notification.notificationByResponse!!.name,
                    notification.notificationForResponse!!.commentOn!!.model!!.lowercase(Locale.getDefault()),
                    notification.notificationForResponse!!.commentOn!!.title
                ), 0
            )
            setListImage(
                holder.notificationBinding.picIV,
                notification.notificationByResponse!!.avatar.toString()
            )
        } else
            holder.notificationBinding.titleTV.text = notification.notificationByResponse!!.name
        holder.notificationBinding.descriptionTV.text = notification.date
        val strDate = notification.dateTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date: Date = dateFormat.parse(strDate) as Date
        holder.notificationBinding.descriptionTV.text = TimeAgo.getTimeAgo(context, date)



        if (notification.read == false)
            holder.notificationBinding.notificationRel.setBackgroundColor(context.getColor(R.color.unsolved_question))
        else
            holder.notificationBinding.notificationRel.setBackgroundColor(context.getColor(R.color.white))
    }

    private fun setListImage(imageView: CircleImageView, url: String) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    inner class NotificationViewHolder(val notificationBinding: ListNotificationBinding) :
        RecyclerView.ViewHolder(notificationBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(notificationList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        notificationBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_notification,
                parent,
                false
            )
        return NotificationViewHolder(notificationBinding!!)
    }


    override fun getItemCount(): Int {
        return notificationList.size
    }

    fun updateList(notificationsList: ArrayList<NotificationDataResponse>) {
        notificationList.addAll(notificationsList)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        notificationList.clear()
        notifyDataSetChanged()
    }
}