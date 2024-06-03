package com.example.dynamicjobmanagement.view.Adapter.CourseCenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.model.model.Job

class CourseJobAdapter_Teacher(private var course : Course, private val clickListener: OnTeacherJobClickListener): RecyclerView.Adapter<CourseJobAdapter_Teacher.ViewHolder>() {
    private var jobList = listOf<Job>()

    interface OnTeacherJobClickListener {
        fun onTeacherJobClick(job: Job)
        fun onOperationButtonClick(view:View,job: Job)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_teacher_job_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title_TV: TextView = itemView.findViewById(R.id.courseJobForTeacher_title_TextView)
        private val endTime_TV: TextView = itemView.findViewById(R.id.courseJobForTeacher_endTime_TextView)
        private val commitNum_TV: TextView = itemView.findViewById(R.id.courseJobForTeacher_commitNum_TextView)
        private val memberNum_TV: TextView = itemView.findViewById(R.id.courseJobForTeacher_memberNum_TextView)
        private val operation_IB: TextView = itemView.findViewById(R.id.courseJobForTeacher_operation_ImageButton)

        fun bind(course:Course, job: Job, clickListener: OnTeacherJobClickListener) {
            title_TV.text = job.jobTitle
            endTime_TV.text = job.endTime.toString()
            commitNum_TV.text = job.commitNum.toString()
            memberNum_TV.text=course.studentList.size.toString()
            itemView.setOnClickListener {
                clickListener.onTeacherJobClick(job)
            }
            operation_IB.setOnClickListener {
                clickListener.onOperationButtonClick(it,job)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(course,jobList[position],clickListener)
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