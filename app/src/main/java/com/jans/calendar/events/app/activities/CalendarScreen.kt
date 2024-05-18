package com.jans.calendar.events.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.adapters.CalendarAdapter
import com.jans.calendar.events.app.databinding.ActivityCalendarBinding
import com.jans.calendar.events.app.model.Event
import com.jans.calendar.events.app.utils.ConfigApp
import com.jans.calendar.events.app.utils.ConfigApp.Companion.createICalEvents
import com.jans.calendar.events.app.utils.ConfigApp.Companion.eventsList
//import com.jans.calendar.events.app.utils.ConfigApp.Companion.eventList
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarScreen : AppCompatActivity() {

    private lateinit var b: ActivityCalendarBinding

    private lateinit var calendarEventList: List<Event>


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(b.root)

        // initialize List
        calendarEventList = eventsList(this)

        // Initialize Buttons Click Listeners
        buttonsInit()

        // Declaring Number of Events
        b.tvNumberOfEvents.text = "Number of Events = ${calendarEventList.size}"

       // Showing Calendar Events in RecyclerView
       setUpCalendarRV()


    }

    private fun buttonsInit() {
        // back button
        b.backBtn.setOnClickListener {
            finish()
        }
        // Adding Events
        b.btnAddEvents.setOnClickListener{
            // lets generate an ICS File
            val eventFile = createICalEvents(this,calendarEventList)
            // Lets make A URI for ICS File
            val eventFileUri = FileProvider.getUriForFile(
                this, "$packageName.provider",
                eventFile)
            // Send it to Calendar App
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(eventFileUri, "text/calendar")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        }
    }

    private fun setUpCalendarRV() {
        val recyclerView = b.recyclerView
        // Code to Populate List
        Log.d("Response123", "${calendarEventList.size}")
        // Code for Setup Adapter for Recycler View
        val namesAdapter = CalendarAdapter(calendarEventList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = namesAdapter
    }





}