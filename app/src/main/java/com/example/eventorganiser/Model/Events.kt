package com.example.eventorganiser.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Time
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class Events(var name:String,
             var venue: String,
             var date: String,
             var startTime: String,
             var endTime: String,
             var venueImageurl: String,
             var description: String,
             var id: String,
             var registeredUsers : MutableList<String> = mutableListOf<String>("")){
    //events will be used to create the event card and then for every user their participation will be shown accordingly
    constructor():this ("","","","","","","","")

}
