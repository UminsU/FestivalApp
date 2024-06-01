package com.example.app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class BoardActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    private lateinit var editTextFestivalName: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var radioFree: RadioButton
    private lateinit var radioCharged: RadioButton
    private lateinit var editTextPrice: EditText
    private lateinit var editTextIntroduction: EditText
    private lateinit var editTextInformation: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonWritingOut: Button
    private lateinit var choicePeriodButton: Button
    private lateinit var textPeriodShow: TextView

    private val viewModel = DateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing) // 올바른 레이아웃 파일을 참조

        // View 초기화
        editTextFestivalName = findViewById(R.id.edit_text_festival_name)
        editTextLocation = findViewById(R.id.edit_text_location)
        radioFree = findViewById(R.id.radio_free)
        radioCharged = findViewById(R.id.radio_charged)
        editTextPrice = findViewById(R.id.edit_text_price)
        editTextIntroduction = findViewById(R.id.edit_text_introduction)
        editTextInformation = findViewById(R.id.edit_text_information)
        buttonCancel = findViewById(R.id.button_cancel)
        buttonWritingOut = findViewById(R.id.button_writing_out)
        choicePeriodButton = findViewById(R.id.choice_period)
        textPeriodShow = findViewById(R.id.text_period_show)

        // 이벤트 리스너 설정
        buttonCancel.setOnClickListener {
            clearFields()
        }

        buttonWritingOut.setOnClickListener {
            saveFestivalData()
        }

        choicePeriodButton.setOnClickListener {
            showStartDatePicker()
        }

        val radioGroupPrice = findViewById<RadioGroup>(R.id.radio_group_price)
        radioGroupPrice.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_free -> {
                    editTextPrice.isEnabled = false
                    editTextPrice.setText("0")
                }
                R.id.radio_charged -> {
                    editTextPrice.isEnabled = true
                }
            }
        }
    }

    private fun clearFields() {
        editTextFestivalName.text.clear()
        editTextLocation.text.clear()
        radioFree.isChecked = false
        radioCharged.isChecked = false
        editTextPrice.text.clear()
        editTextIntroduction.text.clear()
        editTextInformation.text.clear()
        textPeriodShow.text = ""
    }

    private fun saveFestivalData() {
        val festivalName = editTextFestivalName.text.toString().trim()
        val location = editTextLocation.text.toString().trim()
        val isFree = radioFree.isChecked
        val price = if (isFree) 0 else editTextPrice.text.toString().toIntOrNull() ?: 0
        val introduction = editTextIntroduction.text.toString().trim()
        val information = editTextInformation.text.toString().trim()
        val period = textPeriodShow.text.toString().trim()

        if (festivalName.isEmpty() || location.isEmpty() || introduction.isEmpty() || information.isEmpty() || period.isEmpty()) {
            Toast.makeText(this, "모든 필드를 작성해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val festival = hashMapOf(
            "name" to festivalName,
            "location" to location,
            "isFree" to isFree,
            "price" to price,
            "introduction" to introduction,
            "information" to information,
            "period" to period
        )

        db.collection("festivals")
            .add(festival)
            .addOnSuccessListener {
                Toast.makeText(this, "축제가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
                clearFields()
                goToReadBoardActivity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "축제 저장에 실패했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToReadBoardActivity() {
        val intent = Intent(this, ReadBoardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        val startDatePickerDialog = DatePickerDialog(
            this,
            { _, startYear, startMonth, startDay ->
                val startDate = "${startYear}년 ${startMonth + 1}월 ${startDay}일"
                showEndDatePicker(startYear, startMonth, startDay, startDate)
            },
            viewModel.year,
            viewModel.month,
            viewModel.day
        )

        startDatePickerDialog.datePicker.minDate = calendar.timeInMillis
        startDatePickerDialog.show()
    }

    private fun showEndDatePicker(startYear: Int, startMonth: Int, startDay: Int, startDate: String) {
        val calendar = Calendar.getInstance()
        val endDatePickerDialog = DatePickerDialog(
            this,
            { _, endYear, endMonth, endDay ->
                val endDate = "${endYear}년 ${endMonth + 1}월 ${endDay}일"
                textPeriodShow.text = "$startDate - $endDate"
            },
            startYear,
            startMonth,
            startDay
        )

        endDatePickerDialog.datePicker.minDate = calendar.timeInMillis
        endDatePickerDialog.show()
    }
}

class DateViewModel {
    val year: Int = 2024
    val month: Int = 4 // 0-based index, so 4 means May
    val day: Int = 28
}
