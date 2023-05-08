package com.genesiswtech.lkwb.ui.blogDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.data.BlogModel
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.blogDetail.model.LatestBlogs
import javax.inject.Inject

class BlogDetailAdapter @Inject constructor(
    private val blogList: ArrayList<LatestBlogs>,
    private val context: Context
) : RecyclerView.Adapter<BlogDetailAdapter.BlogViewHolder>() {

    var onItemClick: ((LatestBlogs) -> Unit)? = null

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogList[position]
        holder.titleTV.text = blog.title
        holder.descriptionTV.text = blog.publishedAt
        Glide.with(context)
            .load(blog.image)
            .into(holder.blogIV)
    }

    inner class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: AppCompatTextView = itemView.findViewById(R.id.blogTitleTV)
        val descriptionTV: AppCompatTextView = itemView.findViewById(R.id.blogTimeTV)
        val blogIV: AppCompatImageView = itemView.findViewById(R.id.blogIV)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(blogList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_blog_detail,
            parent, false
        )
        return BlogViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return blogList.size
    }
}