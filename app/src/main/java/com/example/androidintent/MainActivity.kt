package com.example.androidintent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // ⭐ แนะนำ: ประกาศ Key เป็นค่าคงที่เพื่อป้องกันการพิมพ์ผิด // companion object ทำหน้าที่เหมือน Static ใน java คือเข้าถึงได้โดยไม่ได้สร้าง Obj.
    companion object {
        const val KEY_USERNAME = "USERNAME_DATA"
        const val KEY_PASSWORD = "PASSWORD_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // เชื่อม UI กับตัวแปร
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            // ดึงข้อความจาก EditText ออกมาเป็น String
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // สร้าง Intent เพื่อไปยัง DisplayActivity
            val intent = Intent(this, DisplayActivity::class.java)

            // แนบข้อมูล (Key-Value) ไปกับ Intent
            intent.putExtra(KEY_USERNAME, username)
            intent.putExtra(KEY_PASSWORD, password)

            // เริ่ม Activity ใหม่
            startActivity(intent)
        }
    }
}