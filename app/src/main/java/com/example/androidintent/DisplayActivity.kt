package com.example.androidintent

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // เชื่อม UI กับตัวแปร
        val usernameTextView: TextView = findViewById(R.id.usernameTextView)
        val passwordTextView: TextView = findViewById(R.id.passwordTextView)

        // รับข้อมูลจาก Intent ที่ส่งมา
        // ใช้ Key เดียวกันกับตอนส่ง และใส่ค่า default เป็น "" (String ว่าง)
        val username = intent.getStringExtra(MainActivity.KEY_USERNAME) ?: "N/A"
        val password = intent.getStringExtra(MainActivity.KEY_PASSWORD) ?: "N/A"

        // นำข้อมูลไปแสดงผลใน TextView
        usernameTextView.text = "Username: $username"
        passwordTextView.text = "Password: $password"
    }
}