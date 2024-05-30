package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        go_mainpage()
        go_googleloginpage()
        go_signuppage()
    }
    fun go_mainpage() {
        val button = findViewById<Button>(R.id.login_button)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun go_googleloginpage() {
        val button = findViewById<Button>(R.id.google_sign_in_button)
        button.setOnClickListener {
            val intent = Intent(this, Google_signupActivity::class.java)
            startActivity(intent)
        }
    }

    fun go_signuppage() {
        val button = findViewById<Button>(R.id.signup_button)
        button.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }


}