package com.example.mobimech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.R
import com.example.mobimech.databinding.OrderlistLayoutBinding
import com.example.mobimech.models.DisplayItem

class OrdersRRecyclerViewAdapter(val orderlistItem:  ArrayList<OrderListItem>) : RecyclerView.Adapter<OrdersRRecyclerViewAdapter.ViewHolder>() {

    lateinit var orderlistLayoutBinding: OrderlistLayoutBinding
    class ViewHolder(var viewBinding: OrderlistLayoutBinding) : RecyclerView.ViewHolder(itemView) {
        val tvname=


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view=LayoutInflater.from(parent.context).inflate(R.layout.orderlist_layout, parent, false)
        orderlistLayoutBinding= OrderlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view=orderlistLayoutBinding.root
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        orderlistItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}