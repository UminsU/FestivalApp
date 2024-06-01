package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReadBoardActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    private lateinit var festivalImage: ImageView
    private lateinit var festivalName: TextView
    private lateinit var festivalParking: TextView
    private lateinit var festivalIntroduction: TextView
    private lateinit var festivalInformation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_read_board)

        festivalImage = findViewById(R.id.readboard_image)
        festivalName = findViewById(R.id.readboard_name)
        festivalParking = findViewById(R.id.readboard_parking_text)
        festivalIntroduction = findViewById(R.id.readboard_introdution_text)
        festivalInformation = findViewById(R.id.readboard_information_text)

        goBoardPage()

        val festivalId = intent.getStringExtra("festivalId")
        val imageResId = intent.getIntExtra("imageResId", -1)

        festivalId?.let { loadFestivalData(it) }
        if (imageResId != -1) {
            festivalImage.setImageResource(imageResId)}
    }

    private fun goBoardPage() {
        val button = findViewById<Button>(R.id.readboard_backbutton)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    class ReadBoardActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_read_board)

            val favoriteButton = findViewById<Button>(R.id.favorite_button)
            favoriteButton.setOnClickListener {
                Toast.makeText(this, "게시물을 찜했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFestivalData(festivalId: String) {
        db.collection("festivals")
            .document(festivalId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val parking = document.getString("parking") ?: "주차 정보 없음"
                    val introduction = document.getString("introduction")
                    val information = document.getString("information")
                    val isFree = document.getBoolean("isFree") ?: false
                    val location = document.getString("location")
                    val period = document.getString("period")
                    val price = document.getLong("price")

                    // 여기서 가져온 데이터를 TextView 등에 설정해주면 됩니다.
                    festivalName.text = name
                    festivalParking.text = parking
                    festivalIntroduction.text = introduction
                    festivalInformation.text = information

                    // 다른 데이터도 필요에 따라 처리해주세요.
                } else {
                    Toast.makeText(this, "해당 축제를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "데이터를 가져오지 못했습니다: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}