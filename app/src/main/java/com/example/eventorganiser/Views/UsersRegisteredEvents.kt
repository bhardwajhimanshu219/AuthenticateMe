package com.example.eventorganiser.Views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eventorganiser.Model.Events
import com.example.eventorganiser.R
import kotlinx.android.synthetic.main.custom_events_cardview.view.*
import kotlinx.android.synthetic.main.user_going_to_events.view.*


class UsersRegisteredEvents : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var items: List<Events> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UsersRegisteredEventsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_going_to_events, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is UsersRegisteredEventsViewHolder->{
                holder.bind(items[position])
            }
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(eventList:List<Events>){
        Log.d("inside adapter", eventList.toString())
        items = eventList
    }





    class UsersRegisteredEventsViewHolder
    constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val event_image = itemView.eventImage_user_registered_to
        val event_venue = itemView.eventVenue_user_registered_to
        val event_desc = itemView.eventDescription_user_registered_to
        val event_start_time = itemView.eventStartTime_user_registered_to
        val event_end_time = itemView.eventEndTime_user_registered_to
        val event_date = itemView.eventDate_user_registered_to
        val event_name = itemView.eventName_user_registered_to

        fun bind(eventCard: Events){
            event_venue.setText(eventCard.venue)
            event_date.setText(eventCard.date)
            event_desc.setText(eventCard.description)
            event_end_time.setText(eventCard.endTime)
            event_name.setText(eventCard.name)
            event_start_time.setText(eventCard.startTime)
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions).load(eventCard.venueImageurl).into(event_image)
        }


    }

}



















