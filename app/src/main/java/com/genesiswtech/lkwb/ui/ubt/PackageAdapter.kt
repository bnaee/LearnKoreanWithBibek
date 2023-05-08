package com.genesiswtech.lkwb.ui.ubt

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListPackageBinding
import com.genesiswtech.lkwb.ui.ubt.model.PackageDataResponse
import javax.inject.Inject

class PackageAdapter @Inject constructor(
    private var packageList: ArrayList<PackageDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    private var packageBinding: ListPackageBinding? = null

    var onItemClick: ((PackageDataResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val packages = packageList[position]
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels
        holder.packageBinding.packages.layoutParams.width = width / 3
        holder.packageBinding.packages.layoutParams.height = height / 5
        holder.packageBinding.packages.requestLayout()
        holder.packageBinding.packageSetBtn.text =
            packages.sets.size.toString() + " " + context.getString(R.string.sets)
        holder.packageBinding.packageTV.text = packages.category
        holder.packageBinding.packagePriceTV.text = "Rs. " + packages.price.toString()
        holder.packageBinding.packagePriceCutTV.text = packages.basePrice.toString()
        holder.packageBinding.packagePriceCutTV.showStrikeThrough(true)
        holder.packageBinding.packages.visibility = View.VISIBLE
        if (packages.status == true) {
            holder.packageBinding.packageBuyBtn.text = context.getString(R.string.view_package)
            holder.packageBinding.packageBuyBtn.setTextColor(context.resources.getColor(R.color.white))
            holder.packageBinding.packageBuyBtn.setBackgroundResource(R.drawable.button_green_background)
        }

        if (packages.category == context.getString(R.string.platinum))
            holder.packageBinding.packages.setBackgroundResource(R.drawable.gradient_platinum)
        if (packages.category == context.getString(R.string.gold))
            holder.packageBinding.packages.setBackgroundResource(R.drawable.gradient_gold)
        if (packages.category == context.getString(R.string.silver))
            holder.packageBinding.packages.setBackgroundResource(R.drawable.gradient_silver)
    }

    inner class PackageViewHolder(val packageBinding: ListPackageBinding) :
        RecyclerView.ViewHolder(packageBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(packageList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        packageBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_package,
                parent,
                false
            )
        return PackageViewHolder(packageBinding!!)
    }

    override fun getItemCount(): Int {
        return packageList.size
    }

    fun updateList(ubtList: ArrayList<PackageDataResponse>) {
        packageList.addAll(ubtList)
        notifyDataSetChanged()
    }

    fun clearList() {
        packageList.clear()
        notifyDataSetChanged()
    }

    fun AppCompatTextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}