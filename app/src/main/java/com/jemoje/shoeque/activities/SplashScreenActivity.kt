package com.jemoje.shoeque.activities

import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.UserData
import com.pixplicity.easyprefs.library.Prefs
import java.util.*
import kotlin.concurrent.timerTask

class SplashScreenActivity : AppCompatActivity() {
    private val TAG = "SplashScreenActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }


        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        runOnUiThread {
            val timer = Timer()
            timer.schedule(timerTask {

                val apiToken = Prefs.getString(Keys.USER_TOKEN, "")

                if (apiToken == "") {
                    runOnUiThread {
                        val intent = Intent(applicationContext, LandingMenuActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        finish()
                    }
                } else {
                    runOnUiThread {

                        val fromApiInfo = Prefs.getString(Keys.USER_FULL_DATA, "")
                        val profileResponse =
                            Gson().fromJson<UserData>(fromApiInfo, UserData::class.java)

                        when (profileResponse.userType) {
                            "admin" -> {
                                val intent =
                                    Intent(applicationContext, AdminMainMenuActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                applicationContext.startActivity(intent)
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                finish()
                            }
                            "sale" -> {
                                val intent =
                                    Intent(applicationContext, MainMenuActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                applicationContext.startActivity(intent)
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                finish()
                            }
                            "stock" -> {
                                val intent =
                                    Intent(applicationContext, StockMainMenuActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                applicationContext.startActivity(intent)
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                finish()
                            }
                            else -> {
                                Log.e(TAG, "Something went wrong on userType")
                            }
                        }


                    }

                }
            }, 2000)
        }

    }


}
