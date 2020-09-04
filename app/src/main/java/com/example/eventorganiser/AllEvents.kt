package com.example.eventorganiser

import android.content.Intent
import android.os.Bundle
import android.util.EventLog
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventorganiser.Model.Events
import com.example.eventorganiser.Model.User
import com.example.eventorganiser.Views.EventsRecyclerAdapter
import com.example.eventorganiser.Views.TopSpacingItemDecortion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_all_events.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class AllEvents : Fragment() {
    private lateinit var eventAdap: EventsRecyclerAdapter
    //Fetch user object
    //Get users registered list
    //And make sure to make the register button invisible or completely remove that card from this list or disable the register button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getAllEvents()
//        addEventsIntoFirebase()
        return inflater.inflate(R.layout.fragment_all_events, container, false)
    }

    private fun getAllEvents(){
        val ref = FirebaseDatabase.getInstance().getReference("events/")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventsFb : MutableList<Events> = mutableListOf()
                for(snap in snapshot.children) {
                    val ev: Events? = snap.getValue(Events::class.java)
                    eventsFb.add(ev!!)
                }
                Log.d("making", eventsFb.size.toString())
                intiRecyclerView()
                addDataSet(eventsFb)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("home", "can")
                TODO("Not yet implemented")
            }
        })
    }

    private fun intiRecyclerView(){
        recycler_view_all_events.layoutManager = LinearLayoutManager(this.context)
        eventAdap = EventsRecyclerAdapter()
        val topSpacing = TopSpacingItemDecortion(15)
        recycler_view_all_events.addItemDecoration(topSpacing)
        recycler_view_all_events.adapter = eventAdap
    }
    private fun addDataSet(eventFb: MutableList<Events>){
        eventAdap.submitData(eventFb)
    }

//    private fun addEventsIntoFirebase(){
//        //In future events will be added first and then shown
//        val uuid = UUID.randomUUID()
//        val ref = FirebaseDatabase.getInstance().getReference("/events/$uuid")
//        val event = Events("Event1", "Venue 1", "17/03/2000", "12:00", "15:00", "IMAGEURL", "Lorem ipsum",uuid.toString())
//        ref.setValue(event).addOnSuccessListener {
//            Log.d("Event Added","Event saved to database")
//        }.addOnFailureListener {
//            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
//        }
//    }

}
