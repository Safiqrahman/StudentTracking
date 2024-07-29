package com.example.spiderindia

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val studentName = intent.getStringExtra("studentName")
        val studentClass = intent.getStringExtra("studentClass")
        val schoolName = intent.getStringExtra("schoolName")
        val imageUri = intent.getStringExtra("imageUri")
        val section = intent.getStringExtra("studentSection")


        findViewById<TextView>(R.id.StudentName).text = studentName
        findViewById<TextView>(R.id.Class).text = studentClass
        findViewById<TextView>(R.id.schoolname1).text = schoolName
        findViewById<TextView>(R.id.Section).text = section


        val studentImage = findViewById<ImageView>(R.id.studentImage)
        if (!imageUri.isNullOrEmpty()) {
            try {
                studentImage.setImageURI(Uri.parse(imageUri))
            } catch (e: Exception) {
                e.printStackTrace()
                studentImage.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            studentImage.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}
