package com.example.spiderindia

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.Manifest

import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddStudentActivity : AppCompatActivity() {

    private lateinit var studentImageView: ImageView
    private val REQUEST_IMAGE_GALLERY = 1
    private val REQUEST_PERMISSION = 2
    private var selectedImageUri: Uri? = null
    private lateinit var dobEditText: TextInputEditText
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US) // Desired date format


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addstudent)

        val studentName = findViewById<TextInputLayout>(R.id.studentname)
        val schoolName = findViewById<TextInputLayout>(R.id.schoolname)
        val classDropdown = findViewById<AutoCompleteTextView>(R.id.class_dropdown)
        val sectionDropdown = findViewById<AutoCompleteTextView>(R.id.section_dropdown)
        val studentImageButton = findViewById<ImageView>(R.id.imageView1)
        val goBackButton = findViewById<MaterialToolbar>(R.id.goBackButton)
        val saveButton = findViewById<Button>(R.id.Submit)
        dobEditText = findViewById(R.id.dob_edit_text)


        val classes = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val classAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, classes)
        classDropdown.setAdapter(classAdapter)

        // Populate section dropdown
        val sections = listOf("A", "B", "C", "D")
        val sectionAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sections)
        sectionDropdown.setAdapter(sectionAdapter)

        // Show dropdown on click
        classDropdown.setOnClickListener {
            classDropdown.showDropDown()
        }

        sectionDropdown.setOnClickListener {
            sectionDropdown.showDropDown()
        }
        dobEditText.setOnClickListener {
            showDatePickerDialog()
        }
        studentImageView = findViewById(R.id.imageView)


        goBackButton.setOnClickListener {
            finish()
        }
        studentImageButton.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        saveButton.setOnClickListener {
            val name = studentName.editText?.text.toString()
            val school = schoolName.editText?.text.toString()
            val studentClass = classDropdown.text.toString()
            val studentSection= sectionDropdown.text.toString()
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                dobEditText.setText(dateFormat.format(calendar.time))
            },
            year, month, day
        )

        datePickerDialog.show()
    }



    private fun checkPermissionsAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                openGallery()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, REQUEST_PERMISSION)
            }
        } else {
            // Android 10 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            } else {
                openGallery()
            }
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                selectedImageUri = data?.data
                studentImageView.setImageURI(selectedImageUri)
            } else if (requestCode == REQUEST_PERMISSION) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                    openGallery()
                }
            }
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
