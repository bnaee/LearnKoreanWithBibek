package com.genesiswtech.lkwb.ui.changeLanguage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListChangeLanguageBinding
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import javax.inject.Inject

class ChangeLanguageAdapter @Inject constructor(
    private val languageList: ArrayList<String>
) : RecyclerView.Adapter<ChangeLanguageAdapter.ChangeLanguageViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    private var languageChangeBinding: ListChangeLanguageBinding? = null

    override fun onBindViewHolder(holder: ChangeLanguageViewHolder, position: Int) {
        holder.languageChangeBinding.titleTV.text = languageList[position]
        if (position == 0)
            holder.languageChangeBinding.titleTV.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_uk,
                0,
                0,
                0
            )
        if (position == 1)
            holder.languageChangeBinding.titleTV.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_korea,
                0,
                0,
                0
            )
        Log.d("TAG", LKWBPreferencesManager.readLanguageCode() + position)
        if (LKWBPreferencesManager.readLanguageCode() == "en" && position == 0)
            holder.languageChangeBinding.selectedIV.visibility = View.VISIBLE

        if (LKWBPreferencesManager.readLanguageCode() == "ko" && position == 1)
            holder.languageChangeBinding.selectedIV.visibility = View.VISIBLE

    }

    inner class ChangeLanguageViewHolder(val languageChangeBinding: ListChangeLanguageBinding) :
        RecyclerView.ViewHolder(languageChangeBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(languageList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeLanguageViewHolder {
        languageChangeBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_change_language,
                parent,
                false
            )
        return ChangeLanguageViewHolder(languageChangeBinding!!)
    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}
