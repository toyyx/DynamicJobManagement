package com.example.dynamicjobmanagement.view.Adapter.CourseCenter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Job
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CourseJobAdapter(private val clickListener: OnJobClickListener): RecyclerView.Adapter<CourseJobAdapter.ViewHolder>() {
    private var jobList = listOf<Job>()

    interface OnJobClickListener {
        fun onStudentJobClick(job: Job)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_job_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTV: TextView = itemView.findViewById(R.id.courseJob_title_TextView)
        private val endOrFinTV: TextView = itemView.findViewById(R.id.courseJob_endTime_or_finished_TextView)

        fun bind(job:Job, clickListener: OnJobClickListener) {
            titleTV.text = job.jobTitle
            if(job.endTime.isAfter(LocalDateTime.now())){
                titleTV.setTextColor(Color.BLACK)
                endOrFinTV.text = "截止时间:${job.endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}"
                endOrFinTV.setTextColor(Color.BLACK)
            }else{
                titleTV.setTextColor(Color.GRAY)
                endOrFinTV.text="已截止"
                endOrFinTV.setTextColor(Color.GRAY)
            }
            itemView.setOnClickListener {
                clickListener.onStudentJobClick(job)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    fun setData(newData: List<Job>) {
        val diffCallback = CourseJobDiffCallback(jobList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        jobList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class CourseJobDiffCallback(
        private val oldList: List<Job>,
        private val newList: List<Job>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].jobId == newList[newItemPosition].jobId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}