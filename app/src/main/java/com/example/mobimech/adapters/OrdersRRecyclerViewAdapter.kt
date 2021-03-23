package com.example.mobimech.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.databinding.OrderlistLayoutBinding
import com.example.mobimech.models.OrderListItem

class OrdersRRecyclerViewAdapter(val orderlistItem: ArrayList<OrderListItem>) :
    RecyclerView.Adapter<OrdersRRecyclerViewAdapter.ViewHolder>() {

    lateinit var orderlistLayoutBinding: OrderlistLayoutBinding

    class ViewHolder(var viewBinding: OrderlistLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view=LayoutInflater.from(parent.context).inflate(R.layout.orderlist_layout, parent, false)
        orderlistLayoutBinding =
            OrderlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = orderlistLayoutBinding
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderlistItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewBinding.mecahnname.text = orderlistItem[position].mechName
        holder.viewBinding.orderdate.text = orderlistItem[position].OrderDate
    }
}