package com.jans.calendar.events.app.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.model.EventCal
import com.jans.calendar.events.app.utils.calendarUtil.model.EventObjects
import java.io.InputStream
import java.util.Calendar
import java.util.Date

class ConfigApp {


    companion object{

        // read json and convert it to a list
        fun eventsList(context: Context): List<EventCal> {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.events_json)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, charset("UTF-8"))
            val gson = Gson()
            val eventCalType = object : TypeToken<List<EventCal>>() {}.type
            return gson.fromJson(jsonString, eventCalType)
        }


        fun doesDateExist(events: List<EventObjects>, date: Date): Pair<Boolean, Int?> {
            for ((index, event) in events.withIndex()) {
                if (isSameDay(event.date, date)) {
                    return Pair(true, index) // Return true and the index of the found date
                }
            }
            return Pair(false, null) // Return false and null index if not found
        }


        private fun isSameDay(date1: Date, date2: Date): Boolean {
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal1.time = date1
            cal2.time = date2
            return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                    cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
        }

        fun extractDateComponents(dateString: String): Triple<Int, Int, Int> {
            // Split the string by the space to separate date and time
            val datePart = dateString.split(" ")[0]

            // Split the date part by hyphen to get year, month, and day
            val parts = datePart.split("-")

            // Convert the string components to integers
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            return Triple(day, month, year)
        }


    }



}