package com.masum.datepicker

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.masum.datepicker.MonthAdapter.OnSelectedListener
import com.masum.datepicker.listener.DateMonthDialogListener
import com.masum.datepicker.listener.OnCancelMonthDialogListener
import java.util.*

class MonthPicker {
    private var mAlertDialog: AlertDialog? = null
    private var builder: Builder
    private var context: Context
    private var mPositiveButton: TextView? = null
    private var mNegativeButton: TextView? = null
    private var dateMonthDialogListener: DateMonthDialogListener? = null
    private var onCancelMonthDialogListener: OnCancelMonthDialogListener? = null
    private var isBuild = false

    constructor(context: Context) {
        this.context = context
        builder = Builder()
    }

    constructor(activity: Activity) {
        context = activity
        builder = Builder()
    }

    fun show() {
        if (isBuild) {
            mAlertDialog!!.show()
            builder.setDefault()
        } else {
            builder.build()
            isBuild = true
        }
    }

    fun setPositiveButton(dateMonthDialogListener: DateMonthDialogListener?): MonthPicker {
        this.dateMonthDialogListener = dateMonthDialogListener
        mPositiveButton!!.setOnClickListener(builder.positiveButtonClick())
        return this
    }

    fun setNegativeButton(onCancelMonthDialogListener: OnCancelMonthDialogListener?): MonthPicker {
        this.onCancelMonthDialogListener = onCancelMonthDialogListener
        mNegativeButton!!.setOnClickListener(builder.negativeButtonClick())
        return this
    }

    fun setPositiveText(text: String?): MonthPicker {
        mPositiveButton!!.text = text
        return this
    }

    fun setNegativeText(text: String?): MonthPicker {
        mNegativeButton!!.text = text
        return this
    }

    fun setLocale(locale: Locale?): MonthPicker {
        builder.setLocale(locale)
        return this
    }

    fun setSelectedMonth(month: Int): MonthPicker {
        builder.setSelectedMonth(month)
        return this
    }

    fun setSelectedYear(year: Int): MonthPicker {
        builder.setSelectedYear(year)
        return this
    }

    fun setColorTheme(color: Int): MonthPicker {
        builder.setColorTheme(color)
        return this
    }

    fun setMonthType(monthType: MonthType?): MonthPicker {
        builder.setMonthType(monthType)
        return this
    }

    fun setLimitDate(newBeginDate: Date? = null, newEndDate: Date? = null): MonthPicker {
        builder.setLimitDate(newBeginDate, newEndDate)
        return this
    }

    fun dismiss() {
        mAlertDialog!!.dismiss()
    }

    private inner class Builder : OnSelectedListener {
        private val monthAdapter: MonthAdapter
        private val mTitleView: TextView
        private val mYear: TextView
        private var year: Int
        private var month: Int
        private var beginDate: Date? = null
        private var endDate: Date? = null
        private val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        private val contentView: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_month_picker, null)

        private fun getColorByThemeAttr(context: Context, attr: Int, defaultColor: Int): Int {
            val typedValue = TypedValue()
            val theme = context.theme
            val got = theme.resolveAttribute(attr, typedValue, true)
            return if (got) typedValue.data else defaultColor
        }

        //set default config
        fun setDefault() {
            val date = Date()
            val cal = Calendar.getInstance()
            cal.time = date
            year = cal[Calendar.YEAR]
            month = cal[Calendar.MONTH]
            monthAdapter.setSelectedItem(month)
            setTitle(monthAdapter.shortMonth, year)
            monthAdapter.notifyDataSetChanged()
        }

        fun setLocale(locale: Locale?) {
            monthAdapter.setLocale(locale)
        }

        fun setSelectedMonth(index: Int) {
            monthAdapter.setSelectedItem(index)
            setTitle(monthAdapter.shortMonth, year)
        }

        fun setSelectedYear(year: Int) {
            this.year = year
            setTitle(monthAdapter.shortMonth, year)
        }

        fun setColorTheme(color: Int) {
            val linearToolbar = contentView.findViewById<View>(R.id.linear_toolbar) as LinearLayout
            linearToolbar.setBackgroundColor(color)
            monthAdapter.setBackgroundMonth(color)
            mPositiveButton!!.setTextColor(color)
            mNegativeButton!!.setTextColor(color)
        }

        fun setMonthType(monthType: MonthType?) {
            monthAdapter.setMonthType(monthType!!)
        }

        fun setLimitDate(newBeginDate: Date? = null, newEndDate: Date? = null) {
            if (newBeginDate != null && newEndDate != null && newBeginDate.after(newEndDate)) return
            beginDate = newBeginDate
            endDate = newEndDate
            checkMonthsEnable()

        }

