package com.example.dynamicjobmanagement.view.Adapter.HopeSquare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.SeekHelp

class MySquareSeekHelpListAdapter(private val clickListener: OnMySeekHelpClickListener) : RecyclerView.Adapter<MySquareSeekHelpListAdapter.ViewHolder>() {
    private var seekHelpList = listOf<SeekHelp>()

    interface OnMySeekHelpClickListener {
        fun onSeekHelpClick(seekHelp: SeekHelp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mysquare_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val course_job_TV: TextView = itemView.findViewById(R.id.mySquare_course_job_TextView)
        private val time_TV: TextView = itemView.findViewById(R.id.mySquare_pubnishTime_TextView)
        private val likeNum_TV: TextView = itemView.findViewById(R.id.mySquare_likeNum_TextView)
        private val commentNum_TV: TextView = itemView.findViewById(R.id.mySquare_commentNum_TextView)

        fun bind(seekHelp: SeekHelp, clickListener: OnMySeekHelpClickListener) {
            course_job_TV.text = "${seekHelp.courseName}-${seekHelp.jobTitle}"
            time_TV.text = seekHelp.publishTime.toString()
            likeNum_TV.text=seekHelp.likeNumber.toString()
            commentNum_TV.text=seekHelp.commentNumber.toString()

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
        val diffCallback = MySeekHelpDiffCallback(seekHelpList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        seekHelpList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class MySeekHelpDiffCallback(
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