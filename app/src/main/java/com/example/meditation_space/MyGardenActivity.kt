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

class MyGardenActivity : BaseActivity() {
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var graden_close: ImageView? = null
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var garden_raindrop_view: TextView? = null
    private var swipeRefresh:androidx.swiperefreshlayout.widget.SwipeRefreshLayout?=null

    var adapter:MyFlowerAdapter?=null
    val FlowerList = ArrayList<MyFlowers>()//我的花卉列表
    lateinit var receiver: update2Receiver //广播接收器。用于接受修改当前界面的雨滴的广播

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wyr_my_garden)

        init()

        setSupportActionBar(toolbar)
        graden_close?.setOnClickListener {
            finish()
        }
        swipeRefresh?.setOnRefreshListener {
            refreshFruits(adapter)
        }
    }

    private fun refreshFruits(adapter: MyFlowerAdapter?) {
        thread {
            FlowerList.clear()
            val conn = Login().getConnection()
            conn?.let {
                val pstmt =
                    it.prepareStatement("select user_id,flower_id,culture_status,flower.name,flower.total_raindrop from userculture,flower where flower.id=userculture.flower_id and user_id=?;")
                pstmt.setInt(1, ActivityCollector.getUser_id())
                val rs = pstmt.executeQuery()
                while (rs.next()) {
                    val id = rs.getInt("flower_id")
                    val status = rs.getInt("culture_status")
                    val name = rs.getString("name")
                    val total_raindrop = rs.getInt("total_raindrop")
                    FlowerList.add(MyFlowers(id, name, total_raindrop, status))
                }
                rs.close()
                pstmt.close()
                it.close()
            }
            runOnUiThread {
                adapter?.notifyDataSetChanged()
                swipeRefresh?.isRefreshing = false
            }
        }
    }

    private fun init() {
        toolbar = findViewById(R.id.garden_toobal)
        graden_close = findViewById(R.id.graden_close)
        recyclerView = findViewById(R.id.garden_recyclerView)
        swipeRefresh=findViewById(R.id.swipeRefresh)
        garden_raindrop_view=findViewById(R.id.garden_raindrop_view)

        garden_raindrop_view?.text = ActivityCollector.getRainDrop().toString()

        thread {
            FlowerList.clear()
            val conn = Login().getConnection()
            conn?.let {
                val pstmt =
                    it.prepareStatement("select user_id,flower_id,culture_status,flower.name,flower.total_raindrop from userculture,flower where flower.id=userculture.flower_id and user_id=?;")
                pstmt.setInt(1, ActivityCollector.getUser_id())
                val rs = pstmt.executeQuery()
                while (rs.next()) {
                    val id = rs.getInt("flower_id")
                    val status = rs.getInt("culture_status")
                    val name = rs.getString("name")
                    val total_raindrop = rs.getInt("total_raindrop")
                    FlowerList.add(MyFlowers(id, name, total_raindrop, status))
                }
                rs.close()
                pstmt.close()
                it.close()
            }
            runOnUiThread {
                recyclerView?.let {
                    val layoutManager = GridLayoutManager(this, 2)
                    it.layoutManager = layoutManager
                    adapter = MyFlowerAdapter(this, FlowerList)
                    it.adapter = adapter
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.wyr_timegarden.GARDENVIEW_UPDATE_RAINDROP")//过滤器
        receiver = update2Receiver()
        //第一个参数是广播接收器BroadcastReceiver，第二个参数是过滤器。
        registerReceiver(receiver, intentFilter)//动态注册接收器
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)//注销接收器
    }


    inner class update2Receiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            p0?.let {
                garden_raindrop_view?.text = ActivityCollector.getRainDrop().toString()
            }
        }
    }
}