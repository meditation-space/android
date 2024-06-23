package com.example.meditation_space

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import BaseActivity
import kotlin.concurrent.thread

class LoginActivity : BaseActivity() {
    private var left_signin: LinearLayout? = null
    private var right_signin: LinearLayout? = null
    private var denglu: Button? = null
    private var zhuce: Button? = null
    private var left_login: LinearLayout? = null
    private var right_login: LinearLayout? = null
    private var confirmDenglu: Button? = null
    private var confirmZhuce: Button? = null
    private var denglu_account: EditText? = null
    private var denglu_password: EditText? = null
    private var zhuce_account: EditText? = null
    private var zhuce_password: EditText? = null

    private var loading: pl.droidsonroids.gif.GifImageView? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statusBar = StatusBar(this)
        statusBar.setColor(R.color.translucent) //设置颜色为透明状态栏
        setContentView(R.layout.activity_login)

        init()
        right_login!!.setBackgroundColor(Color.TRANSPARENT)
        zhuce!!.setTextColor(Color.GRAY)

        //判断是否自动登录
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val autoLogin = prefs.getBoolean("autoLogin", false)
        if (autoLogin) {
            val account = prefs.getString("account", "")
            val password = prefs.getString("password", "")
            thread {
                var bool = false
                var sunlight = 0
                var raindrop = 0
                var user_id = 1
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.61.19.94:5000/getUser?account=${account}&password=$password")
                    .build()
                val response = client.newCall(request).execute()
                var responseData = response.body?.string()
                if (responseData != null) {
                    try {
                        val jsonArray = JSONArray(responseData)
                        val jsonObject = jsonArray.getJSONObject(0)
                        sunlight = jsonObject.getString("sunlight").toInt()
                        raindrop = jsonObject.getString("raindrop").toInt()
                        user_id = jsonObject.getString("id").toInt()
                        bool=true
                    } catch (e: Exception) {
                        bool=false
                    }
                }
                if (bool) {
                    ActivityCollector.setUser_id(user_id)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("sunlight", sunlight)
                    intent.putExtra("raindrop", raindrop)
                    startActivity(intent)
                    finish()
                }
            }
        }

        denglu?.setOnClickListener {
            left_login!!.setBackgroundResource(R.drawable.background_gradient_left_shape)
            right_login!!.setBackgroundResource(android.R.color.transparent)
            denglu!!.setTextColor(Color.WHITE)
            zhuce!!.setTextColor(Color.GRAY)

            left_signin?.visibility = View.VISIBLE
            right_signin?.visibility = View.INVISIBLE
        }
        zhuce?.setOnClickListener {
            right_login!!.setBackgroundResource(R.drawable.background_gradient_right_shape)
            left_login!!.setBackgroundResource(android.R.color.transparent)
            zhuce!!.setTextColor(Color.WHITE)
            denglu!!.setTextColor(Color.GRAY)

            right_signin?.visibility = View.VISIBLE
            left_signin?.visibility = View.INVISIBLE
        }
        confirmDenglu?.setOnClickListener {
            thread {
                val account = denglu_account!!.text.toString()
                val password = denglu_password!!.text.toString()
                var bool = false
                var sunlight = 0
                var raindrop = 0
                var user_id = 1
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.61.19.94:5000/getUser?account=${account}&password=$password")
                    .build()
                val response = client.newCall(request).execute()
                var responseData = response.body?.string()
                if (responseData != null) {
                    try {
                        val jsonArray = JSONArray(responseData)
                        val jsonObject = jsonArray.getJSONObject(0)
                        sunlight = jsonObject.getString("sunlight").toInt()
                        raindrop = jsonObject.getString("raindrop").toInt()
                        user_id = jsonObject.getString("id").toInt()
                        bool=true
                    } catch (e: Exception) {
                        bool=false
                    }
                }
                runOnUiThread {
                    if (bool) {
                        loading?.visibility = View.VISIBLE
                        ActivityCollector.setUser_id(user_id)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("sunlight", sunlight)
                        intent.putExtra("raindrop", raindrop)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "账号或者密码错误！", Toast.LENGTH_SHORT).show()
                    }
                }
                if (bool) {//实现后续可以自动登录
                    val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                    editor.putString("account", account)
                    editor.putString("password", password)
                    editor.putBoolean("autoLogin", true)
                    editor.apply()
                }
            }
        }
        confirmZhuce?.setOnClickListener {
            thread {
                val account = zhuce_account!!.text.toString()
                val password = zhuce_password!!.text.toString()
                val conn = Login().getConnection()
                var bool = 0
                conn?.let {
                    val pstmt =
                        it.prepareStatement("insert into user(account,password) values(?,?);")
                    pstmt.setString(1, account)
                    pstmt.setString(2, password)
                    if (account.equals("")) {
                        bool = 2
                    } else if (password.equals("")) {
                        bool = 3
                    } else {
                        try {
                            pstmt.executeUpdate()
                            bool = 1
                        } catch (e: com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException) {
                            e.printStackTrace()//违反约束（唯一性约束）
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    pstmt.close()
                    it.close()
                }
                runOnUiThread {
                    when (bool) {
                        0 -> {
                            Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show()
                        }
                        1 -> {
                            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                        }
                        2 -> {
                            Toast.makeText(this, "账号不能为空！", Toast.LENGTH_SHORT).show()
                        }
                        3 -> {
                            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun init() {
        denglu = findViewById(R.id.denglu)
        zhuce = findViewById(R.id.zhuce)
        left_login = findViewById(R.id.left_login)
        right_login = findViewById(R.id.right_login)
        left_signin = findViewById(R.id.left_signin)
        right_signin = findViewById(R.id.right_signin)
        confirmDenglu = findViewById(R.id.confirmDenglu)
        confirmZhuce = findViewById(R.id.confirmZhuce)
        denglu_account = findViewById(R.id.denglu_account)
        denglu_password = findViewById(R.id.denglu_password)
        zhuce_account = findViewById(R.id.zhuce_account)
        zhuce_password = findViewById(R.id.zhuce_password)

        loading = findViewById(R.id.iv_gif)
    }

}