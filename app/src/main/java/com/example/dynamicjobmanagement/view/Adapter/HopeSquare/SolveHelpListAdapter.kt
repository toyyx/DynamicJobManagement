package com.example.dynamicjobmanagement.view.Adapter.HopeSquare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import java.time.LocalDateTime

class SolveHelpListAdapter(val clickerLister: OnHelpOperationClickListener): RecyclerView.Adapter<SolveHelpListAdapter.ViewHolder>() {
    private var solveHelpList = listOf<SolveHelp>()

    interface OnHelpOperationClickListener {
        fun onOperationClick(view:View, solveHelp: SolveHelp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.solve_help_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name_TV: TextView = itemView.findViewById(R.id.solveHelpList_replierName_TextView)
        private val time_TV: TextView = itemView.findViewById(R.id.solveHelpList_replyTime_TextView)
        private val content_TV: TextView = itemView.findViewById(R.id.solveHelpList_replyContent_TextView)
        private val operation_IB: ImageButton = itemView.findViewById(R.id.solveHelpList_operation_ImageButton)

        fun bind(solveHelp: SolveHelp, clickListener: OnHelpOperationClickListener) {
            name_TV.text = solveHelp.replierName

            val publishTime=solveHelp.replyTime
            val nowDateTime= LocalDateTime.now()
            if(publishTime.year==nowDateTime.year&&publishTime.month==nowDateTime.month){
                when(publishTime.dayOfMonth-nowDateTime.dayOfMonth){
                    0 -> time_TV.text = "今天 ${publishTime.toLocalTime()}"
                    -1 -> time_TV.text = "昨天 ${publishTime.toLocalTime()}"
                    -2 -> time_TV.text = "前天 ${publishTime.toLocalTime()}"
                    else ->time_TV.text = publishTime.toString()
                }
            }else
                time_TV.text = publishTime.toString()

            content_TV.text = solveHelp.replyContent

            if(UserRepository.getUserType()==UserType.STUDENT){
                operation_IB.visibility=View.GONE
            }else{
                operation_IB.visibility=View.VISIBLE
                operation_IB.setOnClickListener {
                    clickListener.onOperationClick(it, solveHelp)
                }
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(solveHelpList[position],clickerLister)
    }

    override fun getItemCount(): Int {
        return solveHelpList.size
    }

    fun setData(newData: List<SolveHelp>) {
        val diffCallback = SolveHelpDiffCallback(solveHelpList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        solveHelpList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class SolveHelpDiffCallback(
        private val oldList: List<SolveHelp>,
        private val newList: List<SolveHelp>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].solveId == newList[newItemPosition].solveId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}