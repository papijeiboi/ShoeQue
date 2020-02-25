package com.jemoje.shoeque.activities

import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.gson.GsonBuilder
import com.jemoje.shoeque.R
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.UserResponse
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_landing_menu.*
import retrofit2.Call
import retrofit2.Response

class LandingMenuActivity : AppCompatActivity() {
    private val TAG = "LandingMenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_menu)

        Prefs.Builder()
            .setContext(applicationContext)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val handleClicks = View.OnClickListener {
            when (it) {
                btn_landing_sign_in -> {
                    runOnUiThread {
                        val email = edt_landing_email.text.toString().trim()
                        val password = edt_landing_password.text.toString().trim()

                        validateEmailPassword(email, password)
                    }
                }

            }
        }

        btn_landing_sign_in.setOnClickListener(handleClicks)


    }

    private fun displayDialog(message: String) {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(message)


        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateEmailPassword(email: String, password: String) {
        if (email.isEmpty() && password.isEmpty()) {
            displayDialog("Email and Password must not be empty!")
        } else if (email.isEmpty() && password.isNotEmpty()) {
            displayDialog("Email must not be empty!")
        } else if (email.isNotEmpty() && password.isEmpty()) {
            displayDialog("Password must not be empty!")
        } else if (email.isNotEmpty() && password.isNotEmpty()) {

            if (isEmailValid(email)) {
                progress_layout_landing.visibility = View.VISIBLE
                callWebService(email, password)
            } else {
                displayDialog("Please enter valid email.")
            }

        }
    }

    private fun callWebService(email: String, password: String) {

        val apiService = UserService.create(this.getString(R.string.base_url))
        val callService = apiService.login(email, password)

        callService.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                progress_layout_landing.visibility = View.GONE
                Log.e(TAG, " onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                progress_layout_landing.visibility = View.GONE
                when (response.code()) {
                    200 -> {
                        val userResponse: UserResponse? = response.body()

                        Prefs.putString(
                            Keys.USER_TOKEN,
                            userResponse!!.token!!.accessToken
                        )

                        val mGson = GsonBuilder()
                            .setLenient()
                            .create()
                        Prefs.putString(
                            Keys.USER_FULL_DATA,
                            mGson.toJson(response.body()!!.user)
                        )

                        when (userResponse.user!!.userType) {
                            "admin" -> {
                                runOnUiThread {
                                    finish()
                                    val intent =
                                        Intent(
                                            applicationContext,
                                            AdminMainMenuActivity::class.java
                                        )
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    applicationContext.startActivity(intent)
                                    overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                                }
                            }
                            "sale" -> {
                                runOnUiThread {
                                    finish()
                                    val intent =
                                        Intent(applicationContext, MainMenuActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    applicationContext.startActivity(intent)
                                    overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                                }
                            }
                            "stock" -> {
                                runOnUiThread {
                                    finish()
                                    val intent =
                                        Intent(
                                            applicationContext,
                                            StockMainMenuActivity::class.java
                                        )
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    applicationContext.startActivity(intent)
                                    overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                                }
                            }
                        }
                    }
                    401 -> {
                        displayDialog("Your credentials are incorrect. Please try again.")
                    }
                    400 -> {
                        displayDialog("Invalid Request. Please enter a username or a password")
                    }
                    else -> {
                        displayDialog("Something went wrong!")
                    }
                }

            }
        })
    }
}
