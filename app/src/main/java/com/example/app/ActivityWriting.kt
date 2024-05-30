package com.example.app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class ActivityWriting : AppCompatActivity() {

    private val viewModel = DateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_writing)
        writing_cancel()

        val radioGroupPrice = findViewById<RadioGroup>(R.id.radio_group_price)
        val textPrice = findViewById<EditText>(R.id.edit_text_price)

        radioGroupPrice.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_free -> {
                    // 무료 라디오 버튼이 선택된 경우
                    textPrice.isEnabled = false // 비활성화
                    textPrice.setText("0") // 내용 지우기
                }
                R.id.radio_charged -> {
                    // 유료 라디오 버튼이 선택된 경우
                    textPrice.isEnabled = true // 활성화
                }
            }
        }

        val choicePeriodButton = findViewById<Button>(R.id.choice_period)
        val textView = findViewById<TextView>(R.id.text_period_show)

        choicePeriodButton.setOnClickListener {
            showStartDatePicker(textView)
        }
    }

    private fun showStartDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val startDatePickerDialog = DatePickerDialog(
            this,
            { _, startYear, startMonth, startDay ->
                val startDate = "${startYear}년 ${startMonth + 1}월 ${startDay}일"
                showEndDatePicker(textView, startYear, startMonth, startDay, startDate)
            },
            viewModel.year,
            viewModel.month,
            viewModel.day
        )

        // 현재 날짜 이전은 선택하지 못하도록 설정
        startDatePickerDialog.datePicker.minDate = calendar.timeInMillis

        val headerView = startDatePickerDialog.datePicker.findViewById<View>(
            resources.getIdentifier("date_picker_header", "id", "android")
        )
        headerView?.visibility = View.GONE

        startDatePickerDialog.show()
    }

    private fun showEndDatePicker(textView: TextView, startYear: Int, startMonth: Int, startDay: Int, startDate: String) {
        val calendar = Calendar.getInstance()
        val endDatePickerDialog = DatePickerDialog(
            this,
            { _, endYear, endMonth, endDay ->
                val endDate = "${endYear + 1}년 ${endMonth + 1}월 ${endDay}일"
                textView.text = "$startDate - $endDate"
            },
            startYear,
            startMonth,
            startDay
        )

        // 현재 날짜 이전은 선택하지 못하도록 설정
        endDatePickerDialog.datePicker.minDate = calendar.timeInMillis

        val headerView = endDatePickerDialog.datePicker.findViewById<View>(
            resources.getIdentifier("date_picker_header", "id", "android")
        )
        headerView?.visibility = View.GONE

        endDatePickerDialog.show()
    }

    private fun writing_cancel() {
        val button = findViewById<Button>(R.id.button_cancel)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

class DateViewModel {
    val year: Int = 2024
    val month: Int = 5 // May (0-based index, so 4 means May)
    val day: Int = 28
}
