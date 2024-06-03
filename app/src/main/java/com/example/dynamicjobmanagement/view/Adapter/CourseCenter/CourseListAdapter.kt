package com.example.dynamicjobmanagement.view.Adapter.CourseCenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository

class CourseListAdapter(private val clickListener: OnCourseClickListener) : RecyclerView.Adapter<CourseListAdapter.ViewHolder>() {
    private var courseList = listOf<Course>()

    interface OnCourseClickListener {
        fun onCourseClick(course: Course)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseNameTV: TextView = itemView.findViewById(R.id.course_1stInformation_TextView)
        private val courseInfoTV: TextView = itemView.findViewById(R.id.course_2ndInformation_TextView)

        fun bind(course: Course, clickListener: OnCourseClickListener) {
            courseNameTV.text = course.courseName
            if(UserRepository.getUserType()==UserType.STUDENT)
                courseInfoTV.text = course.teacherName
            else
                courseInfoTV.text = course.courseTime

            itemView.setOnClickListener {
                clickListener.onCourseClick(course)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(courseList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    fun setData(newData: List<Course>) {
        val diffCallback = CourseDiffCallback(courseList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        courseList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class CourseDiffCallback(
        private val oldList: List<Course>,
        private val newList: List<Course>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].courseName == newList[newItemPosition].courseName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}