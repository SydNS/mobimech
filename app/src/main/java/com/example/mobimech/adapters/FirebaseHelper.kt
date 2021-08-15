package com.example.mobimech.adapters

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.example.mobimech.models.Mechanics_here
import com.google.firebase.database.*
import java.util.ArrayList

class FirebaseHelper(var db: DatabaseReference, var c: Context, var mListView: ListView) {
    var saved: Boolean? = null
    var teachers = ArrayList<Mechanics_here>()

    /*
    let's now write how to save a single Teacher to FirebaseDatabase
     */
    fun save(teacher: Mechanics_here?): Boolean? {
        //check if they have passed us a valid teacher. If so then return false.
        saved = if (teacher == null) {
            false
        } else {
            //otherwise try to push data to firebase database.
            try {
                //push data to FirebaseDatabase. Table or Child called Teacher will be created.
                db.child("Mechanics_here").push().setValue(teacher)
                true
            } catch (e: DatabaseException) {
                e.printStackTrace()
                false
            }
        }
        //tell them of status of save.
        return saved
    }

    /*
    Retrieve and Return them clean data in an arraylist so that they just bind it to ListView.
     */
    fun retrieve(): ArrayList<Mechanics_here> {
        db.child("Mechanics_here").addValueEventListener(object : ValueEventListener() {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                teachers.clear()
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    for (ds in dataSnapshot.children) {
                        //Now get Teacher Objects and populate our arraylist.
                        val teacher: Mechanics_here? = ds.getValue(Mechanics_here::class.java)
                        teachers.add(teacher!!)
                    }
                    adapter = Custome(c, teachers)
                    mListView.adapter = adapter
                    Handler().post(Runnable { mListView.smoothScrollToPosition(teachers.size) })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("mTAG", databaseError.getMessage())
                Toast.makeText(c, "ERROR " + databaseError.getMessage(), Toast.LENGTH_LONG).show()
            }
        })
        return teachers
    }

    /*
   let's receive a reference to our FirebaseDatabase
   */
    init {
        retrieve()
    }
}