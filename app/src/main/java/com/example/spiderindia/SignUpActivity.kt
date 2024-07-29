package com.example.spiderindia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var username: String
    private lateinit var phoneNumber: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val usernameEditText = findViewById<EditText>(R.id.UserName)
        val phoneEditText = findViewById<EditText>(R.id.PhoneNumber)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.ConfirmPassword)
        val signUpButton = findViewById<MaterialButton>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            // Disable the button to prevent multiple clicks
            signUpButton.isEnabled = false

            username = usernameEditText.text.toString().trim()
            val rawPhoneNumber = phoneEditText.text.toString().trim()
            phoneNumber = "+91$rawPhoneNumber"  // Dummy phone number format
            password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            when {
                TextUtils.isEmpty(username) -> {
                    showToast("Enter Username")
                    signUpButton.isEnabled = true
                }
                TextUtils.isEmpty(rawPhoneNumber) -> {
                    showToast("Enter Phone Number")
                    signUpButton.isEnabled = true
                }
                TextUtils.isEmpty(password) -> {
                    showToast("Enter Password")
                    signUpButton.isEnabled = true
                }
                TextUtils.isEmpty(confirmPassword) -> {
                    showToast("Confirm your Password")
                    signUpButton.isEnabled = true
                }
                password != confirmPassword -> {
                    showToast("Passwords do not match")
                    signUpButton.isEnabled = true
                }
                else -> {
                    // Use a fake email based on the username for registration
                    val email = "$username@example.com"

                    // Register user
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // User is successfully registered
                                val userId = auth.currentUser?.uid

                                // Save additional user details to Firestore
                                val userMap = hashMapOf(
                                    "username" to username,
                                    "phoneNumber" to phoneNumber
                                )
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()

                                userId?.let {
                                    firestore.collection("users").document(it)
                                        .set(userMap)
                                        .addOnCompleteListener { firestoreTask ->
                                            if (firestoreTask.isSuccessful) {
                                                // Show success toast
                                                showToast("Registration Successful")
                                            } else {
                                                // Handle Firestore failure
                                                showToast("Error saving user data: ${firestoreTask.exception?.message}")
                                                signUpButton.isEnabled = true
                                            }
                                        }
                                }
                            } else {
                                // If registration fails
                                showToast("Registration failed: ${task.exception?.message}")
                                signUpButton.isEnabled = true
                            }
                        }
                }
            }
        }

        val signInTextView = findViewById<TextView>(R.id.Signin)
        signInTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
