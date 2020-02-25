package com.jemoje.shoeque.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jemoje.shoeque.R
import com.jemoje.shoeque.activities.ManageUserActivity
import com.jemoje.shoeque.activities.SuccessAddUserActivity
import com.jemoje.shoeque.activities.UpdateUserActivity
import com.jemoje.shoeque.model.UserData

class UsersAdapter constructor(
    manageUserAct: ManageUserActivity,
    manageUserData: MutableList<UserData>,
    typeUser: String
) : RecyclerView.Adapter<UsersAdapter.ItemViewHolder>() {

    private val manageUserDataList: MutableList<UserData> = manageUserData
    private val manageUserActivity = manageUserAct
    private var userType = typeUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_manage_user, parent, false)


        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return manageUserDataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (userType == "Stock") {
            userType = "stock"
        } else if (userType == "Sale") {
            userType = "sale"
        } else if (userType == "Admin") {
            userType = "admin"
        }

        holder.userId.text = manageUserDataList[position].id
        holder.userName.text = manageUserDataList[position].name
        holder.userEmail.text = manageUserDataList[position].email
        holder.userType.text = manageUserDataList[position].userType

        val selectedUserId = manageUserDataList[position].id

        holder.itemView.setOnClickListener {
            holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(
                    manageUserActivity,
                    R.anim.image_click
                )
            )
            val intent =
                Intent(manageUserActivity, UpdateUserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("selectedUserId", selectedUserId)
            intent.putExtra("userType", userType)
            manageUserActivity.startActivity(intent)
            manageUserActivity.overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
        }
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userId: TextView
        var userName: TextView
        var userEmail: TextView
        var userType: TextView

        init {

            userId = itemView.findViewById(R.id.tv_item_manage_user_id)
            userName = itemView.findViewById(R.id.tv_item_manage_user_name)
            userEmail = itemView.findViewById(R.id.tv_item_manage_user_email)
            userType = itemView.findViewById(R.id.tv_item_manage_user_type)


        }
    }
}