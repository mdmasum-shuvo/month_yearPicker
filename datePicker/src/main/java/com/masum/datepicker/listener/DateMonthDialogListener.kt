package com.masum.datepicker.listener

interface DateMonthDialogListener {
    fun onDateMonth(month: Int, startDate: Int, endDate: Int, year: Int, monthLabel: String?)
}