package com.jans.calendar.events.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jans.calendar.events.app.databinding.ActivityFirstBinding

class FirstScreen : AppCompatActivity() {

    private lateinit var b: ActivityFirstBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(b.root)


        b.btnCalendarScreen.setOnClickListener{
            startActivity(Intent(this, CalendarScreen::class.java))
        }

        b.practiseScreenBtn.setOnClickListener{

        }






    }



}