package com.example.dynamicjobmanagement.view.Adapter.PersonCenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.User

class SwitchAccountAdapter(private val clickListener: OnAccountClickListener): RecyclerView.Adapter<SwitchAccountAdapter.ViewHolder>() {
    private var userList = listOf<User>()

    interface OnAccountClickListener {
        fun onAccountClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.switch_account_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name_TV: TextView = itemView.findViewById(R.id.switchAccount_userName_TextView)
        private val account_TV: TextView = itemView.findViewById(R.id.switchAccount_account_TextView)

        fun bind(user: User, clickListener: OnAccountClickListener) {
            name_TV.text = user.name
            account_TV.text = user.account

            itemView.setOnClickListener {
                clickListener.onAccountClick(user)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(newData: List<User>) {
        val diffCallback = SwitchAccountDiffCallback(userList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class SwitchAccountDiffCallback(
        private val oldList: List<User>,
        private val newList: List<User>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].account == newList[newItemPosition].account
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}