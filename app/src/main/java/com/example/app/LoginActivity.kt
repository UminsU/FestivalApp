package com.example.app

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.editTextId.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        goMainPage()
                    } else {
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "로그인에 실패하였습니다. 이메일과 비밀번호를 확인해주세요.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        binding.signupButton.setOnClickListener {
            goSignup()
        }
    }

    private fun goMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, SubMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
