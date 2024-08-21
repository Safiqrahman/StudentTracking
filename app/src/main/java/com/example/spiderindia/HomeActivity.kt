package com.example.spiderindia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val addStudent = findViewById<LinearLayout>(R.id.addstudent)
        val viewStudent = findViewById<LinearLayout>(R.id.viewstudent)
        val logout = findViewById<ImageView>(R.id.logoutField)

        viewStudent.setOnClickListener {
            val intent = Intent(this, ViewDataActivity::class.java)
            startActivity(intent)
        }

        addStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, which ->

            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->

            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
