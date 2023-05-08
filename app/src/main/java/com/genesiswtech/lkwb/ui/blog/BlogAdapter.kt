package com.genesiswtech.lkwb.ui.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import javax.inject.Inject

class BlogAdapter @Inject constructor(
    private val blogList: ArrayList<BlogDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    var onItemClick: ((BlogDataResponse) -> Unit)? = null

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
        val descriptionTV: AppCompatTextView = itemView.findViewById(R.id.blogSubTV)
        val blogIV: AppCompatImageView = itemView.findViewById(R.id.blogImg)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(blogList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_blog,
            parent, false
        )
        return BlogViewHolder(itemView)

    }

    fun removeBlogList(id: Int) {
        for (item in blogList) {
            if (item.id == id) {
                blogList.remove(item)
                notifyDataSetChanged()
                break
            }
        }
    }

    fun addBlogList(blogDataResponse: BlogDataResponse) {
        blogList.add(blogDataResponse)
        notifyDataSetChanged()
    }

    fun updateList(ubtList: ArrayList<BlogDataResponse>) {
        blogList.addAll(ubtList)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        blogList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return blogList.size
    }
}