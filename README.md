## custom month and year picker

Step 1.Add it in your root build.gradle at the end of repositories

    allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }

Step 2. Add the dependency


	dependencies {
	        implementation 'com.github.mdmasum-shuvo:month_yearPicker:1.0.0' //latest version
	}


Step 3. Call the MonthPickerDialog

    lateinit var monthPicker: MonthPicker


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
    
    
    #show picker 
        monthPicker.show()
    
    #dismiss picker
        monthPicker.dismiss()
    
    #month & year selection
        monthPicker.setSelectedMonth(1)
        monthPicker.setSelectedYear(2021)
