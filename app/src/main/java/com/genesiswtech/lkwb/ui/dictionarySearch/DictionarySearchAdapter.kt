package com.genesiswtech.lkwb.ui.dictionarySearch

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import javax.inject.Inject

class DictionarySearchAdapter @Inject constructor(
    private val dictionariesList: ArrayList<DictionarySearchDataResponse>,
    private val context: Context,
    private val type: String
) : RecyclerView.Adapter<DictionarySearchAdapter.DictionaryViewHolder>() {

    var onItemClick: ((DictionarySearchDataResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        val dictionary = dictionariesList[position]
        holder.titleTV.text = dictionary.word
        if (type == context.getString(R.string.korean_to_nepali)) {
            if (dictionary.meaning!!.npMeaning != null)
                holder.descriptionTV.text = dictionary.meaning!!.npMeaning!!.trim()
            else
                holder.descriptionTV.text = context.getString(R.string.na)
        } else if (type == context.getString(R.string.korean_to_english)) {
            if (dictionary.meaning!!.enMeaning != null)
                holder.descriptionTV.text = dictionary.meaning!!.enMeaning!!.trim()
            else
                holder.descriptionTV.text = context.getString(R.string.na)
        } else {
            if (dictionary.meaning!!.npMeaning != null)
                holder.descriptionTV.text = dictionary.meaning!!.npMeaning!!.trim()
            else if (dictionary.meaning!!.enMeaning != null)
                holder.descriptionTV.text = dictionary.meaning!!.enMeaning!!.trim()
            else holder.descriptionTV.text = context.getString(R.string.na)
        }
        Log.d("TAG", "Dictionary description: " + holder.descriptionTV.text)
        if (holder.descriptionTV.text.trim().isEmpty()) {
            holder.descriptionTV.text = context.getString(R.string.na)
        }

        if (position % 3 == 0) {
            holder.dictionaryCV.setBackgroundColor(context.getColor(R.color.ubt_blue))
        } else if ((position + 1) % 3 == 0) {
            holder.dictionaryCV.setBackgroundColor(context.getColor(R.color.ubt_pink))
        } else if ((position - 1) % 3 == 0) {
            holder.dictionaryCV.setBackgroundColor(context.getColor(R.color.ubt_yellow))
        }
    }

    inner class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: AppCompatTextView = itemView.findViewById(R.id.txt_discovery_title)
        val descriptionTV: AppCompatTextView = itemView.findViewById(R.id.txt_discovery_subtitle)
        val dictionaryCV: CardView = itemView.findViewById(R.id.dictionaryListCard)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dictionariesList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_dictionary_search,
            parent, false
        )
        return DictionaryViewHolder(itemView)
    }

    fun removeDictionaryList(id: Int) {
        for (item in dictionariesList) {
            if (item.id == id) {
                dictionariesList.remove(item)
                notifyDataSetChanged()
                break
            }
        }
    }

    fun updateList(ubtList: ArrayList<DictionarySearchDataResponse>) {
        dictionariesList.addAll(ubtList)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        dictionariesList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dictionariesList.size
    }
}