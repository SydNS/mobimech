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
import com.example.mobimech.models.ServicesModel

class MakeOrder : Fragment() ,ServiceListAdapter.OnItemClickListener{
    lateinit var makeorderBinding: MakeorderBinding

    private val recyclerView: RecyclerView? = null
    private var recyclerDataArrayList: ArrayList<ServicesModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        makeorderBinding= MakeorderBinding.inflate(inflater)


//        to the user map
        makeorderBinding.makeorders.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.)
            startActivity(Intent(activity,UserMapUi::class.java))
//            startActivity(Intent(activity,MechanicMapUi::class.java))
//            activity?.finish()

        }

//        to the mechanics map
        makeorderBinding.hl2.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.)
//            startActivity(Intent(activity,UserMapUi::class.java))
            startActivity(Intent(activity,MechanicMapUi::class.java))
//            activity?.finish()

        }


        // created new array list..
        recyclerDataArrayList = ArrayList()

        // added data to array list
        recyclerDataArrayList!!.add(ServicesModel("Whole Repair", R.drawable.whole_service))
        recyclerDataArrayList!!.add(ServicesModel("Brakes Services", R.drawable.brakes__services))
        recyclerDataArrayList!!.add(ServicesModel("Wheel Services", R.drawable.wheel_repair))
        recyclerDataArrayList!!.add(ServicesModel("Battery Services", R.drawable.battery_service))
        recyclerDataArrayList!!.add(ServicesModel("Shock Services", R.drawable.shocks_services))
        recyclerDataArrayList!!.add(ServicesModel("Engine Service", R.drawable.engine_repair))

        // added data from arraylist to adapter class.
        val adapter = ServiceListAdapter(recyclerDataArrayList!!,this)

        // setting grid layout manager to implement grid view.
        // in this method '3' represents number of columns to be displayed in grid view.
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        // at last set adapter to recycler view.
        makeorderBinding.serviceslist.layoutManager = layoutManager
        makeorderBinding.serviceslist.adapter = adapter

        return makeorderBinding.root}


    override fun onItemClick(position: Int) {

        Toast.makeText(requireActivity(), "Item $position clicked", Toast.LENGTH_SHORT).show()
//        val clickedItem = accinfoList?.get(position)
//        clickedItem?.accountinfo = "Clicked"
////        myAccountAdapter?.notifyItemChanged(position)

        when (position) {
//            0 ->makeorderBinding.root.findNavController().navigate(R.id.action_navigation_dashboard_to_mapsFragment_Ride)

//            1 ->startActivity(Intent(activity, WalletActivity::class.java))

//            else -> println("maybe")
        }
    }

}
