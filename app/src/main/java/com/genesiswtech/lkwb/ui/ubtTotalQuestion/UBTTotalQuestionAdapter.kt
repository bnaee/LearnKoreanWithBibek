package com.genesiswtech.lkwb.ui.ubtTotalQuestion

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import javax.inject.Inject


class UBTTotalQuestionAdapter @Inject constructor(
    private val activity: AppCompatActivity,
    private val start: Int,
    private val dataList: HashMap<Int, Int>
) : RecyclerView.Adapter<UBTTotalQuestionAdapter.UBTTotalQuestionViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: UBTTotalQuestionViewHolder, position: Int) {

        val width: Int = activity.resources.displayMetrics.heightPixels
        holder.numberFrame.layoutParams = LinearLayout.LayoutParams(width/7, width/7)
        val blog = dataList[position]
        val pos: Int = start + position + 1
        holder.unsolvedTV.text = pos.toString()
        holder.unsolvedTV.visibility = View.VISIBLE

        if (blog == 5)
            holder.unsolvedTV.setBackgroundResource(R.drawable.unsolved_question_background)
        else if (blog == 0 || blog == 1 || blog == 2 || blog == 3)
            holder.unsolvedTV.setBackgroundResource(R.drawable.attempted_question_background)

    }

    inner class UBTTotalQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val unsolvedTV: AppCompatTextView = itemView.findViewById(R.id.questionUnsolvedNumberTV)
        val numberFrame: FrameLayout = itemView.findViewById(R.id.numberFL)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition + 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UBTTotalQuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_ubt_total_question,
            parent, false
        )
        return UBTTotalQuestionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}