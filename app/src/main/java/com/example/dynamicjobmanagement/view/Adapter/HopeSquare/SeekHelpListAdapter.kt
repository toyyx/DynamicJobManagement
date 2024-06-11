package com.example.dynamicjobmanagement.view.Adapter.HopeSquare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.SeekHelp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SeekHelpListAdapter(private val clickListener: OnSeekHelpClickListener): RecyclerView.Adapter<SeekHelpListAdapter.ViewHolder>() {
    private var seekHelpList = listOf<SeekHelp>()

    interface OnSeekHelpClickListener {
        fun onSeekHelpClick(seekHelp: SeekHelp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.square_center_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coures_job_TV: TextView = itemView.findViewById(R.id.seekHelpList_course_job_TextView)
        private val time_TV: TextView = itemView.findViewById(R.id.seekHelpList_time_TextView)
        private val seekerName_TV: TextView = itemView.findViewById(R.id.seekHelpList_seekerName_TextView)
        private val content_TV: TextView = itemView.findViewById(R.id.seekHelpList_content_TextView)
        private val likeNum_TV: TextView = itemView.findViewById(R.id.seekHelpList_likeNum_TextView)
        private val comment_TV: TextView = itemView.findViewById(R.id.seekHelpList_commentNum_TextView)

        fun bind(seekHelp: SeekHelp, clickListener: OnSeekHelpClickListener) {
            coures_job_TV.text = "${seekHelp.courseName}-${seekHelp.jobTitle}"

            val publishTime=seekHelp.publishTime
            val nowDateTime=LocalDateTime.now()
            if(publishTime.year==nowDateTime.year&&publishTime.month==nowDateTime.month){
                when(publishTime.dayOfMonth-nowDateTime.dayOfMonth){
                    0 -> time_TV.text = "今天 ${publishTime.toLocalTime()}"
                    -1 -> time_TV.text = "昨天 ${publishTime.toLocalTime()}"
                    -2 -> time_TV.text = "前天 ${publishTime.toLocalTime()}"
                    else ->time_TV.text = publishTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                }
            }else
                time_TV.text = publishTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            seekerName_TV.text = seekHelp.seekerName
            content_TV.text = seekHelp.seekContent
            likeNum_TV.text = seekHelp.likeNumber.toString()
            comment_TV.text = seekHelp.commentNumber.toString()

            itemView.setOnClickListener {
                clickListener.onSeekHelpClick(seekHelp)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(seekHelpList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return seekHelpList.size
    }

    fun setData(newData: List<SeekHelp>) {
        val diffCallback = SeekHelpDiffCallback(seekHelpList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        seekHelpList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class SeekHelpDiffCallback(
        private val oldList: List<SeekHelp>,
        private val newList: List<SeekHelp>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].seekId == newList[newItemPosition].seekId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}