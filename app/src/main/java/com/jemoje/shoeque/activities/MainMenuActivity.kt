package com.jemoje.shoeque.activities

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.UserData
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_admin_main_menu.*
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.layout_drawer_admin.*

class MainMenuActivity : AppCompatActivity() {
    private var dataFromApi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
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

        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(

            android.Manifest.permission.CAMERA
        )

        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }


        btn_menu_main.setOnClickListener {
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

        btn_main_scan.setOnClickListener {
            val intent = Intent(applicationContext, ScanActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
            overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
        }


        btn_main_search.setOnClickListener {

        }


    }

    private fun toggle() {
        if (layout_drawer_main.isDrawerVisible(GravityCompat.START)) {
            layout_drawer_main.closeDrawer(GravityCompat.START)
        } else {
            layout_drawer_main.openDrawer(GravityCompat.START)

        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
}
