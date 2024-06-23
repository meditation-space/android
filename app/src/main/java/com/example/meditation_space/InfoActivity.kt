package com.example.meditation_space

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import BaseActivity
import android.content.Intent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread

class InfoActivity : BaseActivity() {
    private var nickname: TextView? = null
    private var sex: TextView? = null
    private var phone: TextView? = null
    private var birthday: TextView? = null
    private var nature: TextView? = null
    private var place: TextView? = null
    private var info_close: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        init()

        nickname?.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("请输入您的昵称（最多6个字符）")

            val input = EditText(this)
            input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(6)) // 设置最大输入长度为6个字符
            builder.setView(input)

            builder.setPositiveButton("确定") { dialog, which ->
                val name = input.text.toString()
                if (name.isNotEmpty()) {
                    //修改姓名
                    nickname?.setText(name);
                } else {
                    Toast.makeText(this, "输入的昵称不能为空", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("取消") { dialog, which ->
            }
            val dialog = builder.create()
            dialog.show()
        }
        sex?.setOnClickListener {
            val items = arrayOf("男", "女", "保密")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("请选择您的性别")
            builder.setSingleChoiceItems(items, -1) { dialog, which ->
            }
            builder.setPositiveButton("确定") { dialog, which ->
                //修改性别
                sex?.setText("保密");
            }
            builder.setNegativeButton("取消") { dialog, which ->
            }
            val dialog = builder.create()
            dialog.show()
        }
        phone?.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("请输入您的电话")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
            builder.setView(input)

            builder.setPositiveButton("确定") { dialog, which ->
                //修改电话
                val number = input.text.toString()
                phone?.setText(number);
                thread {
                    var bool = false
                    var sunlight = 0
                    var raindrop = 0
                    var user_id = 1
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url("http://10.61.19.94:5000/getUser")
                        .build()
                }
            }
            builder.setNegativeButton("取消") { dialog, which ->
            }
            val dialog = builder.create()
            dialog.show()
        }
        birthday?.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(2000, 0, 1) // 设置默认日期为2000年1月1日

            val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                // 处理选择的日期
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                Toast.makeText(this, "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
                birthday?.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }
        nature?.setOnClickListener {
            val items = arrayOf("冷静", "热情", "善良", "机智", "严肃", "活泼", "害羞", "开朗", "冷漠", "热情")
            val checkedItems = booleanArrayOf(false, false, false, false, false, false, false, false, false, false)
            val selectedTraits = mutableListOf<String>()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("请选择您的性格")
            builder.setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            builder.setPositiveButton("确定") { dialog, _ ->
                for (i in items.indices) {
                    if (checkedItems[i]) {
                        selectedTraits.add(items[i])
                    }
                }
                if (selectedTraits.size > 3) {
                    Toast.makeText(this, "最多只能选择三个选项", Toast.LENGTH_SHORT).show()
                    selectedTraits.clear()
                } else {
                    Toast.makeText(this, "Selected traits: ${selectedTraits.joinToString(", ")}", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("取消") { _, _ -> }

            val dialog = builder.create()
            dialog.show()
        }
        place?.setOnClickListener {
            //修改地点
        }
        info_close?.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        nickname = findViewById(R.id.nickname)
        sex = findViewById(R.id.sex)
        phone = findViewById(R.id.phone)
        birthday = findViewById(R.id.birthday)
        nature = findViewById(R.id.nature)
        place = findViewById(R.id.place)
        info_close = findViewById(R.id.info_close)
    }

}

