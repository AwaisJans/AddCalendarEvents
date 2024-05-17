package com.jans.calendar.events.app.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("SetTextI18n")
class CalendarAdapter(private var eventsList: List<Event>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return EventsVH(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_events, parent, false))
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // getting Context
        mContext = holder.itemView.context

        val event = eventsList?.get(position)!!

        when (holder) {
            is EventsVH -> {
                holder.bind(event)
            }
        }
    }

    override fun getItemCount(): Int = eventsList!!.size

    class EventsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvTitle: TextView = itemView.findViewById(R.id.tvTitleEvent)
        private var tvDescription: TextView = itemView.findViewById(R.id.tvEventDesc)
        private var tvDate: TextView = itemView.findViewById(R.id.tvEventDate)

        fun bind(event: Event) {
            // set Event Title
            tvTitle.text = event.title
            // set Event Description
            tvDescription.text = event.description
            // set Event Date
            val dayName = getDayName(event.year, event.month, event.day)
            val formattedDate = "Date: ${event.day} ${getMonthName(event.year, event.month)} ${event.year}, $dayName"
            tvDate.text = formattedDate
        }


        private fun formatTime(hour: Int, minute: Int): String {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return formatter.format(calendar.time)
        }

        private fun getDayName(year: Int, month: Int, day: Int): String {
            val calendar = Calendar.getInstance().apply {
                set(year, month - 1, day) // month is 0-based in Calendar
            }
            val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
            return formatter.format(calendar.time)
        }

        private fun getMonthName(year: Int, month: Int): String {
            val calendar = Calendar.getInstance().apply {
                set(year, month - 1, 1) // Month is 0-based in Calendar
            }
            val formatter = SimpleDateFormat("MMMM", Locale.getDefault())
            return formatter.format(calendar.time)
        }


    }





}


















