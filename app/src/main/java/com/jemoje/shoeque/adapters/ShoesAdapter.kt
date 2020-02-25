package com.jemoje.shoeque.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jemoje.shoeque.R
import com.jemoje.shoeque.activities.ManageStockActivity
import com.jemoje.shoeque.model.ShoesData

class ShoesAdapter constructor(
    manageStockAct: ManageStockActivity,
    shoesData: MutableList<ShoesData>,
    typeUser: String
) : RecyclerView.Adapter<ShoesAdapter.ItemViewHolder>() {
    private val TAG = "ShoesAdapter"
    private val shoesDataList: MutableList<ShoesData> = shoesData
    private val manageStockActivity = manageStockAct
    private var userType = typeUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_manage_shoes, parent, false)


        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return shoesDataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.shoeName.text = shoesDataList[position].name

        Glide.with(manageStockActivity)
            .load(shoesDataList[position].images!![0].imageUrl!!)
            .error(R.drawable.ic_no_photo)
            .into(holder.shoeImage)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var shoeImage: ImageView
        var shoeName: TextView

        init {
            shoeImage = itemView.findViewById(R.id.iv_manage_shoes)
            shoeName = itemView.findViewById(R.id.tv_manage_shoes_title)
        }
    }
}