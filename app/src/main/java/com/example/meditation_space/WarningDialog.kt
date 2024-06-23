package com.example.meditation_space

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView

@SuppressLint("MissingInflatedId")
class WarningDialog(
    context: Context,
    val title: String,
    val message: String,
    val fun1: () -> Unit
) : Dialog(context) {

    init {
        // 设置Dialog的样式和内容
        setContentView(R.layout.warn_dialog_layout)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)

        // 获取Dialog中的控件
        val titleTextView = findViewById<TextView>(R.id.dialog_title)
        val messageTextView = findViewById<TextView>(R.id.dialog_message)
        val okButton = findViewById<Button>(R.id.dialog_wakaru_button)

        // 设置控件的初始值
        titleTextView.text = title
        messageTextView.text = message

        // 设置按钮的点击事件
        okButton.setOnClickListener {
            fun1()
            dismiss()
        }
    }
}