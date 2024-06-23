package com.example.meditation_space

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class RankAdapter(val context: Context, val rankList: List<Rank>) :
    RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rank: TextView = view.findViewById(R.id.rankNum)
        val rank_image: ImageView = view.findViewById(R.id.rank_image)
        val nickname: TextView = view.findViewById(R.id.nickname)
        val totalTime: TextView = view.findViewById(R.id.totalTime)
        val jifen: TextView = view.findViewById(R.id.jifen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rank_item, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ranks = rankList[position]
        holder.rank.text = ranks.rank
        if (ranks.rank=="1"){
            Glide.with(context).load(R.drawable.jin)
                .into(holder.rank_image)
        }else if (ranks.rank=="2") {
            Glide.with(context).load(R.drawable.yin)
                .into(holder.rank_image)
        }else if (ranks.rank=="3"){
            Glide.with(context).load(R.drawable.tong)
                .into(holder.rank_image)
        }else{
            holder.rank_image.visibility=View.GONE
        }
        holder.nickname.text = ranks.nickname
        holder.totalTime.text = ranks.totalTime
        holder.jifen.text = ranks.jifen
    }

    override fun getItemCount() = rankList.size
}