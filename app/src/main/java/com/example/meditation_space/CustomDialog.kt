package com.example.meditation_space

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView

//自定义一个dialog类，用于弹出开始收集雨水的提示
class CustomDialog(
    context: Context,
    val title: String,
    val message: String,
    val cancelText:String,
    val okText:String,
    val fun1: () -> Unit
) : Dialog(context) {

    init {
        // 设置Dialog的样式和内容
        setContentView(R.layout.custom_dialog_layout)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)

        // 获取Dialog中的控件
        val titleTextView = findViewById<TextView>(R.id.dialog_title)
        val messageTextView = findViewById<TextView>(R.id.dialog_message)
        val cancelButton = findViewById<Button>(R.id.dialog_cancel_button)
        val okButton = findViewById<Button>(R.id.dialog_ok_button)

        // 设置控件的初始值
        titleTextView.text = title
        messageTextView.text = message
        cancelButton.text=cancelText
        okButton.text=okText

        // 设置按钮的点击事件
        cancelButton.setOnClickListener {
            dismiss()
        }
        okButton.setOnClickListener {
            fun1()
            dismiss()
        }
    }
}