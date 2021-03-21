package com.example.mobimech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.R
import com.example.mobimech.models.DisplayItem

class WalkthroughRecyclerviewdapter(val userList: ArrayList<DisplayItem>) :
    RecyclerView.Adapter<WalkthroughRecyclerviewdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(user: DisplayItem) {
            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView
            val image  = itemView.findViewById(R.id.displayimage) as ImageView
            textViewName.text = user.name
            textViewAddress.text = user.address
            image.setImageResource(user.displayimage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}