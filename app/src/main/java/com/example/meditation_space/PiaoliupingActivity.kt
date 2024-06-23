package com.example.meditation_space

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import BaseActivity

class PiaoliupingActivity : BaseActivity() {
    private var reng: Button? = null
    private var lao: Button? = null
    private var bottle: Button? = null
    private var plp_close: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piaoliuping)

        init()

        reng?.setOnClickListener {

        }
        lao?.setOnClickListener {

        }
        bottle?.setOnClickListener {

        }
        plp_close?.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        reng = findViewById(R.id.reng)
        lao = findViewById(R.id.lao)
        bottle = findViewById(R.id.bottle)
        plp_close = findViewById(R.id.plp_close)
    }
}