package com.example.dynamicjobmanagement.view.Adapter.CourseCenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R

class CourseMemberAdapter: RecyclerView.Adapter<CourseMemberAdapter.ViewHolder>() {
    private var memberList = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_member_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val memberNameTV: TextView = itemView.findViewById(R.id.courseMember_name_TextView)

        fun bind(name: String) {
            memberNameTV.text = name
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memberList[position])
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    fun setData(newData: List<String>) {
        val diffCallback = CourseMemberDiffCallback(memberList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        memberList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class CourseMemberDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}