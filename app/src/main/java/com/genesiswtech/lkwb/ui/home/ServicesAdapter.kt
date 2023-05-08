package com.genesiswtech.lkwb.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.ListServiceBinding
import com.genesiswtech.lkwb.ui.home.model.Services
import javax.inject.Inject

class ServicesAdapter @Inject constructor(
    private val serviceList: ArrayList<Services>,
    private val context: Context
) : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    private var serviceListBinding: ListServiceBinding? = null

    var onItemClick: ((Services) -> Unit)? = null

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        val width = context.resources.displayMetrics.widthPixels
//        val height = context.resources.displayMetrics.heightPixels
        holder.serviceListBinding.linear.layoutParams.width = width / 3-50
        holder.serviceListBinding.serviceNameTV.text = service.title!!
        Glide.with(context)
            .load(service.icon)
            .into(holder.serviceListBinding.serviceIV)
        if(position==0)
            holder.serviceListBinding.linear.setBackgroundResource(R.drawable.service_ubt_bg)
        if(position==1)
            holder.serviceListBinding.linear.setBackgroundResource(R.drawable.service_grammer_bg)
        if(position==2)
            holder.serviceListBinding.linear.setBackgroundResource(R.drawable.service_dictionary_bg)
        if(position==3)
            holder.serviceListBinding.linear.setBackgroundResource(R.drawable.service_discussion_bg)
        if(position==4)
            holder.serviceListBinding.linear.setBackgroundResource(R.drawable.service_blog_bg)
    }

    inner class ServiceViewHolder(val serviceListBinding: ListServiceBinding) :
        RecyclerView.ViewHolder(serviceListBinding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(serviceList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        serviceListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_service,
                parent,
                false
            )
        return ServiceViewHolder(serviceListBinding!!)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}