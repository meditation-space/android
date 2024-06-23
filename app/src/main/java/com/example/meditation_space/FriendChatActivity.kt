package com.example.meditation_space

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FriendChatActivity : AppCompatActivity(), View.OnClickListener {
    private var chat_close: ImageView? = null
    private val msgList = ArrayList<Msg>()
    private var adapter: MsgAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_chat)

        chat_close = findViewById(R.id.chat_close)
        chat_close?.setOnClickListener {
            finish()
        }
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val customDialog = CustomDialog(
                this,
                "邀请好友",
                "是否邀请好友一起进入禅定空间",
                "我再想想",
                "发起邀请",
                {}
            )
            customDialog.show()
        }

        initMsg()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.chatList)
        recyclerView.layoutManager = layoutManager
        adapter = MsgAdapter(msgList)
        recyclerView.adapter = adapter
        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val send = findViewById<Button>(R.id.send)
        val sendMessage = findViewById<EditText>(R.id.senMessage)
        val recyclerView = findViewById<RecyclerView>(R.id.chatList)
        when (v) {
            send -> {
                val content = sendMessage.text.toString()
                if (content.isNotEmpty()) {
                    val msg = Msg(content, Msg.TYPE_SENT)
                    msgList.add(msg)
                    adapter?.notifyItemInserted(msgList.size - 1)
                    recyclerView.scrollToPosition(msgList.size - 1)
                    sendMessage.setText("")
                }
            }
        }
    }

    private fun initMsg() {
        val msg1 = Msg("Hello Admin", Msg.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = Msg("Hello", Msg.TYPE_SENT)
        msgList.add(msg2)
    }
}