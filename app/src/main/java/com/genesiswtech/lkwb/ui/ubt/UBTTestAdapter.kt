package com.genesiswtech.lkwb.ui.ubt

import android.content.Context
import android.graphics.Paint
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListUbtTestBinding
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestDataResponse
import javax.inject.Inject


class UBTTestAdapter @Inject constructor(
    private var ubtTestList: ArrayList<UBTTestDataResponse>,
    private val context: Context
) : RecyclerView.Adapter<UBTTestAdapter.UBTTestViewHolder>() {
    var packageType: Boolean? = false

    constructor(
        ubtTestList: ArrayList<UBTTestDataResponse>,
        context: Context, packageType: Boolean
    ) : this(ubtTestList, context) {
        this.packageType = packageType
    }

    private var ubtListBinding: ListUbtTestBinding? = null

    var onItemClick: ((UBTTestDataResponse) -> Unit)? = null

    val numbers = IntArray(ubtTestList.size) { it }

    override fun onBindViewHolder(holder: UBTTestViewHolder, position: Int) {
        val ubt = ubtTestList[position]
        holder.ubtListBinding.ubtTitleTV.text = ubt.title
        if (ubt.description != null) {
            holder.ubtListBinding.ubtDescriptionTV.text =
                Html.fromHtml(ubt.description, 0).trim()
            holder.ubtListBinding.ubtDescriptionTV.visibility = View.VISIBLE
        } else
            holder.ubtListBinding.ubtDescriptionTV.visibility = View.GONE
        if (packageType == true) {
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            params.setMargins(0, 15, 0, 0)
            holder.ubtListBinding.ubtTitleTV.layoutParams = params
            holder.ubtListBinding.ubtPriceTV.visibility = View.GONE
            holder.ubtListBinding.ubtPriceTagTV.visibility = View.GONE
            holder.ubtListBinding.ubtBasePriceTV.visibility = View.GONE
            holder.ubtListBinding.ubtCountTV.text = context.getString(R.string.test_now)
            holder.ubtListBinding.ubtCountTV.setBackgroundDrawable(context.getDrawable(R.drawable.ubt_button_green_background))
        } else {
            holder.ubtListBinding.ubtPriceTV.text = ubt.price.toString()+" "
            holder.ubtListBinding.ubtBasePriceTV.text = ubt.base_price.toString()
            holder.ubtListBinding.ubtBasePriceTV.paintFlags =
                holder.ubtListBinding.ubtBasePriceTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            if (ubt.canAccessSet == true) {
                holder.ubtListBinding.ubtPriceTagTV.text =
                    context.getString(R.string.free_for_premium_user)
                holder.ubtListBinding.ubtPriceTV.visibility = View.GONE
                holder.ubtListBinding.ubtBasePriceTV.visibility = View.GONE
                if (ubt.status == true) {
                    holder.ubtListBinding.ubtCountTV.text = context.getString(R.string.test_now)
                    holder.ubtListBinding.ubtCountTV.setBackgroundDrawable(context.getDrawable(R.drawable.ubt_button_green_background))
                } else
                    holder.ubtListBinding.ubtCountTV.text =
                        context.getString(R.string.claim_now)
            } else {
                if (ubt.price == 0) {
                    holder.ubtListBinding.ubtBasePriceTV.visibility = View.GONE
                    holder.ubtListBinding.ubtPriceTV.visibility = View.GONE
                    holder.ubtListBinding.ubtPriceTagTV.text = context.getString(R.string.free)
                    if (ubt.status == true) {
                        holder.ubtListBinding.newTV.visibility = View.GONE
                        holder.ubtListBinding.ubtCountTV.text = context.getString(R.string.test_now)
                        holder.ubtListBinding.ubtCountTV.setBackgroundDrawable(context.getDrawable(R.drawable.ubt_button_green_background))
                    } else {
                        holder.ubtListBinding.ubtCountTV.text =
                            context.getString(R.string.claim_now)
                        if (ubt.isNew == true)
                            holder.ubtListBinding.newTV.visibility = View.VISIBLE
                        else
                            holder.ubtListBinding.newTV.visibility = View.GONE
                    }
                } else
                    if (ubt.status == false) {
                        holder.ubtListBinding.ubtCountTV.text = "   "+context.getString(R.string.buy_now)+"   "
                        if (ubt.isNew == true)
                            holder.ubtListBinding.newTV.visibility = View.VISIBLE
                        else
                            holder.ubtListBinding.newTV.visibility = View.GONE
                    } else {
                        holder.ubtListBinding.newTV.visibility = View.GONE
                        holder.ubtListBinding.ubtCountTV.setBackgroundDrawable(context.getDrawable(R.drawable.ubt_button_green_background))
                        holder.ubtListBinding.ubtCountTV.text = context.getString(R.string.test_now)
                    }
            }


        }

        if (position % 3 == 0) {
            holder.ubtListBinding.ubtRL.setBackgroundColor(context.getColor(R.color.ubt_blue))
        } else if ((position + 1) % 3 == 0) {
            holder.ubtListBinding.ubtRL.setBackgroundColor(context.getColor(R.color.ubt_pink))
        } else if ((position - 1) % 3 == 0) {
            holder.ubtListBinding.ubtRL.setBackgroundColor(context.getColor(R.color.ubt_yellow))
        }
    }

    inner class UBTTestViewHolder(val ubtListBinding: ListUbtTestBinding) :
        RecyclerView.ViewHolder(ubtListBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(ubtTestList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UBTTestViewHolder {
        ubtListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_ubt_test,
                parent,
                false
            )
        return UBTTestViewHolder(ubtListBinding!!)
    }

    override fun getItemCount(): Int {
        return ubtTestList.size
    }

    fun updateList(ubtList: ArrayList<UBTTestDataResponse>) {
        ubtTestList.addAll(ubtList)
        notifyDataSetChanged()
    }

    fun clearList() {
        ubtTestList.clear()
        notifyDataSetChanged()
    }
}