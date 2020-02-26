package com.jemoje.shoeque.activities

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.UserData
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_admin_main_menu.*
import kotlinx.android.synthetic.main.activity_stock_main_menu.*
import kotlinx.android.synthetic.main.activity_stock_main_menu.layout_drawer_admin
import kotlinx.android.synthetic.main.layout_drawer_admin.*

class StockMainMenuActivity : AppCompatActivity() {
    private var dataFromApi = ""

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_stock_main_menu)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        dataFromApi = Prefs.getString(Keys.USER_FULL_DATA, "")
        val profileResponse = Gson().fromJson<UserData>(dataFromApi, UserData::class.java)

        tv_drawer_admin_name.text = profileResponse.name

        btn_order_stock.setOnClickListener {
            runOnUiThread {
                val intent =
                    Intent(
                        applicationContext,
                        StockOrderActivity::class.java
                    )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext.startActivity(intent)
                overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
            }
        }
        btn_menu_stock.setOnClickListener {
            toggle()
        }

        btn_admin_logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete?")

            builder.setPositiveButton("YES") { dialog, which ->
                Prefs.clear()
                finish()
                val intent = Intent(this, LandingMenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun toggle() {
        if (layout_drawer_admin.isDrawerVisible(GravityCompat.START)) {
            layout_drawer_admin.closeDrawer(GravityCompat.START)
        } else {
            layout_drawer_admin.openDrawer(GravityCompat.START)

        }
    }
}
