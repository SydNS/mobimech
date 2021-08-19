package com.example.mobimech.homeui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobimech.UI.ChatLogActivity
import com.example.mobimech.UI.LatestMessagesActivity
import com.example.mobimech.adapters.OrdersRRecyclerViewAdapter
import com.example.mobimech.databinding.LogsfragBinding
import com.example.mobimech.models.OrderListItem
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LogsFrag : Fragment() {
    lateinit var logsfragBinding: LogsfragBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logsfragBinding= LogsfragBinding.inflate(inflater)

//        crating an arraylist to store users using the data class user
        val orderdt = ArrayList<OrderListItem>()
        val database = Firebase.database
        val myRef = database.getReference("message")


        //adding some dummy data to the list
        orderdt.add(OrderListItem("Juma","22/3/2021" ))
        orderdt.add(OrderListItem("David","23/3/2021" ))
        orderdt.add(OrderListItem("Elijah","24/3/2021" ))
        orderdt.add(OrderListItem("Paul","25/3/2021" ))

        logsfragBinding.orderrecyclerview.adapter= OrdersRRecyclerViewAdapter(orderdt)
        logsfragBinding.orderrecyclerview.layoutManager= LinearLayoutManager(activity)


        logsfragBinding.fab.setOnClickListener {
            startActivity(Intent(activity,LatestMessagesActivity::class.java))
        }

        return logsfragBinding.root
    }

}
