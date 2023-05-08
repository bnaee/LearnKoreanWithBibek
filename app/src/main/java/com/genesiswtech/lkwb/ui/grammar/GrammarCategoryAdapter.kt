package com.genesiswtech.lkwb.ui.grammar

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import javax.inject.Inject


class GrammarCategoryAdapter @Inject constructor(
    private val context: Context,
    private val grammarList: ArrayList<Grammar>
) : RecyclerView.Adapter<GrammarCategoryAdapter.GrammarCategoryViewHolder>() {

    var onItemClick: ((Grammar) -> Unit)? = null

    override fun onBindViewHolder(holder: GrammarCategoryViewHolder, position: Int) {

        val dictionary = grammarList[position]
        holder.titleTV.text = dictionary.word
        if (dictionary.excerpt != null)
            holder.descriptionTV.text = Html.fromHtml(dictionary.excerpt, 0).trim()
        if (holder.descriptionTV.text.isEmpty())
            holder.descriptionTV.setText("N/A")

        if (position % 3 == 0) {
            holder.dictionaryCV.setBackgroundColor(context.getColor(R.color.ubt_blue))
        } else if ((position + 1) % 3 == 0) {
            holder.dictionaryCV.setBackgroundColor(context.getColor(R.color.ubt_pink))
        } else if ((position - 1) % 3 == 0) {
            holder.dictionaryCV.setBackgroundColor(context.getColor(R.color.ubt_yellow))
        }
    }

    inner class GrammarCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: AppCompatTextView = itemView.findViewById(R.id.txt_discovery_title)
        val descriptionTV: AppCompatTextView = itemView.findViewById(R.id.txt_discovery_subtitle)
        val dictionaryCV: CardView = itemView.findViewById(R.id.dictionaryListCard)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(grammarList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarCategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_dictionary_search,
            parent, false
        )
        return GrammarCategoryViewHolder(itemView)
    }

    fun removeGrammarList(id: Int) {
        for (item in grammarList) {
            if (item.id == id) {
                grammarList.remove(item)
                notifyDataSetChanged()
                break
            }
        }
    }

    fun addGrammarList(grammar: Grammar) {
        grammarList.add(grammar)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return grammarList.size
    }
}