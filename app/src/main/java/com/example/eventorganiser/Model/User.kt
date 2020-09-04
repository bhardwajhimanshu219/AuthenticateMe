package com.example.eventorganiser.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



class User(val uid:String ,
           val username:String ,
           val profileimageurl:String,
           val myEvents: MutableList<String> = mutableListOf<String>("")) {
    //User will be create using the uid, username, profileimageurl->
    // Parcelable is used to move objects around in different activities
    constructor():this("","","")
}