package com.example.androidintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // ประกาศตัวแปรสำหรับ TextView ที่จะแสดงผล
    private lateinit var selectedCountryTextView: TextView
    // --- ขั้นตอนที่ 1: ลงทะเบียน Launcher เพื่อรอรับผลลัพธ์ --- // สร้างตัวรับผลลัพธ์ชื่อ countryPickerLauncher
    // StartActivityForResult() คือ "สัญญา" ว่าเราจะเปิด Activity และรอผลกลับมา
    private val countryPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // --- ขั้นตอนที่ 4: จัดการผลลัพธ์ที่ส่งกลับมา --- // โค้ดในนี้จะทำงาน "หลังจาก" ที่ CountryPickerActivity ส่งผลลัพธ์กลับมาแล้ว
        // ตรวจสอบว่าผลลัพธ์สำเร็จหรือไม่ (ผู้ใช้กดยืนยัน)
        if (result.resultCode == Activity.RESULT_OK) {
            // ดึงข้อมูล (Intent) ที่ส่งกลับมา
            val selectedCountry = result.data?.getStringExtra(CountryPickerActivity.KEY_SELECTED_COUNTRY)
            // นำข้อมูลที่ได้ไปแสดงผลใน TextView
            selectedCountryTextView.text = selectedCountry ?: "เกิดข้อผิดพลาด"
        }
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
        // เชื่อมตัวแปรกับ UI จากไฟล์ XML
        selectedCountryTextView = findViewById(R.id.selectedCountryTextView)
        val selectCountryButton: Button = findViewById(R.id.selectCountryButton)

        // ตั้งค่าการทำงานเมื่อปุ่มถูกกด
        selectCountryButton.setOnClickListener {
            // --- ขั้นตอนที่ 2: สร้าง Intent และสั่งให้ Launcher เริ่มทำงาน ---
            val intent = Intent(this, CountryPickerActivity::class.java)
            // สั่งให้ launcher ที่เราสร้างไว้ เริ่มทำงานด้วย intent นี้
            countryPickerLauncher.launch(intent)
        }
    }
}