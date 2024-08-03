package com.jans.calendar.events.app.utils.calendarUtil

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.GridLayoutAnimationController
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.utils.calendarUtil.adapter.GridAdapter
import com.jans.calendar.events.app.utils.calendarUtil.model.ColoredDate
import com.jans.calendar.events.app.utils.calendarUtil.model.EventObjects
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class KalendarView : LinearLayout {
    private var previousButton: ImageView? = null
    private var nextButton: ImageView? = null
    private var currentDate: TextView? = null
    var calendarGridView: GridView? = null
    private val addEventButton: Button? = null
    private val month = 0
    private val year = 0
    private val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private var cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private var context: Context? = null
    private var mAdapter: GridAdapter? = null
    var prev: Int = -1
    var pos: Int = 0
    var cr_pos: Int = -2
    var dayValueInCells: MutableList<Date>? = null
    var mEvents: MutableList<EventObjects> = ArrayList()
    var selected: Date? = null
    var today_date: Calendar = Calendar.getInstance()

    /**
     * The function get the current selected date
     *
     * @return  selected date
     */
    var selectedDate: Date? = null
    var mDateSelector: DateSelector? = null
    var mMonthChanger: MonthChanger? = null
    var colorFulDates: MutableList<ColoredDate> = ArrayList()

    //customizations
    var todayIndicator: Drawable? = null
    var selectedIndicator: Drawable? = null
    var eventIndicator: Drawable? = null
    var dateColor: Int = 0
    var nonMonthDateColor: Int = 0
    var todayDateColor: Int = 0
    var selectedDateColor: Int = 0
    var monthFontFace: Typeface? = null
    var weekFontFace: Typeface? = null
    var dateFontFace: Typeface? = null
    var monthTextStyle: Int = 0
    var weekTextStyle: Int = 0
    var dateTextStyle: Int = 0
    var nextIcon: Drawable? = null
    var prevIcon: Drawable? = null
    var calendarBackgroundColor: Int = 0
    var animatingMonths: Boolean = true
    var animationController: GridLayoutAnimationController? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        cal = getZeroTime(cal)
        today_date = getZeroTime(today_date)
        selectedDate = today_date.time
        val tempTomorrow = Calendar.getInstance()
        tempTomorrow.time = today_date.time
        tempTomorrow.add(Calendar.DATE, 1)
        selectedDate = tempTomorrow.time

        todayIndicator = AppCompatResources.getDrawable(context, R.drawable.calendarview_today)
        selectedIndicator =
            AppCompatResources.getDrawable(context, R.drawable.calendarview_select_date)
        eventIndicator = AppCompatResources.getDrawable(context, R.drawable.calendarview_event)
        dateColor = Color.BLACK
        nonMonthDateColor = Color.LTGRAY
        todayDateColor = Color.BLACK
        selectedDateColor = Color.WHITE
        calendarBackgroundColor = Color.WHITE

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.KalendarView, 0, 0
        )

        val drwTodayId = typedArray.getResourceId(R.styleable.KalendarView_todayIndicator, 0)
        if (drwTodayId != 0) todayIndicator = AppCompatResources.getDrawable(context, drwTodayId)

        val drwSelectedId = typedArray.getResourceId(R.styleable.KalendarView_selectedIndicator, 0)
        if (drwSelectedId != 0) selectedIndicator =
            AppCompatResources.getDrawable(context, drwSelectedId)

        val drwEventId = typedArray.getResourceId(R.styleable.KalendarView_eventIndicator, 0)
        if (drwEventId != 0) eventIndicator = AppCompatResources.getDrawable(context, drwEventId)

        val colorDate = typedArray.getColor(R.styleable.KalendarView_dateColor, 0)
        if (colorDate != 0) dateColor = colorDate
        val colorNonMonth = typedArray.getColor(R.styleable.KalendarView_nonMonthDateColor, 0)
        if (colorNonMonth != 0) nonMonthDateColor = colorNonMonth
        val colorToday = typedArray.getColor(R.styleable.KalendarView_todayDateColor, 0)
        if (colorToday != 0) todayDateColor = colorToday
        val colorSelected = typedArray.getColor(R.styleable.KalendarView_selectedDateColor, 0)
        if (colorSelected != 0) selectedDateColor = colorSelected

        // Set a custom font family via its reference
        val monthFontId = typedArray.getResourceId(R.styleable.KalendarView_monthFontFamily, 0)
        if (monthFontId != 0) monthFontFace = ResourcesCompat.getFont(context, monthFontId)
        val weekFontId = typedArray.getResourceId(R.styleable.KalendarView_weekFontFamily, 0)
        if (weekFontId != 0) weekFontFace = ResourcesCompat.getFont(context, weekFontId)
        val dateFontId = typedArray.getResourceId(R.styleable.KalendarView_dateFontFamily, 0)
        if (dateFontId != 0) dateFontFace = ResourcesCompat.getFont(context, dateFontId)
        //for text styles
        monthTextStyle = typedArray.getResourceId(R.styleable.KalendarView_monthTextStyle, 0)
        weekTextStyle = typedArray.getResourceId(R.styleable.KalendarView_weekTextStyle, 0)
        dateTextStyle = typedArray.getResourceId(R.styleable.KalendarView_dateTextStyle, 0)

        //for next icon
        val tempNextIcon = typedArray.getResourceId(R.styleable.KalendarView_nextIcon, 0)
        if (tempNextIcon != 0) nextIcon = AppCompatResources.getDrawable(context, tempNextIcon)
        //for prev icon
        val tempPrevIcon = typedArray.getResourceId(R.styleable.KalendarView_prevIcon, 0)
        if (tempPrevIcon != 0) prevIcon = AppCompatResources.getDrawable(context, tempPrevIcon)

        //for calendarbackground
        val colorBg = typedArray.getColor(R.styleable.KalendarView_calendarBackground, 0)
        if (colorBg != 0) calendarBackgroundColor = colorBg

        //to set the animation controller
        animatingMonths = typedArray.getBoolean(R.styleable.KalendarView_animatingMonths, true)

        initializeUILayout()
        setUpCalendarAdapter()
        setPreviousButtonClickEvent()
        setNextButtonClickEvent()
        setGridCellClickEvents()


        Log.d(TAG, "I need to call this method")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initializeUILayout() {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.calendarview, this)
        previousButton = view.findViewById(R.id.previous_month)
        nextButton = view.findViewById(R.id.next_month)
        currentDate = view.findViewById(R.id.display_current_date)
        calendarGridView = view.findViewById(R.id.calendar_grid)
        val llRoot = view.findViewById<LinearLayout>(R.id.ll_root)
        val llCalendarHead = view.findViewById<LinearLayout>(R.id.ll_calendar_head)
        val llCalendarWeek = view.findViewById<LinearLayout>(R.id.ll_calendar_week)
        //apply textstyle
        if (monthTextStyle != 0) currentDate!!.setTextAppearance(context, monthTextStyle)
        if (weekTextStyle != 0) {
            (view.findViewById<View>(R.id.sun) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
            (view.findViewById<View>(R.id.mon) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
            (view.findViewById<View>(R.id.tue) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
            (view.findViewById<View>(R.id.wed) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
            (view.findViewById<View>(R.id.thu) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
            (view.findViewById<View>(R.id.fri) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
            (view.findViewById<View>(R.id.sat) as TextView).setTextAppearance(
                context,
                weekTextStyle
            )
        }
        //set font family
        if (monthFontFace != null) currentDate!!.setTypeface(monthFontFace)
        if (weekFontFace != null) {
            (view.findViewById<View>(R.id.sun) as TextView).setTypeface(weekFontFace)
            (view.findViewById<View>(R.id.mon) as TextView).setTypeface(weekFontFace)
            (view.findViewById<View>(R.id.tue) as TextView).setTypeface(weekFontFace)
            (view.findViewById<View>(R.id.wed) as TextView).setTypeface(weekFontFace)
            (view.findViewById<View>(R.id.thu) as TextView).setTypeface(weekFontFace)
            (view.findViewById<View>(R.id.fri) as TextView).setTypeface(weekFontFace)
            (view.findViewById<View>(R.id.sat) as TextView).setTypeface(weekFontFace)
        }

        //for next, prev icons
        if (nextIcon != null) nextButton!!.setImageDrawable(nextIcon)
        if (prevIcon != null) previousButton!!.setImageDrawable(prevIcon)

        //set calendar background
        llRoot.setBackgroundColor(calendarBackgroundColor)
        llCalendarHead.setBackgroundColor(calendarBackgroundColor)
        llCalendarWeek.setBackgroundColor(calendarBackgroundColor)
        calendarGridView!!.setBackgroundColor(calendarBackgroundColor)

        val animation = AnimationUtils.loadAnimation(getContext(), R.anim.grid_anim)
        animationController = GridLayoutAnimationController(animation, 0f, .1f)
    }

    private fun setPreviousButtonClickEvent() {
        previousButton!!.setOnClickListener { v: View? ->
            cal.add(Calendar.MONTH, -1)
            setUpCalendarAdapter()
            if (mMonthChanger != null) mMonthChanger!!.onMonthChanged(cal.time)
        }
    }

    private fun setNextButtonClickEvent() {
        nextButton!!.setOnClickListener { v: View? ->
            cal.add(Calendar.MONTH, 1)
            setUpCalendarAdapter()
            if (mMonthChanger != null) mMonthChanger!!.onMonthChanged(cal.time)
        }
    }

    fun setGridCellClickEvents() {
        calendarGridView!!.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                pos = view.tag as Int
                val llParent = view.findViewById<LinearLayout>(R.id.ll_parent)
                llParent.background = selectedIndicator
                val txt = view.findViewById<TextView>(R.id.calendar_date_id)
                txt.setTextColor(selectedDateColor)
                selectedDate = dayValueInCells!![pos]

                if ((prev != -1) && prev != cr_pos) {
                    val prevParent =
                        parent.getChildAt(prev).findViewById<LinearLayout>(R.id.ll_parent)
                    prevParent.setBackgroundColor(calendarBackgroundColor)
                    val txtd = parent.getChildAt(prev).findViewById<TextView>(R.id.calendar_date_id)
                    txtd.setTextColor(if (txtd.tag as Int == 0) dateColor else nonMonthDateColor)
                    val customDateColor = mAdapter!!.getDateColor(dayValueInCells!![prev])
                    if (customDateColor != 0 && txtd.tag as Int == 0) txtd.setTextColor(
                        customDateColor
                    )
                }
                if (prev == cr_pos) {
                    val childView = parent.getChildAt(prev)
                    if (childView != null) {
                        val todayParent = childView.findViewById<LinearLayout>(R.id.ll_parent)
                        todayParent.background = todayIndicator
                        val txtd = childView.findViewById<TextView>(R.id.calendar_date_id)
                        txtd.setTextColor(todayDateColor)
                        val customDateColor = mAdapter!!.getDateColor(dayValueInCells!![prev])
                        if (customDateColor != 0) txtd.setTextColor(customDateColor)
                    }
                }
                prev = pos

                val month_id = txt.tag as Int
                if (month_id == -1) {
                    cr_pos = pos
                }
                if (month_id == 1) {
                    cal.add(Calendar.MONTH, 1)
                    setUpCalendarAdapter()
                    cr_pos = -2
                    //to inform month changed
                    if (mMonthChanger != null) mMonthChanger!!.onMonthChanged(cal.time)
                }
                if (month_id == 2) {
                    cal.add(Calendar.MONTH, -1)
                    setUpCalendarAdapter()
                    cr_pos = -2
                    //to inform month changed
                    if (mMonthChanger != null) mMonthChanger!!.onMonthChanged(cal.time)
                }
                if (mDateSelector != null) mDateSelector!!.onDateClicked(selectedDate)
            }
    }

    private fun setUpCalendarAdapter() {
        dayValueInCells = ArrayList()
        val sDate1 = "27/02/2020"
        try {
            val date1 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(sDate1)
            val date2 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse("08/09/2023")

            val evd = EventObjects(10, "hello", date1)
            val evde = EventObjects(11, "hi", date2)

            //        mEvents.add(evd);
//        mEvents.add(evde);
        } catch (e: ParseException) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }

        val mCal = cal.clone() as Calendar
        mCal[Calendar.DAY_OF_MONTH] = 1
        val firstDayOfTheMonth = mCal[Calendar.DAY_OF_WEEK] - 1
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth)
        while (dayValueInCells!!.size < MAX_CALENDAR_COLUMN) {
            dayValueInCells!!.add(mCal.time)
            mCal.add(Calendar.DAY_OF_MONTH, 1)
        }
        Log.d(TAG, "Number of date " + dayValueInCells!!.size)
        val sDate = formatter.format(cal.time)
        currentDate!!.text = sDate
        prev = dayValueInCells!!.indexOf(selectedDate)
        cr_pos = dayValueInCells!!.indexOf(today_date.time)
        mAdapter =
            GridAdapter(context!!, dayValueInCells!!, cal, mEvents, selectedDate!!, colorFulDates)
        mAdapter!!.setDrawables(todayIndicator, selectedIndicator, eventIndicator)
        mAdapter!!.setTextColors(dateColor, nonMonthDateColor, todayDateColor, selectedDateColor)
        mAdapter!!.setFontProperties(dateFontFace, dateTextStyle)
        mAdapter!!.setCalendarBackgroundColor(calendarBackgroundColor)
        calendarGridView!!.adapter = mAdapter

        //for animating month changes
        if (animatingMonths && !isInEditMode) calendarGridView!!.layoutAnimation =
            animationController
    }


    interface DateSelector {
        fun onDateClicked(selectedDate: Date?)
    }

    interface MonthChanger {
        fun onMonthChanged(changedMonth: Date?)
    }

    fun setEvents(mEvents: MutableList<EventObjects>) {
        if (mAdapter != null) {
            mAdapter!!.allEvents.clear()
            this.mEvents.clear()
            mAdapter!!.allEvents = mEvents
            this.mEvents = mEvents
            mAdapter!!.notifyDataSetChanged()
        }
    }

    fun addEvents(mEvents: List<EventObjects>?) {
        if (mAdapter != null) {
            mAdapter!!.allEvents.addAll(mEvents!!)
            this.mEvents.addAll(mEvents!!)
            mAdapter!!.notifyDataSetChanged()
        }
    }


    fun setColoredDates(colorDates: MutableList<ColoredDate>) {
        if (mAdapter != null) {
            mAdapter!!.colorFulDates = colorDates
            colorFulDates = colorDates
            mAdapter!!.notifyDataSetChanged()
        }
    }

    fun addColoredDates(colorDates: List<ColoredDate>?) {
        if (mAdapter != null) {
            mAdapter!!.colorFulDates.addAll(colorDates!!)
            colorFulDates.addAll(colorDates!!)
            mAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * Listener for the date click events in the KalendarView <br></br>
     * it gives the new clicked date
     *
     */
    fun setDateSelector(mSelector: DateSelector?) {
        this.mDateSelector = mSelector
    }

    /**
     * Listener for the month changes in the KalendarView <br></br>
     * it gives the new month changed date
     *
     */
    fun setMonthChanger(mChanger: MonthChanger?) {
        this.mMonthChanger = mChanger
    }

    /**
     * The function set the first selected
     * date as initial date
     *
     * @param initialDate date object
     */
    fun setInitialSelectedDate(initialDate: Date) {
        selectedDate = getZeroTime(initialDate)
        cal.time = selectedDate
        setUpCalendarAdapter()
    }

    val showingMonth: Date
        /**
         * The function get the current showing month date
         *
         * @return  showing month's date
         */
        get() = cal.time

    private fun getZeroTime(date: Date): Date {
        val tempCal = Calendar.getInstance()
        tempCal.time = date
        tempCal[Calendar.HOUR] = 0
        tempCal[Calendar.MINUTE] = 0
        tempCal[Calendar.SECOND] = 0
        tempCal[Calendar.MILLISECOND] = 0
        return tempCal.time
    }

    private fun getZeroTime(mCalendar: Calendar): Calendar {
        val tempCal = mCalendar.clone() as Calendar
        tempCal[Calendar.HOUR] = 0
        tempCal[Calendar.MINUTE] = 0
        tempCal[Calendar.SECOND] = 0
        tempCal[Calendar.MILLISECOND] = 0
        return tempCal
    }

    companion object {
        private val TAG: String = KalendarView::class.java.simpleName
        private const val MAX_CALENDAR_COLUMN = 42
    }
}
