package com.jans.calendar.events.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.calendar.events.app.adapters.CalendarAdapter
import com.jans.calendar.events.app.databinding.ActivityCalendarBinding
import com.jans.calendar.events.app.model.Event
import com.jans.calendar.events.app.utils.ConfigApp
import com.jans.calendar.events.app.utils.ConfigApp.Companion.eventList
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarScreen : AppCompatActivity() {

    private lateinit var b: ActivityCalendarBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.backBtn.setOnClickListener {
            finish()
        }

        // Declaring Number of Events
        b.tvNumberOfEvents.text = "Number of Events = ${eventList.size}"

       // Showing Calendar Events
       setUpCalendarRV()



        // Adding Events

        b.btnAddEvents.setOnClickListener{


            val eventFile = createICalEvents(eventList)
            val eventFileUri = FileProvider.getUriForFile(
                this, "$packageName.provider",
                eventFile)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(eventFileUri, "text/calendar")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)

        }



    }

    private fun setUpCalendarRV() {
        val recyclerView = b.recyclerView
        // Code to Populate List
        val eventsList = ConfigApp.eventList
        Log.d("Response123", "${eventsList.size}")
        // Code for Setup Adapter for Recycler View
        val namesAdapter = CalendarAdapter(eventsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = namesAdapter
    }



    private fun createICalEvents(events: List<Event>): File {
        val icsFile = File(externalCacheDir, "AllDayEvents.ics")

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        val icsContent = buildString {
            appendLine("BEGIN:VCALENDAR")
            appendLine("VERSION:2.0")
            appendLine("PRODID:-//ChatGPT//iCal4j 1.0//EN")

            for (event in events) {
                val eventStart = Calendar.getInstance().apply {
                    set(event.year, event.month - 1, event.day, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val eventEnd = eventStart.clone() as Calendar
                eventEnd.add(Calendar.DAY_OF_MONTH, 1) // All-day event, so the end is the next day

                appendLine("BEGIN:VEVENT")
                appendLine("DTSTAMP:${dateFormat.format(Calendar.getInstance().time)}Z")
                appendLine("UID:${(Math.random() * Long.MAX_VALUE).toLong()}@yourdomain.com")
                appendLine("SUMMARY:${event.title}")
                appendLine("DESCRIPTION:${event.description}")
                appendLine("LOCATION:${event.location}")
                appendLine("DTSTART;VALUE=DATE:${dateFormat.format(eventStart.time)}")
                appendLine("DTEND;VALUE=DATE:${dateFormat.format(eventEnd.time)}")
                appendLine("END:VEVENT")
            }

            appendLine("END:VCALENDAR")
        }

        FileOutputStream(icsFile).use { stream ->
            stream.write(icsContent.toByteArray())
        }

        return icsFile
    }

}