## month and year picker Dependency library

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
        monthPicker = MonthPicker(this)   //pass the activity context
        monthPicker.setMonthType(MonthType.TEXT)  // set the type of month in dialog int or string
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

## App Screenshots

<img src="https://raw.githubusercontent.com/mdmasum-shuvo/month_yearPicker/master/ss/index.jpg" width=30% height=30%> 
