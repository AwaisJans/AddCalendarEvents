package com.jans.calendar.events.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jans.calendar.events.app.databinding.ActivityCalendarViewScreenBinding


class CalendarViewScreen : AppCompatActivity() {

    private lateinit var b: ActivityCalendarViewScreenBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityCalendarViewScreenBinding.inflate(layoutInflater)
        setContentView(b.root)




    }








}