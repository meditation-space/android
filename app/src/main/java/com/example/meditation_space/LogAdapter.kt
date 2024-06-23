package com.example.meditation_space

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(val context: Context, val logList: List<Log>) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logTime: TextView = view.findViewById(R.id.logTime)
        val logStatus: TextView = view.findViewById(R.id.logStatus)
        val logDate: TextView = view.findViewById(R.id.logDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.loglist_item, parent, false)
        val viewHolder = ViewHolder(view)
//        viewHolder.itemView.setOnClickListener {
//            val intent = Intent(parent.context, FriendChatActivity::class.java)
//            parent.context.startActivity(intent)
//        }
        return viewHolder
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = logList[position]
        holder.logTime.text = log.time
        if (log.status == "禅定成功") {
            holder.logStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.logStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.logStatus.text = log.status
        holder.logDate.text = log.data
    }

    override fun getItemCount() = logList.size
}