package com.jemoje.shoeque.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jemoje.shoeque.R
import com.jemoje.shoeque.activities.OrderActivity
import com.jemoje.shoeque.model.SelectionModel
import com.jemoje.shoeque.model.SizesData

class ShoeInfoAdapter constructor(
    private val orderAct: OrderActivity,
    private val sizeData: MutableList<SizesData>
) :
    RecyclerView.Adapter<ShoeInfoAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_shoes_info, parent, false)


        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sizeData.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.shoeSize.text = sizeData[position].size
        holder.shoeQty.text = sizeData[position].quantity
        val shoeId = sizeData[position].productId
        val sizeId = sizeData[position].id



        holder.shoeAdd.setOnClickListener {
            if (!sizeData[position].isSelected) {
                orderAct.selectItem(position)
                holder.shoeAdd.setImageResource(R.drawable.ic_minus)
            } else {
                orderAct.deSelectItem(position)
                holder.shoeAdd.setImageResource(R.drawable.ic_add)
            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var shoeSize: TextView
        var shoeQty: TextView
        var shoeAdd: ImageView

        init {

            shoeSize = itemView.findViewById(R.id.tv_item_shoes_info_size)
            shoeQty = itemView.findViewById(R.id.tv_item_shoes_info_qty)
            shoeAdd = itemView.findViewById(R.id.btn_item_shoes_info_add)


        }
    }
}