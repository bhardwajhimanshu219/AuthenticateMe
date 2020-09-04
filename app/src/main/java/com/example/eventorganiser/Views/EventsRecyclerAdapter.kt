package com.example.eventorganiser.Views

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eventorganiser.Model.Events
import com.example.eventorganiser.Model.User
import com.example.eventorganiser.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.custom_events_cardview.view.*

class EventsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var items: List<Events> = ArrayList()
    private var userLoggedIn: String ?= null
    private var userObject: User ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        userLoggedIn = FirebaseAuth.getInstance().uid
        fetchcurrentuser(userLoggedIn!!)
        return EventsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_events_cardview, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is EventsViewHolder->{
                holder.bind(items[position])
                holder.register_btn.setOnClickListener {
                    Log.d("re", "registered")
                    items[position].registeredUsers.add(userLoggedIn!!)
                    //Now I want to update for this event in the Firebase
                    updateEventWithUserId(items[position])
                    //Now I want to update the User object for the event Id
                    userObject!!.myEvents.add(items[position].id)
                    updateUserForEventId(userObject!!)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(eventList: List<Events>){
        items = eventList
    }

    fun updateEventWithUserId(item:Events){
        val ref = FirebaseDatabase.getInstance().getReference("/events/${item.id}")
        ref.setValue(item).addOnSuccessListener {
            Log.d("Event Added","Event saved to database")
        }.addOnFailureListener {
            Log.d("failure", it.localizedMessage.toString())
        }
    }

    private fun fetchcurrentuser(userId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/user/$userId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                userObject = p0.getValue(User::class.java)
                Log.d("LatesetMessages","Current User $userObject")
            }
        })
    }

    fun updateUserForEventId(newUser: User){
        val ref = FirebaseDatabase.getInstance().getReference("/user/${newUser.uid}")
        ref.setValue(newUser).addOnSuccessListener {
            Log.d("successfull", "users database was successfully updated")
        }.addOnFailureListener {
            Log.d("unsucessfull", "oh! no something went wrong")
        }

    }



    class EventsViewHolder
    constructor(itemView: View):RecyclerView.ViewHolder(itemView){
        val event_image = itemView.eventImage
        val event_venue = itemView.eventVenue
        val event_desc = itemView.eventDescription
        val event_start_time = itemView.eventStartTime
        val event_end_time = itemView.eventEndTime
        val event_date = itemView.eventDate
        val event_name = itemView.eventName
        val register_btn = itemView.clickToRegisterevent

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