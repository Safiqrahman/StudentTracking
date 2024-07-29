package com.example.spiderindia

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class ViewDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewdata)

        val students = getStudents()

        val recyclerView = findViewById<RecyclerView>(R.id.StudentdataView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudentAdapter(students,this)
        val goBackButton =findViewById<MaterialToolbar>(R.id.goBackButton)

        goBackButton.setOnClickListener {
            finish()
        }
    }

    private fun getStudents(): List<Student> {
        val sharedPreferences = getSharedPreferences("student_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val studentListType = object : TypeToken<List<Student>>() {}.type

        val studentsJson = sharedPreferences.getString("students", null)
        return if (studentsJson != null) {
            gson.fromJson(studentsJson, studentListType)
        } else {
            emptyList()
        }
    }
}
