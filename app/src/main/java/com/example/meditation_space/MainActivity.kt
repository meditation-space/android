package com.example.meditation_space

import BaseActivity
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {
    private var drawerLayout: DrawerLayout? = null//滑动布局
    private var navView: com.google.android.material.navigation.NavigationView? = null//左侧导航栏
    private var toolbar: ImageView? = null//用于进入左侧导航栏
    private var countdownText: TextView? = null//倒计时
    private var startButton: Button? = null//开始按钮
    private var centerImage: pl.droidsonroids.gif.GifImageView? = null//中心图片
    private var cancelButton: Button? = null
    private var main_sunlight_view: TextView? = null
    private var main_raindrop_view: TextView? = null
    private var sound: ImageView? = null

    private var totalTime: Long = 1200000//全局变量，用于记录总时间(初始值:20分钟,即1200000毫秒)
    private var countDownTimer: CountDownTimer? = null//记时对象
    lateinit var receiver: numberDownClose  //广播接收器。用于关闭数字倒计时（在强制退出或者切换后台时）
    lateinit var numberViewReceiver: numberDownViewRefresh  //广播接收器。用于在记时结束后跳出对话框
    private val mediaPlayer = MediaPlayer()//音乐播放对象

    //    private var sunlight: Int = 0//阳光
//    private var raindrop: Int = 0//雨滴 //这两个属性我使用了ActivityCollector中的成员属性来代替
    private var hideControl: Boolean = false//标识符，为true时，隐藏一些没必要的操作（在开始收集雨水时）

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statusBar = StatusBar(this)
        statusBar.setColor(R.color.transparent) //设置颜色为透明状态栏
        setContentView(R.layout.activity_main)

        init()

        initMediaPlayer()
        sound?.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start() // 开始播放
                sound?.setImageResource(R.drawable.sound)
            } else {
                mediaPlayer.reset() // 停止播放
                initMediaPlayer()
                sound?.setImageResource(R.drawable.no_sound)
            }
        }
        centerImage?.setOnClickListener {
            if (!hideControl) {//hideControl==false才生效
//                Toast.makeText(this, "你点击了图片", Toast.LENGTH_SHORT).show()
            }
        }
        toolbar?.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
        navView?.setNavigationItemSelectedListener {//左侧导航栏点击事件
            when (it.itemId) {
                R.id.menu_info -> {
                    val intent = Intent(this,InfoActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_garden -> {
                    val intent = Intent(this, MyGardenActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_shop -> {
                    val intent = Intent(this, ShopActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_rank -> {
                    val intent = Intent(this, RankingActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_friend_list -> {
                    val intent = Intent(this,FriendListActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_log_list -> {
                    val intent = Intent(this,LogActivity::class.java)
                    startActivity(intent)
                }

//                R.id.menu_piaoliuping -> {
//                    val intent = Intent(this,PiaoliupingActivity::class.java)
//                    startActivity(intent)
//                }

                R.id.menu_signout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("autoLogin", false)
                    editor.apply()
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
        startButton?.setOnClickListener {//开始收集按钮
            //弹出对话框：告知用户
            val customDialog = CustomDialog(
                this,
                "开始收集！",
                "即将开始专注训练了哦，在此切换后台或者切换界面都会被认定失败。",
                "取消",
                "现在开始",
                { startCountdown() }
            )
            customDialog.show()
        }
        cancelButton?.setOnClickListener { //放弃按钮
            val customDialog = CustomDialog(
                this,
                "放弃收集",
                "已经收集了一段时间了，放弃此次收集将前功尽弃，是否放弃？",
                "我再想想",
                "确认放弃",
                { finishCountdown() }
            )
            customDialog.show()
        }
        countdownText?.setOnClickListener {
            if (!hideControl) {//hideControl==false才生效
                showNumberPickerDialog()
            }
        }
    }

    override fun onStart() {//每次进入这个界面（如从花店退出时，或者一开始进入时）刷新阳光和雨滴
        super.onStart()
        main_sunlight_view?.text = ActivityCollector.getSunLight().toString()
        main_raindrop_view?.text = ActivityCollector.getRainDrop().toString()
    }

    private fun init() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        startButton = findViewById(R.id.start_button)
        cancelButton = findViewById(R.id.cancel_button)
        countdownText = findViewById(R.id.countdown_text)
        centerImage = findViewById(R.id.centerImage)
        main_sunlight_view = findViewById(R.id.main_sunlight_view)
        main_raindrop_view = findViewById(R.id.main_raindrop_view)
        sound = findViewById(R.id.sound)
        //从login界面获取的阳光和雨滴，跟新ActivityCollector中的阳光和雨滴
        ActivityCollector.setSunLight(intent.getIntExtra("sunlight", 0))
        ActivityCollector.setRainDrop(intent.getIntExtra("raindrop", 0))
    }

    private fun initMediaPlayer() {
        val assetManager = assets
        val fd = assetManager.openFd("久石让-千与千寻（纯音乐）.mp3")
        mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer.prepare()
    }

    private fun showNumberPickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_buttom)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
        val numberPicker = dialog.findViewById<NumberPicker>(R.id.number_picker)
        val displayedValues =
            arrayOf(
                "10",
                "15",
                "20",
                "25",
                "30",
                "35",
                "40",
                "45",
                "50",
                "55",
                "60",
                "65",
                "70",
                "75",
                "80",
                "85",
                "90",
                "95",
                "100",
                "105",
                "110",
                "115",
                "120"
            )
        numberPicker.minValue = 0
        numberPicker.maxValue = 22
        numberPicker.displayedValues = displayedValues
        numberPicker.value =
            (((totalTime / 60000) - 10) / 5).toInt()//通过totalTime的映射获得当前选中的时间的下标index

        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        btnOk?.setOnClickListener {
            val selectedNumber = numberPicker.value
            totalTime = displayedValues[selectedNumber].toLong() * 60000
            //修改界面上的倒计时
            countdownText?.text =
                String.format("%02d:%02d", displayedValues[selectedNumber].toInt(), 0)
            dialog.dismiss()
        }
    }

    private fun startCountdown() {
        ActivityCollector.setIsCollectRainDrop(true)
        //开始、放弃按钮切换
        startButton?.visibility = View.INVISIBLE
        cancelButton?.visibility = View.VISIBLE

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                countdownText?.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {//记时间正常结束
                ActivityCollector.setIsCollectRainDrop(false)
                countdownText?.text = "00:00"
//                加分
                thread {
                    //更新界面上的信息
                    var sunlight = ActivityCollector.getSunLight()
                    var raindrop = ActivityCollector.getRainDrop()
                    raindrop += (totalTime / 60000).toInt()
                    ActivityCollector.setRainDrop(raindrop)
                    val randoms = (1..10).random()
                    if (randoms <= 5) {//50%的机率可以让用户获得此次获得水滴的10%的阳光
                        sunlight += (totalTime / 60000).toInt() / 10
                        ActivityCollector.setSunLight(sunlight)
                    }
                    //更新数据库中的信息
                    val conn = Login().getConnection()
                    conn?.let {
                        val pstmt =
                            it.prepareStatement("update user set sunlight=?,raindrop=? where id=?;")
                        pstmt.setInt(1, sunlight)
                        pstmt.setInt(2, raindrop)
                        pstmt.setInt(3, ActivityCollector.getUser_id())
                        pstmt.executeUpdate()
                        pstmt.close()
                        it.close()
                    }
                    runOnUiThread {//界面上跟新
                        main_sunlight_view?.text = sunlight.toString()
                        main_raindrop_view?.text = raindrop.toString()
                    }
                }
                //提示对话框
//                val intent2 = Intent("com.example.wyr_timegarden.NUMBERDOWNDIALOG")
//                sendBroadcast(intent2)
                WarningDialog(this@MainActivity, "收集成功", "恭喜你完成了收集！") {
                    val minutes = totalTime / 60000
                    val seconds = (totalTime % 60000) / 1000
                    countdownText?.text =
                        String.format("%02d:%02d", minutes, seconds)//恢复countdownText里面的内容
                }.show()
//                把之前隐藏的操作界面重新激活
                startButton?.visibility = View.VISIBLE
                cancelButton?.visibility = View.INVISIBLE
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)// 将DrawerLayout设置为LOCK_MODE_UNLOCKED模式，即解锁侧滑菜单
                toolbar?.visibility = View.VISIBLE
                hideControl = false
            }
        }.start()

//        隐藏没必要的操作界面
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)//设置为锁定状态，即无法通过手势或代码打开侧滑菜单。
        toolbar?.visibility = View.INVISIBLE
        hideControl = true
    }

    private fun finishCountdown() {
        ActivityCollector.setIsCollectRainDrop(false)
        //开始、放弃按钮切换
        startButton?.visibility = View.VISIBLE
        cancelButton?.visibility = View.INVISIBLE

        countDownTimer?.cancel()
        val minutes = totalTime / 60000
        val seconds = (totalTime % 60000) / 1000
        countdownText?.text = String.format("%02d:%02d", minutes, seconds)//恢复countdownText里面的内容
//        把之前隐藏没必要的操作界面重新激活
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)// 将DrawerLayout设置为LOCK_MODE_UNLOCKED模式，即解锁侧滑菜单
        toolbar?.visibility = View.VISIBLE
        hideControl = false
    }


    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.wyr_timegarden.NUBERDOWNCLOSE")//过滤器
        receiver = numberDownClose()
        //第一个参数是广播接收器BroadcastReceiver，第二个参数是过滤器。
        registerReceiver(receiver, intentFilter)//动态注册接收器

        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("com.example.wyr_timegarden.NUMBERDOWNDIALOG")//过滤器
        numberViewReceiver = numberDownViewRefresh()
        registerReceiver(numberViewReceiver, intentFilter2)//动态注册接收器
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)//注销接收器
        unregisterReceiver(numberViewReceiver)//注销接收器
        countDownTimer?.cancel()
        mediaPlayer.stop()
        mediaPlayer.release()
        Log.d("MainActivity", "onDestroy")
    }

    inner class numberDownClose : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            finishCountdown()
        }
    }

    inner class numberDownViewRefresh : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            p0?.let {
                WarningDialog(this@MainActivity, "收集成功", "恭喜你完成了收集！") {
                    val minutes = totalTime / 60000
                    val seconds = (totalTime % 60000) / 1000
                    countdownText?.text =
                        String.format("%02d:%02d", minutes, seconds)//恢复countdownText里面的内容
                }.show()
            }
        }
    }
}