package com.genesiswtech.lkwb.ui.search

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.ui.search.model.GrammarSearchDataResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import javax.inject.Inject

class GrammarSearchAdapter @Inject constructor(
    private val context: Context,
    private val grammarList: ArrayList<GrammarSearchDataResponse>
) : RecyclerView.Adapter<GrammarSearchAdapter.GrammarSearchViewHolder>() {

    var onItemClick: ((GrammarSearchDataResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: GrammarSearchViewHolder, position: Int) {

        val dictionary = grammarList[position]
        holder.titleTV.text = dictionary.word
        if (dictionary.description != null) holder.descriptionTV.text =
            Html.fromHtml(dictionary.excerpt, 0).trim()

        if (position % 3 == 0) {
            holder.dictionaryListCard.setBackgroundColor(context.getColor(R.color.ubt_blue))
        } else if ((position + 1) % 3 == 0) {
            holder.dictionaryListCard.setBackgroundColor(context.getColor(R.color.ubt_pink))
        } else if ((position - 1) % 3 == 0) {
            holder.dictionaryListCard.setBackgroundColor(context.getColor(R.color.ubt_yellow))
        }
    }

    inner class GrammarSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: AppCompatTextView = itemView.findViewById(R.id.txt_discovery_title)
        val descriptionTV: AppCompatTextView = itemView.findViewById(R.id.txt_discovery_subtitle)
        val dictionaryListCard: CardView = itemView.findViewById(R.id.dictionaryListCard)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(grammarList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarSearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_dictionary_search, parent, false
        )
        return GrammarSearchViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return grammarList.size
    }

    fun updateList(ubtList: ArrayList<GrammarSearchDataResponse>) {
        grammarList.addAll(ubtList)
        notifyDataSetChanged()
    }

    fun clearList() {
        grammarList.clear()
        notifyDataSetChanged()
    }
}