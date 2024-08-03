package com.jans.calendar.events.app.utils.calendarUtil.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.utils.calendarUtil.model.ColoredDate
import com.jans.calendar.events.app.utils.calendarUtil.model.EventObjects
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GridAdapter(
    var cos: Context,
    private val monthlyDates: MutableList<Date>,
    private val currentDate: Calendar,
    var allEvents: MutableList<EventObjects>,
    var color_date: Date,
    colorFulDates: MutableList<ColoredDate>
) : ArrayAdapter<Any?>(
    cos, R.layout.calendarview_cell
) {
    private val mInflater: LayoutInflater = LayoutInflater.from(cos)

    //for change the color for specific dates
    var colorFulDates: MutableList<ColoredDate> = ArrayList()
    var sdfDmy: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

    //customizations
    var todayIndicator: Drawable? = null
    var selectedIndicator: Drawable? = null
    var eventIndicator: Drawable? = null
    var dateColor: Int = 0
    var nonMonthDateColor: Int = 0
    var todayDateColor: Int = 0
    var selectedDateColor: Int = 0
    var dateFontFace: Typeface? = null
    var dateTextStyle: Int = 0
    var calBGColor: Int = 0

    init {
        this.colorFulDates = colorFulDates
    }

    fun setDrawables(
        todayIndicator: Drawable?,
        selectedIndicator: Drawable?,
        eventIndicator: Drawable?
    ) {
        this.todayIndicator = todayIndicator
        this.selectedIndicator = selectedIndicator
        this.eventIndicator = eventIndicator
    }

    fun setTextColors(
        dateColor: Int,
        nonMonthDateColor: Int,
        todayDateColor: Int,
        selectedDateColor: Int
    ) {
        this.dateColor = dateColor
        this.nonMonthDateColor = nonMonthDateColor
        this.todayDateColor = todayDateColor
        this.selectedDateColor = selectedDateColor
    }

    fun setFontProperties(fontFace: Typeface?, textStyle: Int) {
        dateFontFace = fontFace
        dateTextStyle = textStyle
    }

    fun setCalendarBackgroundColor(bgColor: Int) {
        this.calBGColor = bgColor
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mDate = monthlyDates[position]
        val dateCal = Calendar.getInstance()
        dateCal.time = mDate
        val dayValue = dateCal[Calendar.DAY_OF_MONTH]
        val displayMonth = dateCal[Calendar.MONTH] + 1
        val displayYear = dateCal[Calendar.YEAR]
        val currentMonth = currentDate[Calendar.MONTH] + 1
        val currentYear = currentDate[Calendar.YEAR]
        val today_date = Calendar.getInstance()
        val toYear = today_date[Calendar.YEAR]
        val toMonth = today_date[Calendar.MONTH] + 1
        val toDay = today_date[Calendar.DATE]
        val colorday = color_date.date
        val colorMonth = color_date.month + 1
        val coloryear = color_date.year
        var view = convertView

        if (view == null) {
            view = mInflater.inflate(R.layout.calendarview_cell, parent, false)
            val llParent = view.findViewById<LinearLayout>(R.id.ll_parent)
            val clRoot = view.findViewById<ConstraintLayout>(R.id.cl_root)
            llParent.setBackgroundColor(calBGColor)
            clRoot.setBackgroundColor(calBGColor)
        }
        val llParent = view!!.findViewById<LinearLayout>(R.id.ll_parent)
        val clRoot = view.findViewById<ConstraintLayout>(R.id.cl_root)
        val cellNumber = view.findViewById<View>(R.id.calendar_date_id) as TextView
        //set font family
        if (dateTextStyle != 0) cellNumber.setTextAppearance(context, dateTextStyle)
        if (dateFontFace != null) cellNumber.typeface = dateFontFace
        //set background
        llParent.setBackgroundColor(calBGColor)
        clRoot.setBackgroundColor(calBGColor)

        if (displayMonth == currentMonth && displayYear == currentYear) {
            cellNumber.setTextColor(if (dateColor != 0) dateColor else Color.BLACK)
            cellNumber.tag = 0
        } else {
            if (displayMonth > currentMonth && displayYear == currentYear || (displayMonth < currentMonth && displayYear > currentYear)) {
                cellNumber.tag = 1
            } else {
                cellNumber.tag = 2
            }
            cellNumber.setTextColor(if (nonMonthDateColor != 0) nonMonthDateColor else Color.LTGRAY)
        }
        //Add day to calendar
        //TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
        cellNumber.text = dayValue.toString()


        if (displayMonth == toMonth && displayYear == toYear && dayValue == toDay) {
            if (todayIndicator != null) llParent.background = todayIndicator
            else llParent.setBackgroundResource(R.drawable.calendarview_today)
            cellNumber.tag = -1
            if (todayDateColor != 0) cellNumber.setTextColor(todayDateColor)
        }
        //set custom date color before set the selected color
        val customDateColor = getDateColor(mDate)
        if (customDateColor != 0) cellNumber.setTextColor(customDateColor)

        if (displayMonth == colorMonth && colorday == dayValue) {
            if (selectedIndicator != null) llParent.background = selectedIndicator
            else llParent.setBackgroundResource(R.drawable.calendarview_select_date)
            cellNumber.setTextColor(if (selectedDateColor != 0) selectedDateColor else Color.WHITE)
        }

        view.tag = position


        //        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });


        //Add events to the calendar
        val tvEventIndicator = view.findViewById<View>(R.id.event_id) as TextView
        val eventCalendar = Calendar.getInstance()
        for (i in allEvents.indices) {
            eventCalendar.time = allEvents[i].date
            if (dayValue == eventCalendar[Calendar.DAY_OF_MONTH] && displayMonth == eventCalendar[Calendar.MONTH] + 1 && displayYear == eventCalendar[Calendar.YEAR]) {
                if (eventIndicator != null) tvEventIndicator.background = eventIndicator
                else tvEventIndicator.setBackgroundResource(R.drawable.calendarview_event)
            }
        }
        return view
    }

    override fun getCount(): Int {
        return monthlyDates.size
    }

    override fun getItem(position: Int): Any? {
        return monthlyDates[position]
    }

    override fun getPosition(item: Any?): Int {
        return monthlyDates.indexOf(item)
    }

    fun getDateColor(mDate: Date?): Int {
        for (i in colorFulDates.indices) {
            if (sdfDmy.format(mDate) == sdfDmy.format(colorFulDates[i].date)) {
                return colorFulDates[i].color
            }
        }
        return 0
    }

    companion object {
        private val TAG: String = GridAdapter::class.java.simpleName
    }
}
