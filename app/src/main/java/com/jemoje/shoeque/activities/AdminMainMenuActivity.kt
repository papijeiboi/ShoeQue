package com.jemoje.shoeque.activities

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.UserData
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_admin_main_menu.*
import kotlinx.android.synthetic.main.layout_drawer_admin.*

class AdminMainMenuActivity : AppCompatActivity() {
    private var dataFromApi = ""

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_admin_main_menu)
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        dataFromApi = Prefs.getString(Keys.USER_FULL_DATA, "")
        val profileResponse = Gson().fromJson<UserData>(dataFromApi, UserData::class.java)

        tv_drawer_admin_name.text = profileResponse.name

        Glide.with(this)
            .load(R.drawable.ic_admin_profile)
            .circleCrop()
            .into(iv_drawer_admin_profile)

        btn_menu_admin.setOnClickListener {
            toggle()
        }

        val adminHandleClicks = View.OnClickListener {
            when (it){
                btn_admin_manage_stock -> {
                    runOnUiThread {
                        val intent =
                            Intent(
                                applicationContext,
                                ManageStockActivity::class.java
                            )
                        intent.putExtra("userType", "Stock")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                }
                btn_admin_manage_sales_user -> {
                    runOnUiThread {
                        val intent =
                            Intent(
                                applicationContext,
                                ManageUserActivity::class.java
                            )
                        intent.putExtra("userType", "Sale")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                }
                btn_admin_manage_stock_user -> {
                    runOnUiThread {
                        val intent =
                            Intent(
                                applicationContext,
                                ManageUserActivity::class.java
                            )
                        intent.putExtra("userType", "Stock")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                }
                btn_admin_manage_admin_user -> {
                    runOnUiThread {
                        val intent =
                            Intent(
                                applicationContext,
                                ManageUserActivity::class.java
                            )
                        intent.putExtra("userType", "Admin")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                }
            }
        }
        btn_admin_manage_stock.setOnClickListener(adminHandleClicks)
        btn_admin_manage_sales_user.setOnClickListener(adminHandleClicks)
        btn_admin_manage_stock_user.setOnClickListener(adminHandleClicks)
        btn_admin_manage_admin_user.setOnClickListener(adminHandleClicks)





    }

    private fun toggle() {
        if (layout_drawer_admin.isDrawerVisible(GravityCompat.START)) {
            layout_drawer_admin.closeDrawer(GravityCompat.START)
        } else {
            layout_drawer_admin.openDrawer(GravityCompat.START)

        }
    }
}
