package com.example.meditation_space

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import BaseActivity

class LogActivity : BaseActivity() {
    private var log_close: ImageView? = null
    private val logList = ArrayList<Log>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loglist)

        log_close = findViewById(R.id.log_close)

        log_close?.setOnClickListener {
            finish()
        }

        initLog()

        val layoutManager = LinearLayoutManager(this)
        val recyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.loglist_recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = LogAdapter(this,logList)
        recyclerView.adapter = adapter
    }

    private fun initLog() {
        repeat(1) {
            logList.add(Log("10:00", "禅定成功", "2024-06-11 19:00:10"))
            logList.add(Log("20:00", "禅定成功", "2024-06-10 19:45:10"))
            logList.add(Log("20:00", "禅定成功", "2024-06-09 19:24:22"))
            logList.add(Log("10:00", "禅定成功", "2024-06-08 18:33:57"))
            logList.add(Log("20:00", "禅定失败", "2024-06-07 17:43:36"))

            logList.add(Log("20:00", "禅定成功", "2024-05-28 21:45:10"))
            logList.add(Log("20:00", "禅定成功", "2024-05-28 21:24:02"))
            logList.add(Log("10:00", "禅定成功", "2024-05-28 20:43:17"))
            logList.add(Log("20:00", "禅定失败", "2024-05-28 20:43:36"))

            logList.add(Log("20:00", "禅定成功", "2024-05-27 21:45:10"))
            logList.add(Log("20:00", "禅定成功", "2024-05-26 21:24:02"))
            logList.add(Log("10:00", "禅定成功", "2024-05-25 20:43:17"))
            logList.add(Log("20:00", "禅定失败", "2024-05-24 20:43:36"))

            logList.add(Log("20:00", "禅定成功", "2024-04-23 21:45:10"))
            logList.add(Log("20:00", "禅定成功", "2024-04-23 21:24:02"))
            logList.add(Log("10:00", "禅定成功", "2024-04-23 20:43:17"))
            logList.add(Log("20:00", "禅定失败", "2024-04-23 20:43:36"))

            logList.add(Log("20:00", "禅定成功", "2024-04-22 21:45:10"))
            logList.add(Log("20:00", "禅定成功", "2024-04-22 21:24:02"))
            logList.add(Log("10:00", "禅定成功", "2024-04-22 20:43:17"))
            logList.add(Log("20:00", "禅定失败", "2024-04-22 20:43:36"))
        }
    }
}