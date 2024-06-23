package com.example.meditation_space

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.concurrent.thread

class MyFlowerAdapter(val context: Context, val flowerList: List<MyFlowers>) :
    RecyclerView.Adapter<MyFlowerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val myFlowerImage: ImageView = view.findViewById(R.id.myFlowerImage)
        val myFlowerName: TextView = view.findViewById(R.id.myFlowerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_flower_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val flower = flowerList[position]
            if (flower.culture_status == 0) {
                CustomDialog(context, "培养幼苗", "确定为${flower.name}幼苗浇水吗", "我在想想", "我要浇水") {
                    if (ActivityCollector.getRainDrop() >= flower.total_raindrop) {
                        thread {
                            try {
                                val conn = Login().getConnection()
                                conn?.let {
                                    val pstmt =
                                        conn.prepareStatement("update userculture set culture_status=1 where user_id=? and flower_id=?;")
                                    pstmt.setInt(1, ActivityCollector.getUser_id())
                                    pstmt.setInt(2, flower.id)
                                    pstmt.executeUpdate()
                                    pstmt.close()
                                    ActivityCollector.setRainDrop(ActivityCollector.getRainDrop() - flower.total_raindrop)
                                    val pstmt2 =
                                        conn.prepareStatement("update user set raindrop=? where id=?")
                                    pstmt2.setInt(1, ActivityCollector.getRainDrop())
                                    pstmt2.setInt(2, ActivityCollector.getUser_id())
                                    pstmt2.executeUpdate()
                                    pstmt2.close()
                                    it.close()
                                }
                                val intent =
                                    Intent("com.example.wyr_timegarden.GARDENVIEW_UPDATE_RAINDROP")
                                parent.context.sendBroadcast(intent)//通知my_garden界面上的数据进行修改
                                (parent.context as MyGardenActivity).runOnUiThread {
                                    Toast.makeText(parent.context, "浇水成功", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } catch (e: Exception) {
                                (parent.context as MyGardenActivity).runOnUiThread {
                                    Toast.makeText(
                                        parent.context,
                                        "异常操作",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(parent.context, "雨滴不足", Toast.LENGTH_SHORT).show()
                    }
                }.show()
            } else {
                Toast.makeText(parent.context, "花卉已经长大，无需浇水", Toast.LENGTH_SHORT).show()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flower = flowerList[position]
        holder.myFlowerName.text = flower.name
        if (flower.culture_status == 0) {//加载幼苗
            Glide.with(context).load(R.drawable.youmiao).into(holder.myFlowerImage)
            holder.myFlowerName.text = flower.name + "幼苗"
        } else {
            when (flower.id) {
                1 -> {
                    Glide.with(context).load(R.drawable.flower1).into(holder.myFlowerImage)
                }
                2 -> {
                    Glide.with(context).load(R.drawable.flower2).into(holder.myFlowerImage)
                }
                3 -> {
                    Glide.with(context).load(R.drawable.flower3).into(holder.myFlowerImage)
                }
                4 -> {
                    Glide.with(context).load(R.drawable.flower4).into(holder.myFlowerImage)
                }
                5 -> {
                    Glide.with(context).load(R.drawable.flower5).into(holder.myFlowerImage)
                }
                else -> {
                    //没有可加载的图片
                }
            }
        }

    }

    override fun getItemCount() = flowerList.size
}