package com.example.androidintent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // ประกาศตัวแปรแบบใช้ภายหลังด้วย keyword "lateinit" สำหรับการใช้งานกล้อง
    private lateinit var cameraButton: Button
    private lateinit var imageView: ImageView

    // 1. สร้างตัวรับผลลัพธ์ (ActivityResultLauncher)
    // นี่คือวิธีที่ทันสมัยในการรับข้อมูลกลับจาก Activity อื่น
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ตรวจสอบว่าการทำงานสำเร็จหรือไม่ (ผู้ใช้กดยืนยันการถ่ายภาพ)
        if (result.resultCode == Activity.RESULT_OK) {
            // รับข้อมูลภาพกลับมาในรูปแบบ Bitmap ขนาดเล็ก (Thumbnail)
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            // นำภาพ Bitmap ไปแสดงใน ImageView
            imageBitmap?.let {
                imageView.setImageBitmap(it)
            }
        }
    }

    // --- Launcher สำหรับ "ขออนุญาต" ใช้กล้อง ---
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // ถ้าผู้ใช้กด "อนุญาต" ให้เปิดกล้องได้เลย
            openCamera()
        } else {
            // ถ้าผู้ใช้กด "ปฏิเสธ" ให้แสดงข้อความแจ้งเตือน
            Toast.makeText(this, "คุณต้องอนุญาตให้ใช้กล้องเพื่อถ่ายภาพ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissionAndOpenCamera() {
        // ตรวจสอบสถานะการอนุญาตใช้กล้อง
        when {
            // กรณีที่ 1: ได้รับอนุญาตแล้ว
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // ถ้าได้รับอนุญาตแล้ว ก็เปิดกล้องได้เลย
                openCamera()
            }
            // กรณีที่ 2: ควรแสดงคำอธิบายเพิ่มเติม (ถ้าผู้ใช้เคยปฏิเสธไปแล้ว)
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // ในแอปจริง ส่วนนี้อาจจะแสดง Dialog อธิบายว่าทำไมต้องใช้กล้อง
                // แต่สำหรับตัวอย่างนี้ เราจะขออนุญาตไปเลย
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            // กรณีที่ 3: ยังไม่เคยขออนุญาต หรือเคยปฏิเสธแบบ "ห้ามถามอีก"
            else -> {
                // ขออนุญาตจากผู้ใช้
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // ฟังก์ชันสำหรับเปิดกล้อง (แยกออกมาเพื่อให้เรียกใช้ซ้ำได้)
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
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
        // เชื่อมตัวแปรกับปุ่มในไฟล์ layout
        val webButton: Button = findViewById(R.id.webButton)
        // เชื่อมตัวแปรกับปุ่มในไฟล์ layout
        val mapButton: Button = findViewById(R.id.mapButton)
        // เชื่อมตัวแปรกับปุ่มในไฟล์ layout
        val dialButton: Button = findViewById(R.id.dialButton)
        // เชื่อมตัวแปรกับ UI จากไฟล์ XML
        cameraButton = findViewById(R.id.cameraButton)
        imageView = findViewById(R.id.imageView)

        // ตั้งค่าการทำงานเมื่อปุ่มถูกกดเปิดWeb
        webButton.setOnClickListener {
            // กำหนด URL ของเว็บไซต์ที่ต้องการเปิด
            val webUrl = "https://www.google.com"
            // สร้าง Intent แบบไม่ระบุชัดเจน (Implicit Intent)
            // 1. Action: Intent.ACTION_VIEW คือการบอกว่า "ต้องการดูข้อมูล"
            // 2. Data: Uri.parse(webUrl) คือข้อมูล (URL) ที่ต้องการให้ดู
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
            // (Safety Check) ตรวจสอบว่ามีแอปในเครื่องที่สามารถทำงานนี้ได้หรือไม่ (เช่น มีเบราว์เซอร์ติดตั้งอยู่)
            // เพื่อป้องกันแอปแครช
            if (intent.resolveActivity(packageManager) != null) {
                // ถ้ามีแอปที่พร้อมทำงาน ให้เริ่ม Activity
                startActivity(intent)
            } else {
                // ถ้าไม่มีแอปที่ทำงานนี้ได้ ให้แสดงข้อความแจ้งเตือน
                Toast.makeText(this, "ไม่พบแอปพลิเคชันสำหรับเปิดเว็บไซต์", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        // ตั้งค่าการทำงานเมื่อปุ่มถูกกดเปิด map
        mapButton.setOnClickListener {
            // กำหนดตำแหน่งด้วยชื่อสถานที่ (วิธีที่ง่ายและยืดหยุ่น)
            // geo:0,0?q=ชื่อสถานที่
            val mapQuery = "Siam Paragon"
            val mapUri = Uri.parse("geo:0,0?q=$mapQuery")
            // หรือกำหนดด้วย ละติจูด, ลองจิจูด และระดับการซูม (z=zoom level)
            // val latitude = 13.7462 // val longitude = 100.5348 // val zoomLevel = 15
            // val mapUri = Uri.parse("geo:$latitude,$longitude?z=$zoomLevel")

            // สร้าง Intent แบบไม่ระบุชัดเจน (Implicit Intent)
            // 1. Action: Intent.ACTION_VIEW คือการบอกว่า "ต้องการดูข้อมูล"
            // 2. Data: mapUri คือข้อมูลตำแหน่งที่ต้องการให้ดู
            val intent = Intent(Intent.ACTION_VIEW, mapUri)

            // (สำคัญ) ระบุ package ของ Google Maps เพื่อให้เปิดแอปนี้โดยตรง
            // ถ้าไม่ระบุบรรทัดนี้ หรือเครื่องผู้ใช้ไม่มี Google Maps ระบบจะแสดงแอปแผนที่อื่นๆ ให้เลือก
            intent.setPackage("com.google.android.apps.maps")
            // (Safety Check) ตรวจสอบว่ามีแอปในเครื่องที่สามารถทำงานนี้ได้หรือไม่
            // เพื่อป้องกันแอปแครช
            if (intent.resolveActivity(packageManager) != null) {
                // ถ้ามีแอปที่พร้อมทำงาน ให้เริ่ม Activity
                startActivity(intent)
            } else {
                // ถ้าไม่มีแอปที่ทำงานนี้ได้ ให้แสดงข้อความแจ้งเตือน
                Toast.makeText(this, "ไม่พบแอปพลิเคชัน Google Maps", Toast.LENGTH_SHORT).show()
            }
        }
        dialButton.setOnClickListener {
            // กำหนดเบอร์โทรศัพท์ที่ต้องการ
            val phoneNumber = "0991234567"

            // สร้าง Intent แบบไม่ระบุชัดเจน (Implicit Intent)
            // 1. Action: Intent.ACTION_DIAL คือการบอกว่า "ต้องการเปิดหน้าจอโทรศัพท์"
            // 2. Data: Uri.parse("tel:$phoneNumber") คือข้อมูลเบอร์โทรศัพท์ที่ต้องขึ้นต้นด้วย "tel:"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))

            // (Safety Check) ตรวจสอบว่ามีแอปในเครื่องที่สามารถทำงานนี้ได้หรือไม่
            // เพื่อป้องกันแอปแครช
            if (intent.resolveActivity(packageManager) != null) {
                // ถ้ามีแอปที่พร้อมทำงาน ให้เริ่ม Activity
                startActivity(intent)
            } else {
                // ถ้าไม่มีแอปที่ทำงานนี้ได้ ให้แสดงข้อความแจ้งเตือน
                Toast.makeText(this, "ไม่พบแอปพลิเคชันสำหรับโทรศัพท์", Toast.LENGTH_SHORT).show()
            }
        }
        // ตั้งค่าการคลิกปุ่มCamera
        cameraButton.setOnClickListener {
            // เมื่อกดปุ่ม ให้เรียกฟังก์ชัน checkPermissionAndOpenCamera()
            checkPermissionAndOpenCamera()
        }
    }
}