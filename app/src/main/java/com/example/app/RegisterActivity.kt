package com.example.app

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var idEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        idEditText = findViewById(R.id.edit_text_id2)
        passwordEditText = findViewById(R.id.edit_text_password2)
        confirmPasswordEditText = findViewById(R.id.edit_text_confirm_password)
        nameEditText = findViewById(R.id.edit_text_name)
        phoneEditText = findViewById(R.id.edit_text_phone)
        birthdateEditText = findViewById(R.id.edit_text_birthdate)
        genderRadioGroup = findViewById(R.id.radio_group_gender)

        findViewById<Button>(R.id.button_check_id).setOnClickListener {
            checkIdAvailability()
        }

        findViewById<Button>(R.id.button_register).setOnClickListener {
            registerUser()
        }
    }

    private fun checkIdAvailability() {
        val userId = idEditText.text.toString().trim()
        if (TextUtils.isEmpty(userId)) {
            idEditText.error = "ID is required"
            return
        }

        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                Toast.makeText(this, "ID already exists", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "ID is available", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to check ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser() {
        val userId = idEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val birthdate = birthdateEditText.text.toString().trim()
        val gender = if (genderRadioGroup.checkedRadioButtonId == R.id.radio_male) "Male" else "Female"

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)
            || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(birthdate)
            || TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            return
        }

        auth.createUserWithEmailAndPassword(userId, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())

                    val userData = mapOf(
                        "name" to name,
                        "phone" to phone,
                        "birthdate" to birthdate,
                        "gender" to gender
                    )

                    database.child(userId).setValue(userData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                                // Navigate to the next screen
                            } else {
                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
