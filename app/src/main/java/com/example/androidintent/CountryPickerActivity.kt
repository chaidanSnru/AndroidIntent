package com.example.androidintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class CountryPickerActivity : AppCompatActivity() {
    // ประกาศ Key สำหรับส่งข้อมูลกลับ ควรเป็นค่าคงที่เพื่อให้เรียกใช้ได้ง่ายและไม่ผิดพลาด
    companion object {
        const val KEY_SELECTED_COUNTRY = "SELECTED_COUNTRY"
    }
    // ประกาศตัวแปรสำหรับ RadioGroup
    private lateinit var countryRadioGroup: RadioGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_country_picker)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // เชื่อมตัวแปรกับ UI จากไฟล์ XML
        countryRadioGroup = findViewById(R.id.countryRadioGroup)
        val confirmButton: Button = findViewById(R.id.confirmButton)
        // เรียกใช้ฟังก์ชันเพื่อสร้าง RadioButton ของทุกประเทศ
        populateCountries()
        // ตั้งค่าการทำงานเมื่อปุ่ม "ยืนยัน" ถูกกด
        confirmButton.setOnClickListener {
            // หา ID ของ RadioButton ที่ถูกเลือกในกลุ่ม
            val selectedRadioButtonId = countryRadioGroup.checkedRadioButtonId
            // ตรวจสอบว่ามีการเลือกหรือไม่ (-1 คือยังไม่มีการเลือก)
            if (selectedRadioButtonId != -1) {
                // ถ้ามีการเลือก ให้หาตัว RadioButton จาก ID
                val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
                // ดึงข้อความ (ชื่อประเทศ) จาก RadioButton
                val selectedCountry = selectedRadioButton.text.toString()

                // --- ขั้นตอนที่ 3: สร้าง Intent, แพ็คข้อมูล, และตั้งค่าผลลัพธ์ ---
                val resultIntent = Intent()
                // แนบข้อมูลที่ต้องการส่งกลับไปกับ Intent
                resultIntent.putExtra(KEY_SELECTED_COUNTRY, selectedCountry)
                // ตั้งค่าผลลัพธ์ว่า "สำเร็จ" (RESULT_OK) พร้อมแนบข้อมูลกลับไป
                setResult(Activity.RESULT_OK, resultIntent)

                // ปิด Activity นี้เพื่อกลับไปหน้าแรกพร้อมส่งผลลัพธ์
                finish()
            } else {
                // ถ้ายังไม่ได้เลือก ให้แสดงข้อความเตือน
                Toast.makeText(this, "กรุณาเลือกประเทศ", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // ฟังก์ชันสำหรับสร้าง RadioButton ของทุกประเทศแล้วเพิ่มเข้าไปใน RadioGroup
    private fun populateCountries() {
        val allCountries = getAllCountries()
        for (country in allCountries) {
            val radioButton = RadioButton(this)
            radioButton.text = country
            radioButton.textSize = 18f
            countryRadioGroup.addView(radioButton)
        }
    }
    // ฟังก์ชันสำหรับดึงรายชื่อประเทศทั้งหมดจากระบบของ Android
    private fun getAllCountries(): ArrayList<String> {
        val locales: Array<String> = Locale.getISOCountries()
        val countries = ArrayList<String>()
        for (countryCode in locales) {
            val locale = Locale("", countryCode)
            countries.add(locale.displayCountry)
        }
        // เรียงรายชื่อประเทศตามตัวอักษร
        countries.sort()
        return countries
    }
}