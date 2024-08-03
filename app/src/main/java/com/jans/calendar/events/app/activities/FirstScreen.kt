package com.jans.calendar.events.app.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jans.calendar.events.app.databinding.ActivityFirstBinding

class FirstScreen : AppCompatActivity() {

    private lateinit var b: ActivityFirstBinding

    // New Branch Saturday , 18th May 2024

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(b.root)


        b.btnCalendarScreen.setOnClickListener {
            startActivity(Intent(this, CalendarScreen::class.java))
        }

        b.practiseScreenBtn.setOnClickListener {

            startActivity(Intent(this, CalendarViewScreen::class.java))
        }

    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


}