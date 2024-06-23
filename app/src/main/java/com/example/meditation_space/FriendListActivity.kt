package com.example.meditation_space

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import BaseActivity

class FriendListActivity : BaseActivity() {
    private val chatList = ArrayList<Chat>()
    private var fclose: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        fclose = findViewById(R.id.friendList_close)
        fclose?.setOnClickListener {
            finish()
        }

        initChat()

        val layoutManager = LinearLayoutManager(this)
        val recyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.friendList)
        recyclerView.layoutManager = layoutManager
        val adapter = ChatAdapter(chatList)
        recyclerView.adapter = adapter
    }

    private fun initChat() {
        repeat(1) {
            chatList.add(Chat(R.mipmap.user1, "迷途", "你好，在吗？"))
            chatList.add(Chat(R.mipmap.user2, "风清扬", "Hello"))
            chatList.add(Chat(R.mipmap.user3, "初醒", "Hi"))
            chatList.add(Chat(R.mipmap.user4, "蜀道难", "OK"))
            chatList.add(Chat(R.mipmap.user5, "难于上青天", "OK"))
        }
    }
}