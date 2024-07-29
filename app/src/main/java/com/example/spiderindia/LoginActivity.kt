package com.example.spiderindia
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val signupTextView = findViewById<TextView>(R.id.Signup)
        val usernameEditText = findViewById<EditText>(R.id.UserName)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val loginButton = findViewById<MaterialButton>(R.id.signInButton)

        signupTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = "${usernameEditText.text.toString().trim()}@example.com" // Use the email format from sign-up
            val password = passwordEditText.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                showToast("Enter Username")
            } else if (TextUtils.isEmpty(password)) {
                showToast("Enter Password")
            } else {
                loginUser(email, password)
            }
        }

        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotpassword)
        val text = "Forgot Password?"
        val spannableString = SpannableString(text)

        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        forgotPasswordTextView.text = spannableString
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()  // Finish this activity so the user can't go back to it
                } else {
                    // If sign in fails, display a message to the user.
                    showToast("Authentication failed: ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
