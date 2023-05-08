package com.genesiswtech.lkwb.ui.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import javax.inject.Inject


class MenuAdapter @Inject constructor(
    private val menuList: ArrayList<String>,
    private val context: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.titleTV.text = menuList.get(position)
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTV: AppCompatTextView = itemView.findViewById(R.id.txt_menu_title)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(menuList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_menu,
            parent, false
        )
        return MenuViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}