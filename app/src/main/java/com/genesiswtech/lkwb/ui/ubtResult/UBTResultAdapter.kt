package com.genesiswtech.lkwb.ui.ubtResult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.data.TotalQuestion
import com.genesiswtech.lkwb.utils.LKWBConstants
import javax.inject.Inject

class UBTResultAdapter @Inject constructor(
    private val activity:AppCompatActivity,
    private val start:Int,
    private val totalQuestionList: ArrayList<TotalQuestion>
) : RecyclerView.Adapter<UBTResultAdapter.UBTTotalQuestionViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null



    override fun onBindViewHolder(holder: UBTTotalQuestionViewHolder, position: Int) {
        val width: Int = activity.resources.displayMetrics.widthPixels
        val blog = totalQuestionList[position].questionStatus
        val pos: Int = start+position + 1
        holder.numberFrame.layoutParams = LinearLayout.LayoutParams(width/7, width/7)
        if (blog == LKWBConstants.UNSOLVED) {
            holder.unsolvedTV.text = pos.toString()
            holder.unsolvedTV.visibility = View.VISIBLE
        } else if (blog == LKWBConstants.WRONG) {
            holder.wrongTV.text = pos.toString()
            holder.wrongTV.visibility = View.VISIBLE
        } else if (blog == LKWBConstants.CORRECT) {
            holder.correctTV.text = pos.toString()
            holder.correctTV.visibility = View.VISIBLE

        }
    }

    inner class UBTTotalQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val correctTV: AppCompatTextView = itemView.findViewById(R.id.questionCorrectNumberTV)
        val wrongTV: AppCompatTextView = itemView.findViewById(R.id.questionWrongNumberTV)
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
        return totalQuestionList.size
    }
}

