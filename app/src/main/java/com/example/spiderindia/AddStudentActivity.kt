package com.example.spiderindia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class AddStudentActivity : AppCompatActivity() {

    private lateinit var studentImageView: ImageView
    private val REQUEST_IMAGE_GALLERY = 1
    private val REQUEST_PERMISSION = 2
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addstudent)

        val studentName = findViewById<TextInputLayout>(R.id.studentname)
        val schoolName = findViewById<TextInputLayout>(R.id.schoolname)
        val classSpinner = findViewById<Spinner>(R.id.class_spinner)
        val sectionSpinner = findViewById<Spinner>(R.id.section_spinner)
        val studentImageButton = findViewById<ImageView>(R.id.imageView1)
        val goBackButton = findViewById<MaterialToolbar>(R.id.goBackButton)
        val saveButton = findViewById<Button>(R.id.Submit)
        studentImageView = findViewById(R.id.imageView)

        // Populate class spinner
        val classes = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val classAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classSpinner.adapter = classAdapter

        // Populate section spinner
        val sections = listOf("A", "B", "C", "D")
        val sectionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sections)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sectionSpinner.adapter = sectionAdapter
        goBackButton.setOnClickListener {
            finish()
        }
        studentImageButton.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        saveButton.setOnClickListener {
            val name = studentName.editText?.text.toString()
            val school = schoolName.editText?.text.toString()
            val studentClass = classSpinner.selectedItem.toString()
            val studentSection= classSpinner.selectedItem.toString()
            val imageUri = selectedImageUri?.toString()


            if (name.isNotEmpty() && school.isNotEmpty() && imageUri != null) {
                val newStudent = Student(name, studentClass,studentSection, imageUri)
                saveStudent(newStudent)

                Toast.makeText(this, "Submitted Successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ViewDataActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionsAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY) {
            selectedImageUri = data?.data
            studentImageView.setImageURI(selectedImageUri)
        }
    }

    private fun saveStudent(student: Student) {
        val sharedPreferences = getSharedPreferences("student_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val studentListType = object : TypeToken<MutableList<Student>>() {}.type

        val existingStudentsJson = sharedPreferences.getString("students", null)
        val existingStudents: MutableList<Student> = if (existingStudentsJson != null) {
            gson.fromJson(existingStudentsJson, studentListType)
        } else {
            mutableListOf()
        }

        existingStudents.add(student)
        val updatedStudentsJson = gson.toJson(existingStudents)
        editor.putString("students", updatedStudentsJson)
        editor.apply()
    }
}
