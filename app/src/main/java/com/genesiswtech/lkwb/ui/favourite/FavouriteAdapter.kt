package com.genesiswtech.lkwb.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import javax.inject.Inject


class FavouriteAdapter @Inject constructor(
    private val favouriteList: ArrayList<String>
) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.titleTV.text = favouriteList[position]
    }

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: AppCompatTextView = itemView.findViewById(R.id.favouriteTitleTV)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(favouriteList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_favourite,
            parent, false
        )
        return FavouriteViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }
}
