package com.jans.calendar.events.app.model

import java.util.Calendar

data class Event(
    val title: String,
    val description: String,
    val location: String,
    val year: Int,
    val month: Int,
    val day: Int
)