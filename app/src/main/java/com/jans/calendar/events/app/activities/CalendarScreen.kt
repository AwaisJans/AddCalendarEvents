package com.jans.calendar.events.app.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.calendar.events.app.adapters.CalendarAdapter
import com.jans.calendar.events.app.databinding.ActivityCalendarBinding
import com.jans.calendar.events.app.databinding.DialogCalendarEventBinding
import com.jans.calendar.events.app.model.EventCal
import com.jans.calendar.events.app.utils.ConfigApp.Companion.doesDateExist
import com.jans.calendar.events.app.utils.ConfigApp.Companion.eventsList
import com.jans.calendar.events.app.utils.ConfigApp.Companion.extractDateComponents
import com.jans.calendar.events.app.utils.calendarUtil.model.EventObjects
import com.jans.calendar.events.app.utils.calendarUtil.KalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarScreen : AppCompatActivity() {

    private lateinit var b: ActivityCalendarBinding
    private lateinit var calendarEventListCal: List<EventCal>

    var events: MutableList<EventObjects> = ArrayList<EventObjects>()

    private var mKalendarView: KalendarView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(b.root)

        setUpCalendarRV()
        setupCalendar()
        buttonsInit()

    }

    private fun setupCalendar() {
        for (event in calendarEventListCal){
            val dateString = event.date
            val (day, month, year) = extractDateComponents(dateString)

            addEvents(day, month-1, year, event.title, event.description, event.location)
        }

        mKalendarView!!.addEvents(events)
    }


    private fun addEvents(day: Int, month: Int, year: Int,title: String,description:String, location:String) {
        val cal = Calendar.getInstance()
        cal[Calendar.DATE] = day
        cal[Calendar.MONTH] = month
        cal[Calendar.YEAR] = year

        events.add(EventObjects(title, cal.time, description, location))
    }


    private fun buttonsInit() {
        b.backBtn.setOnClickListener {
            finish()
        }


        mKalendarView!!.setDateSelector(object : KalendarView.DateSelector {
            override fun onDateClicked(selectedDate: Date?) {
                val (exists, index) = doesDateExist(events, selectedDate!!)
                if (exists) {
                    showEventDialog(index)
                }else{
                    Toast.makeText(this@CalendarScreen, "No event found", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun setUpCalendarRV() {
        calendarEventListCal = eventsList(this)
        mKalendarView = b.kalendar
        val recyclerView = b.recyclerView
        val namesAdapter = CalendarAdapter(calendarEventListCal)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = namesAdapter
    }


    private fun showEventDialog(position: Int?) {
        val dialog = Dialog(this)
        val bD = DialogCalendarEventBinding.inflate(layoutInflater)
        dialog.setContentView(bD.root)

        val event = calendarEventListCal[position!!]

        bD.closeIV.setOnClickListener {
            dialog.dismiss()
        }

        bD.tvTitle.text = event.title
        bD.tvDescription.text = event.description
        bD.tvLocation.text = event.location
        val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS",
            Locale.getDefault()).parse(event.date)!!.toString()
        bD.tvDate.text = dateString


        dialog.show()
    }


}