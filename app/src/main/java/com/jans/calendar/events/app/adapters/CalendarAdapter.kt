package com.jans.calendar.events.app.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.calendar.events.app.R
import com.jans.calendar.events.app.model.EventCal
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SetTextI18n")
class CalendarAdapter(private var eventsList: List<EventCal>?) :
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

        fun bind(eventCal: EventCal) {
            // set Event Title
            tvTitle.text = eventCal.title
            // set Event Description
            tvDescription.text = eventCal.description
            // set Event Date
            val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS",
                Locale.getDefault()).parse(eventCal.date)!!.toString()
            tvDate.text = dateString
        }


    }





}


















