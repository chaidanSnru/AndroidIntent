package com.example.androidintent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 1. ค้นหาปุ่มจาก ID ที่ตั้งไว้ใน XML
        val goToSecondButton: Button = findViewById(R.id.goToSecondButton)

        // 2. ตั้งค่าการดักฟังเมื่อปุ่มถูกคลิก
        goToSecondButton.setOnClickListener {
            // 3. สร้าง Intent เพื่อระบุปลายทาง
            //    - this: คือ Context หรือ Activity ปัจจุบัน
            //    - SecondActivity::class.java: คือคลาสของ Activity ปลายทาง
            val intent = Intent(this, SecondActivity::class.java)

            // 4. สั่งให้ระบบเริ่ม Activity ใหม่ตามที่ Intent ระบุ
            startActivity(intent)
        }
    }
}