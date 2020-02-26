package com.jemoje.shoeque.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jemoje.shoeque.R
import kotlinx.android.synthetic.main.activity_delete_update_user.*

class DeleteUpdateUserActivity : AppCompatActivity() {
    private var userType: String = ""

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_delete_update_user)

        userType = intent.getStringExtra("userType")!!

        btn_delete_update_user_done.setOnClickListener {
            when(userType){
                "stock" -> {
                    runOnUiThread {
                        finish()
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
                "sale" -> {
                    runOnUiThread {
                        finish()
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
                "admin" -> {
                    runOnUiThread {
                        finish()
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
    }
}
