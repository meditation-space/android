package com.example.meditation_space

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager

class RankingActivity : AppCompatActivity() {
    private var rank_close: ImageView? = null
    private var rankList = ArrayList<Rank>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        rank_close = findViewById(R.id.rank_close)
        rank_close?.setOnClickListener {
            finish()
        }

        initRank()

        val layoutManager = LinearLayoutManager(this)
        val recyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rank_recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = RankAdapter(this  ,rankList)
        recyclerView.adapter = adapter
    }

    private fun initRank() {
        repeat(1) {
            rankList.add(Rank("wyrzxk","1","总禅定:10h20min","850"))
            rankList.add(Rank("迷途","2","总禅定:2h10min","200"))
            rankList.add(Rank("风清扬","3","总禅定:1h40min","150"))
            rankList.add(Rank("初醒","4","总禅定:1h30min","100"))
            rankList.add(Rank("蜀道难","5","总禅定:1h20min","50"))
            rankList.add(Rank("难于上青天","6","总禅定:1h30min","0"))
        }
    }
}