package com.example.eventorganiser

import android.app.ProgressDialog
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventorganiser.Model.Events
import com.example.eventorganiser.Model.User
import com.example.eventorganiser.Views.EventsRecyclerAdapter
import com.example.eventorganiser.Views.TopSpacingItemDecortion
import com.example.eventorganiser.Views.UsersRegisteredEvents
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_all_events.*
import kotlinx.android.synthetic.main.fragment_go_to_events.*
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class GoToEvents : Fragment() {
    //adapter to show the events
    var userObject: User ?= null
    private lateinit var userRegisterEventsFromFBAdapter: UsersRegisteredEvents
    var pullRefresh: SwipeRefreshLayout ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fetchcurrentuser()
        return inflater.inflate(R.layout.fragment_go_to_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pullRefresh = view.findViewById(R.id.pull_to_refresh)
        pullRefresh!!.setOnRefreshListener {
                fetchcurrentuser()
                if(userObject!!.myEvents.size>1){
                    Toast.makeText(context, "Found registrations", Toast.LENGTH_SHORT).show()
                    fetchEventsObjectsFromId(userObject!!.myEvents)
                    Toast.makeText(context, "registrations showed", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"No registration done for any event", Toast.LENGTH_SHORT).show()
                }
            pullRefresh!!.isRefreshing = false
        }

        super.onViewCreated(view, savedInstanceState)
    }



    private fun fetchcurrentuser() {
        Log.d("ran this event", "o here")
        val ref = FirebaseDatabase.getInstance().getReference("/user/${FirebaseAuth.getInstance().uid}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                userObject = p0.getValue(User::class.java)
                if(userObject!=null){
                    Log.d("LatesetMessages","Current User ${userObject!!.username}")
                    Toast.makeText(context,"found userobject", Toast.LENGTH_SHORT).show()
                    if(userObject!!.myEvents.size>1){
                        fetchEventsObjectsFromId(userObject!!.myEvents)
                    }else{
                        Toast.makeText(context,"No registration done for any event", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun fetchEventsObjectsFromId(idList: List<String>){
        val eventsOfUsersFb : MutableList<Events> = mutableListOf()
        for(str in idList){
            val ref = FirebaseDatabase.getInstance().getReference("/events/$str")
            ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val hashMap = snapshot.value as HashMap<String, Objects>
                        if(hashMap.size>1){
                            val name = hashMap["name"].toString()
                            val date = hashMap["date"].toString()
                            val venue = hashMap["venue"].toString()
                            val description = hashMap["description"].toString()
                            val startTime = hashMap["startTime"].toString()
                            val endTime = hashMap["endTime"].toString()
                            val venueImageurl = hashMap["venueImageurl"].toString()
                            val id = hashMap["id"].toString()
                            if(!name.isEmpty() and !date.isEmpty() and !venue.isEmpty() and !description.isEmpty() and
                                !endTime.isEmpty() and  !venueImageurl.isEmpty() and  !id.isEmpty()){
                                val fromF: Events = Events(name, venue, date, startTime, endTime, venueImageurl, description,id)
                                eventsOfUsersFb.add(fromF)
                            }
                        }
                    Log.d("size in here", eventsOfUsersFb.size.toString())
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

            intiRecyclerView()
            Log.d("size of events", eventsOfUsersFb.size.toString())
            addDataSet(eventsOfUsersFb)
            userRegisterEventsFromFBAdapter.notifyDataSetChanged()

    }
    private fun intiRecyclerView(){
        user_going_to_events.layoutManager = LinearLayoutManager(this.context)
        userRegisterEventsFromFBAdapter = UsersRegisteredEvents()
        val topSpacing = TopSpacingItemDecortion(5)
        user_going_to_events.addItemDecoration(topSpacing)
        user_going_to_events.adapter = userRegisterEventsFromFBAdapter
    }
    private fun addDataSet(eventFb: MutableList<Events>){
        userRegisterEventsFromFBAdapter.submitList(eventFb)
    }


}
