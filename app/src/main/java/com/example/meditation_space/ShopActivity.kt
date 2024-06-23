package com.example.meditation_space

import BaseActivity
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import kotlin.concurrent.thread

class ShopActivity : BaseActivity() {
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var shop_close: ImageView? = null
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var shop_sunlight_view: TextView? = null

    val FlowerList = ArrayList<Flower>()//花种列表
    lateinit var receiver:updateReceiver //广播接收器。用于接受修改阳光的广播

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        init()

        setSupportActionBar(toolbar)
        shop_close?.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        toolbar = findViewById(R.id.shop_toobal)
        shop_close = findViewById(R.id.shop_close)
        recyclerView = findViewById(R.id.shop_recyclerView)
        shop_sunlight_view = findViewById(R.id.shop_sunlight_view)

        shop_sunlight_view?.text = ActivityCollector.getSunLight().toString()

        thread {
            FlowerList.clear()
            val conn = Login().getConnection()
            conn?.let {
                val pstmt = it.prepareStatement("select * from flower;")
                val rs = pstmt.executeQuery()
                while (rs.next()) {
                    val id = rs.getInt("id")
                    val price = rs.getInt("price").toString()
                    val name = rs.getString("name")
                    FlowerList.add(Flower(id, name, price))
                }
                rs.close()
                pstmt.close()
                it.close()
            }
            runOnUiThread {
                recyclerView?.let {
                    val layoutManager = GridLayoutManager(this, 2)
                    it.layoutManager = layoutManager
                    val adapter = FlowerAdapter(this, FlowerList)
                    it.adapter = adapter
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.wyr_timegarden.SHOPVIEW_UPDATE_SUNLIGHT")//过滤器
        receiver = updateReceiver()
        //第一个参数是广播接收器BroadcastReceiver，第二个参数是过滤器。
        registerReceiver(receiver,intentFilter)//动态注册接收器
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)//注销接收器
    }


    inner class updateReceiver: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            p0?.let {
                shop_sunlight_view?.text=ActivityCollector.getSunLight().toString()
            }
        }
    }
}