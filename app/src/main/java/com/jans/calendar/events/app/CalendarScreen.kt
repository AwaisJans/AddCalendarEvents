package com.jans.calendar.events.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jans.calendar.events.app.databinding.ActivityCalendarBinding


class CalendarScreen : AppCompatActivity() {

    private lateinit var b: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(b.root)


        b.backBtn.setOnClickListener {
            finish()
        }


    }



}