package com.genesiswtech.lkwb.ui.commentReply

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListCommentBinding
import com.genesiswtech.lkwb.databinding.ListReplyBinding
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyDataResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentDataResponse
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import com.genesiswtech.lkwb.utils.TimeAgo
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ReplyAdapter @Inject constructor(
    private val replyList: ArrayList<CommentReplyDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    private var replyListBinding: ListReplyBinding? = null

    var selected_pos: Int = -1

    var onItemClick: ((CommentReplyDataResponse) -> Unit)? = null

    var onEditClick: ((CommentReplyDataResponse) -> Unit)? = null

    var onDeleteClick: ((CommentReplyDataResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val comment = replyList[position]
        holder.replyListBinding.titleTV.text = comment.userName
        holder.replyListBinding.descriptionTV.text = comment.body
        holder.replyListBinding.replyTV.text = comment.displayDate
        Glide.with(context)
            .load(comment.userImage)
            .into(holder.replyListBinding.picIV)
        if (AppUtils.isLoggedOn()) {
            if (comment.userId == LKWBPreferencesManager.getString(LKWBConstants.USER_ID)!!.toInt())
                holder.replyListBinding.optionIBtn.visibility = View.VISIBLE
            else
                holder.replyListBinding.optionIBtn.visibility = View.GONE
        } else
            holder.replyListBinding.optionIBtn.visibility = View.GONE
        holder.replyListBinding.timeTV.text =
            context.getString(R.string.updated) + " " + comment.displayDate
        holder.replyListBinding.nameTV.text =
            " " + context.getString(R.string.by) + " " + comment.userName
        val strDate = comment.dateTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date: Date = dateFormat.parse(strDate) as Date
        holder.replyListBinding.timeTV.text =
            context.getString(R.string.updated) + " " + TimeAgo.getTimeAgo(context, date)


        holder.replyListBinding.optionIBtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(context, holder.replyListBinding.optionIBtn)
            popupMenu.menuInflater.inflate(R.menu.menu_comment, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.commentEdit -> {
                        onEditClick?.invoke(comment)
                        selected_pos = position
                    }
                    R.id.commentDelete -> {
                        onDeleteClick?.invoke(comment)
                        selected_pos = position
                    }
                }
                true
            })
            popupMenu.show()
        }
    }

    inner class ReplyViewHolder(val replyListBinding: ListReplyBinding) :
        RecyclerView.ViewHolder(replyListBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(replyList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        replyListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_reply,
                parent,
                false
            )
        return ReplyViewHolder(replyListBinding!!)

    }

    fun deleteReply() {
        replyList.removeAt(selected_pos)
        notifyDataSetChanged()
    }

    fun updateReply(body: String) {
        replyList[selected_pos].body = body
        notifyDataSetChanged()
    }

    fun addReply(body: CommentReplyDataResponse) {
        replyList.add(0, body)
        notifyDataSetChanged()
    }

    fun updateList(discussList: ArrayList<CommentReplyDataResponse>) {
        replyList.addAll(discussList)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        replyList.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return replyList.size
    }
}