package com.example.mobimech.homeui

import ServiceListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.R
import com.example.mobimech.UI.MechanicMapUi
import com.example.mobimech.UI.UserMapUi
import com.example.mobimech.databinding.MakeorderBinding
import com.example.mobimech.models.RequestedService
import com.example.mobimech.models.ServicesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MakeOrder : Fragment(), ServiceListAdapter.OnItemClickListener {
    lateinit var makeorderBinding: MakeorderBinding

    private val recyclerView: RecyclerView? = null
    private var recyclerDataArrayList: ArrayList<ServicesModel>? = null

//    saveinghe ordertype

    lateinit var database: FirebaseDatabase
    lateinit var getReference: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        getReference = database.reference
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        makeorderBinding = MakeorderBinding.inflate(inflater)


//        to the user map
        makeorderBinding.makeorders.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.)
            startActivity(Intent(activity, UserMapUi::class.java))
//            startActivity(Intent(activity,MechanicMapUi::class.java))
//            activity?.finish()

        }

//        to the mechanics map
        makeorderBinding.hl2.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.)
//            startActivity(Intent(activity,UserMapUi::class.java))
            startActivity(Intent(activity, MechanicMapUi::class.java))
//            activity?.finish()

        }


        // created new array list..
        recyclerDataArrayList = ArrayList()

        // added data to array list
        recyclerDataArrayList!!.add(ServicesModel("Whole Repair", R.drawable.whole_service))
        recyclerDataArrayList!!.add(ServicesModel("Brakes Services", R.drawable.brakes__services))
        recyclerDataArrayList!!.add(ServicesModel("Wheel Services", R.drawable.wheel_repair))
        recyclerDataArrayList!!.add(ServicesModel("Battery Services", R.drawable.battery_servics))
        recyclerDataArrayList!!.add(ServicesModel("Shock Services", R.drawable.shocks_services))
        recyclerDataArrayList!!.add(ServicesModel("Engine Service", R.drawable.engine_repair))

        // added data from arraylist to adapter class.
        val adapter = ServiceListAdapter(recyclerDataArrayList!!, this)

        // setting grid layout manager to implement grid view.
        // in this method '3' represents number of columns to be displayed in grid view.
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        // at last set adapter to recycler view.
        makeorderBinding.serviceslist.layoutManager = layoutManager
        makeorderBinding.serviceslist.adapter = adapter

        return makeorderBinding.root
    }


    override fun onItemClick(position: Int) {
        val getUserID = auth.currentUser?.uid

        Toast.makeText(requireActivity(), "Item $position clicked", Toast.LENGTH_SHORT).show()
//        val clickedItem = accinfoList?.get(position)
//        clickedItem?.accountinfo = "Clicked"
////        myAccountAdapter?.notifyItemChanged(position)

        when (position) {
            0 -> {
                val requestedService =
                    RequestedService(getUserID!!, requestTitle = "Whole Car Repair")
                getReference.child("Services_requests").child(getUserID).push()
                    .setValue(requestedService).addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Item ${requestedService.personRequest} clicked", Toast.LENGTH_SHORT).show()

                    }

            }
//            1 ->startActivity(Intent(activity, WalletActivity::class.java))

//            else -> println("maybe")
        }
    }

}