        fun build() {
            monthAdapter.setSelectedItem(month)
            setTitle(monthAdapter.shortMonth, year)
            checkMonthsEnable()
            mAlertDialog = alertBuilder.create()
            mAlertDialog!!.show()
            mAlertDialog!!.window!!.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
            )
            mAlertDialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE)
            mAlertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mAlertDialog!!.window!!.setBackgroundDrawableResource(R.drawable.material_dialog_window)
            mAlertDialog!!.window!!.setContentView(contentView)
        }

        fun nextButtonClick(): View.OnClickListener {
            return View.OnClickListener {
                val endDateCalendar = endDate?.let {
                    Calendar.getInstance().apply { time = it }
                }
                if (endDate == null || (endDateCalendar?.get(Calendar.YEAR) != null && endDateCalendar.get(
                        Calendar.YEAR
                    ) > year)
                ) {
                    year++
                    setTitle(monthAdapter.shortMonth, year)
                    checkMonthsEnable()
                }
            }
        }

        fun previousButtonClick(): View.OnClickListener {
            return View.OnClickListener {
                val beginDateCalendar = beginDate?.let {
                    Calendar.getInstance().apply { time = it }
                }
                if (beginDate == null || (beginDateCalendar?.get(Calendar.YEAR) != null && beginDateCalendar.get(
                        Calendar.YEAR
                    ) < year)
                ) {
                    year--
                    setTitle(monthAdapter.shortMonth, year)
                    checkMonthsEnable()
                }
            }
        }

        private fun checkMonthsEnable() {
            val array = Array(12) { true }
            val beginDateCalendar = beginDate?.let {
                Calendar.getInstance().apply { time = it }
            }
            val endDateCalendar = endDate?.let {
                Calendar.getInstance().apply { time = it }
            }
            if (beginDateCalendar?.get(Calendar.YEAR) != null && beginDateCalendar.get(Calendar.YEAR) == year) {
                for (i in 0 until beginDateCalendar.get(Calendar.MONTH)) {
                    array[i] = false
                }
            }
            if (endDateCalendar?.get(Calendar.YEAR) != null && endDateCalendar.get(Calendar.YEAR) == year) {
                for (i in endDateCalendar.get(Calendar.MONTH) + 1 until 12) {
                    array[i] = false
                }
            }
            checkSelectedDateIsInLimit()

            monthAdapter.setMonthEnable(array)
        }

        private fun checkSelectedDateIsInLimit() {
            val selectedDate =
                Calendar.getInstance().apply { set(year, month, 0, 0, 0, 0) }.time
            if (beginDate != null && selectedDate.before(beginDate)) {
                Calendar.getInstance().apply { time = beginDate!! }.let {
                    setSelectedMonth(it.get(Calendar.MONTH))
                    setSelectedYear(it.get(Calendar.YEAR))
                }
            } else if (endDate != null && selectedDate.after(endDate)) {
                Calendar.getInstance().apply { time = endDate!! }.let {
                    setSelectedMonth(it.get(Calendar.MONTH))
                    setSelectedYear(it.get(Calendar.YEAR))
                }
            }
        }

        fun positiveButtonClick(): View.OnClickListener {
            return View.OnClickListener {
                dateMonthDialogListener!!.onDateMonth(
                    monthAdapter.month,
                    monthAdapter.startDate,
                    monthAdapter.endDate,
                    year, mTitleView.text.toString()
                )
                mAlertDialog!!.dismiss()
            }
        }

        fun negativeButtonClick(): View.OnClickListener {
            return View.OnClickListener { onCancelMonthDialogListener!!.onCancel(mAlertDialog) }
        }

        override fun onContentSelected() {
            month = monthAdapter.month - 1
            setTitle(monthAdapter.shortMonth, year)
        }

        private fun setTitle(month: String, year: Int) {
            mTitleView.text = String.format("%s %d",
                month.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }, year)
            mYear.text = year.toString()
        }

        init {
            contentView.isFocusable = true
            contentView.isFocusableInTouchMode = true
            mTitleView = contentView.findViewById<View>(R.id.title) as TextView
            mYear = contentView.findViewById<View>(R.id.text_year) as TextView
            val next = contentView.findViewById<View>(R.id.btn_next) as ImageView
            next.setOnClickListener(nextButtonClick())
            val previous = contentView.findViewById<View>(R.id.btn_previous) as ImageView
            previous.setOnClickListener(previousButtonClick())
            mPositiveButton = contentView.findViewById<View>(R.id.btn_p) as TextView
            mNegativeButton = contentView.findViewById<View>(R.id.btn_n) as TextView
            monthAdapter = MonthAdapter(context, this)
            val recyclerView = contentView.findViewById<View>(R.id.recycler_view) as RecyclerView
            recyclerView.layoutManager = GridLayoutManager(context, 4)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = monthAdapter
            val date = Date()
            val cal = Calendar.getInstance()
            cal.time = date
            year = cal[Calendar.YEAR]
            month = cal[Calendar.MONTH]
            setColorTheme(
                getColorByThemeAttr(
                    context,
                    android.R.attr.colorPrimary,
                    R.color.color_primary
                )
            )
        }
    }
}