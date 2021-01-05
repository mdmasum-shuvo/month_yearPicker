package com.example.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.masum.datepicker.MonthPicker
import com.masum.datepicker.MonthType
import com.masum.datepicker.listener.DateMonthDialogListener
import com.masum.datepicker.listener.OnCancelMonthDialogListener

class MainActivity : AppCompatActivity() {
    lateinit var monthPicker: MonthPicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnMonth = findViewById<Button>(R.id.btn_month)
        btnMonth.setOnClickListener {
            monthPicker.show()
        }
        callMonthPicker()
    }


    fun callMonthPicker() {
        monthPicker = MonthPicker(this)
        monthPicker.setMonthType(MonthType.TEXT)
        monthPicker.setPositiveButton(object : DateMonthDialogListener {
            override fun onDateMonth(
                month: Int,
                startDate: Int,
                endDate: Int,
                year: Int,
                monthLabel: String?
            ) {
                Log.e("data", month.toString() + " " + year.toString())
            }
        })

        monthPicker.setNegativeButton(object : OnCancelMonthDialogListener {
            override fun onCancel(dialog: AlertDialog?) {
                monthPicker.dismiss()
            }

        })
    }
}