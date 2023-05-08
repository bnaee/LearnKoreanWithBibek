package com.genesiswtech.lkwb.ui.discussionDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListCommentBinding
import com.genesiswtech.lkwb.ui.discussionDetail.model.CommentDataResponse
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import com.genesiswtech.lkwb.utils.TimeAgo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CommentAdapter @Inject constructor(
    private val commentList: ArrayList<CommentDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var commentListBinding: ListCommentBinding? = null

    var selected_pos: Int = -1

    var onItemClick: ((CommentDataResponse) -> Unit)? = null

    var onEditClick: ((CommentDataResponse) -> Unit)? = null

    var onDeleteClick: ((CommentDataResponse) -> Unit)? = null

    var count = 0

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.commentListBinding.titleTV.text = comment.userName
        holder.commentListBinding.descriptionTV.text = comment.body
        Glide.with(context)
            .load(comment.userImage)
            .into(holder.commentListBinding.picIV)
        if (comment.userId == LKWBPreferencesManager.getString(LKWBConstants.USER_ID)!!.toInt())
            holder.commentListBinding.optionIBtn.visibility = View.VISIBLE
        else
            holder.commentListBinding.optionIBtn.visibility = View.GONE
        holder.commentListBinding.timeTV.text =
            context.getString(R.string.updated) + " " + comment.displayDate
        holder.commentListBinding.nameTV.text =
            " " + context.getString(R.string.by) + " " + comment.userName
        if (comment.replies != null) {
            count = comment.replies!!
            if (count == 0)
                holder.commentListBinding.replyCountTV.text = context.getString(R.string.reply)
            else if (count == 1)
                holder.commentListBinding.replyCountTV.text =
                    comment.replies.toString() + " " + context.getString(R.string.reply)
            else if (count > 1)
                holder.commentListBinding.replyCountTV.text =
                    comment.replies.toString() + " " + context.getString(R.string.replies)
        }
        val strDate = comment.dateTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date: Date = dateFormat.parse(strDate) as Date
        holder.commentListBinding.timeTV.text =
            context.getString(R.string.updated) + " " + TimeAgo.getTimeAgo(context, date)

        holder.commentListBinding.optionIBtn.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.commentListBinding.optionIBtn)
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

    inner class CommentViewHolder(val commentListBinding: ListCommentBinding) :
        RecyclerView.ViewHolder(commentListBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(commentList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        commentListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_comment,
                parent,
                false
            )
        return CommentViewHolder(commentListBinding!!)

    }

    fun deleteComment() {
        commentList.removeAt(selected_pos)
        notifyDataSetChanged()
    }

    fun updateComment(body: String) {
        commentList[selected_pos].body = body
        notifyDataSetChanged()
    }

    fun addComment(body: CommentDataResponse) {
        commentList.add(0, body)
        notifyDataSetChanged()
    }

    fun updateList(discussList: ArrayList<CommentDataResponse>) {
        commentList.addAll(discussList)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        commentList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}