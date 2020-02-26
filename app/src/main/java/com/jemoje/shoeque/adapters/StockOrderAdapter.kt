package com.jemoje.shoeque.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jemoje.shoeque.R
import com.jemoje.shoeque.activities.StockOrderActivity
import com.jemoje.shoeque.model.DisplayOrdersResponseData

class StockOrderAdapter constructor(
    stockOrAct: StockOrderActivity,
    shoesData: MutableList<DisplayOrdersResponseData>
) : RecyclerView.Adapter<StockOrderAdapter.ItemViewHolder>() {

    private val ordersList: MutableList<DisplayOrdersResponseData> = shoesData
    private val stockOrderActivity = stockOrAct

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_stock_order, parent, false)


        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.orderId.text = ordersList[position].id
        holder.orderName.text = ordersList[position].product!!.name
        holder.orderSize.text = ordersList[position].size!!.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderId: TextView
        var orderName: TextView
        var orderSize: TextView

        init {
            orderId = itemView.findViewById(R.id.tv_stock_order_id)
            orderName = itemView.findViewById(R.id.tv_stock_order_name)
            orderSize = itemView.findViewById(R.id.tv_stock_order_size)
        }
    }
}