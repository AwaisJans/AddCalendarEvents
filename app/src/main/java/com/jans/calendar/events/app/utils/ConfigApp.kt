package com.jans.calendar.events.app.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.model.Event
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConfigApp {


    companion object{

        private lateinit var eventList: List<Event>

        // read json and convert it to a list
        fun eventsList(context: Context): List<Event> {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.events_json)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, charset("UTF-8"))
            val gson = Gson()
            val eventType = object : TypeToken<List<Event>>() {}.type
            return gson.fromJson(jsonString, eventType)
        }


        // create an ICS File
        fun createICalEvents(context: Context,eventsList: List<Event>): File {
            this.eventList = eventsList
            val activity = context as Activity

            val icsFile = File(activity.externalCacheDir, "AllDayEvents.ics")

            // write the ICS file
            FileOutputStream(icsFile).use { stream ->
                stream.write(icsContent().toByteArray())
            }

            return icsFile
        }



        // take all events from list
        private fun icsContent(): String {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val jsonDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())

            val icsContent = buildString {
                appendLine("BEGIN:VCALENDAR")
                appendLine("VERSION:2.0")
                appendLine("PRODID:-//ChatGPT//iCal4j 1.0//EN")

                for (event in eventList) {
                    val eventStart = Calendar.getInstance().apply {
                        time = jsonDateFormat.parse(event.date)!!
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
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

            return  icsContent




        }





    }



}