package com.example.spiderindia

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val AddStudent = findViewById<LinearLayout>(R.id.addstudent)
        val ViewStudent = findViewById<LinearLayout>(R.id.viewstudent)

        ViewStudent.setOnClickListener {
            val intent = Intent(this,ViewDataActivity::class.java)
            startActivity(intent)
        }

        AddStudent.setOnClickListener{
            val intent = Intent(this,AddStudentActivity::class.java)
            startActivity(intent)
        }
    }
}