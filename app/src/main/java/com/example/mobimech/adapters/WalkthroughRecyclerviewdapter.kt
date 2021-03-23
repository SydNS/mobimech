package com.example.mobimech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.R
import com.example.mobimech.databinding.ListLayoutBinding
import com.example.mobimech.models.DisplayItem

class WalkthroughRecyclerviewdapter(val userList: ArrayList<DisplayItem>) :
    RecyclerView.Adapter<WalkthroughRecyclerviewdapter.ViewHolder>() {
    lateinit var listLayoutBinding: ListLayoutBinding

    class ViewHolder(var listLayoutBinding: ListLayoutBinding) :
        RecyclerView.ViewHolder(listLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        
        listLayoutBinding =
            ListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listLayoutBinding.textViewUsername.text = userList[position].name
        listLayoutBinding.textViewAddress.text = userList[position].address
        listLayoutBinding.bground.setBackgroundResource(userList[position].displayimage)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}