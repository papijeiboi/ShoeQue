package com.jemoje.shoeque.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jemoje.shoeque.R
import com.jemoje.shoeque.activities.RecommendationActivity
import com.jemoje.shoeque.model.ShoesData

class RecommendationAdapter constructor(private val recAct: RecommendationActivity,
                                        private val shoesData: MutableList<ShoesData>):RecyclerView.Adapter<RecommendationAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_recommendation, parent, false)


        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return shoesData.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Glide.with(recAct)
            .load(shoesData[position].images!![0].imageUrl)
            .error(R.drawable.ic_no_photo)
            .into(holder.recommendedImage)

        holder.recommendedName.text = shoesData[position].name
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var recommendedImage: ImageView
        var recommendedName: TextView

        init {

            recommendedImage = itemView.findViewById(R.id.iv_recommendation)
            recommendedName = itemView.findViewById(R.id.tv_recommendation_name)


        }
    }


}