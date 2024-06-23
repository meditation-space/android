package com.example.meditation_space

import java.text.SimpleDateFormat
import java.util.Date
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

class FlowerAdapter(val context: Context, val flowerList: List<Flower>) :
    RecyclerView.Adapter<FlowerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flowerImage: ImageView = view.findViewById(R.id.flowerImage)
        val flowerName: TextView = view.findViewById(R.id.flowerName)
        val flowerPrice: TextView = view.findViewById(R.id.flowerPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flower_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val flower = flowerList[position]
            CustomDialog(context, "购买种子", "确定购买${flower.name}种子吗", "我在想想", "确认购买") {
                if (ActivityCollector.getSunLight() >= flower.price.toInt()) {
                    val currentDate = Date()
                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val formattedDate = formatter.format(currentDate)
                    thread {
                        try {
                            val conn = Login().getConnection()
                            conn?.let {
                                val pstmt =
                                    conn.prepareStatement("insert into userbuy values(?,?,?)")
                                pstmt.setInt(1, ActivityCollector.getUser_id())
                                pstmt.setInt(2, flower.id)
                                pstmt.setString(3, formattedDate)
                                pstmt.executeUpdate()
                                pstmt.close()
                                ActivityCollector.setSunLight(ActivityCollector.getSunLight() - flower.price.toInt())
                                val pstmt2 =
                                    conn.prepareStatement("insert into userculture values(?,?,?)")
                                pstmt2.setInt(1, ActivityCollector.getUser_id())
                                pstmt2.setInt(2, flower.id)
                                pstmt2.setInt(3, 0)
                                pstmt2.executeUpdate()
                                pstmt2.close()
                                val pstmt3 =
                                    conn.prepareStatement("update user set sunlight=? where id=?")
                                pstmt3.setInt(1, ActivityCollector.getSunLight())
                                pstmt3.setInt(2, ActivityCollector.getUser_id())
                                pstmt3.executeUpdate()
                                pstmt3.close()
                                it.close()
                            }
                            val intent =
                                Intent("com.example.wyr_timegarden.SHOPVIEW_UPDATE_SUNLIGHT")
                            parent.context.sendBroadcast(intent)//通知shop界面上的数据进行修改
                            (parent.context as ShopActivity).runOnUiThread {
                                Toast.makeText(parent.context, "购买成功", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException) {
                            (parent.context as ShopActivity).runOnUiThread {
                                Toast.makeText(
                                    parent.context,
                                    "你已经购买过此种子，无法继续购买",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(parent.context, "阳光不足", Toast.LENGTH_SHORT).show()
                }
            }.show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flower = flowerList[position]
        holder.flowerName.text = flower.name
        holder.flowerPrice.text = flower.price + "阳光"
        Glide.with(context).load(R.drawable.seed)
            .into(holder.flowerImage)//由于图片是ai生成的，自己ps的，速度比较慢，所以种子图片都统一成一张一样的了
    }

    override fun getItemCount() = flowerList.size
}