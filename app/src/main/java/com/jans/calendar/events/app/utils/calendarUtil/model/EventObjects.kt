package com.jans.calendar.events.app.utils.calendarUtil.model

import java.util.Date

class EventObjects {
    var id: Int = 0
        private set
    var message: String
        private set
    var date: Date
        private set


    var description: String = ""
    var location: String = ""



    constructor(message: String, date: Date,description: String, location: String) {
        this.message = message
        this.date = date
        this.description = description
        this.location = location

    }

    constructor(id: Int, message: String, date: Date) {
        this.date = date
        this.message = message
        this.id = id
    }
}
