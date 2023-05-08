package com.genesiswtech.lkwb.ui.discussion

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListDiscussionBinding
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionDataResponse
import com.genesiswtech.lkwb.utils.TimeAgo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DiscussionAdapter @Inject constructor(
    private val discussionList: ArrayList<DiscussionDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>() {

    private var discussListBinding: ListDiscussionBinding? = null
    var onItemClick: ((DiscussionDataResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        val discussion = discussionList[position]

        holder.discussListBinding.titleTV.text = discussion.title
        if (discussion.description == null)
            holder.discussListBinding.descriptionTV.text = context.getString(R.string.na)
        else
            holder.discussListBinding.descriptionTV.text = discussion.excerpt
        holder.discussListBinding.timeTV.text =
            context.getString(R.string.updated) + " " + discussion.displayDate
        holder.discussListBinding.nameTV.text =
            " " + context.getString(R.string.by) + " " + discussion.user
        Glide.with(context)
            .load(discussion.userImage)
            .into(holder.discussListBinding.picIV)
        if (discussion.totalComments == 0) {
            holder.discussListBinding.commentCountTV.text = context.getString(R.string.comment)
        } else if (discussion.totalComments == 1)
            holder.discussListBinding.commentCountTV.text =
                discussion.totalComments.toString() + " " + context.getString(R.string.comment)
        else if (discussion.totalComments!! > 1)
            holder.discussListBinding.commentCountTV.text =
                discussion.totalComments.toString() + " " + context.getString(R.string.comments)
        val strDate = discussion.createdAt
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date: Date = dateFormat.parse(strDate) as Date
        holder.discussListBinding.timeTV.text =
            context.getString(R.string.updated) + " " + TimeAgo.getTimeAgo(context, date)

    }

    inner class DiscussionViewHolder(val discussListBinding: ListDiscussionBinding) :
        RecyclerView.ViewHolder(discussListBinding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(discussionList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        discussListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_discussion,
                parent,
                false
            )
        return DiscussionViewHolder(discussListBinding!!)

    }

    override fun getItemCount(): Int {
        return discussionList.size
    }

    fun updateList(discussList: ArrayList<DiscussionDataResponse>) {
        discussionList.addAll(discussList)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        discussionList.clear()
        notifyDataSetChanged()
    }


}