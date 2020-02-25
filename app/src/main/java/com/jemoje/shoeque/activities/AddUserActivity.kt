package com.jemoje.shoeque.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.jemoje.shoeque.R
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.UserData
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_add_user.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class AddUserActivity : AppCompatActivity() {
    private val TAG = "AddUserActivity"
    private var userType: String = ""

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_add_user)

        userType = intent.getStringExtra("userType")!!
        if (userType == "Stock") {
            userType = "stock"
        } else if (userType == "Sale") {
            userType = "sale"
        } else if (userType == "Admin") {
            userType = "admin"
        }

        btn_add_user_back.setOnClickListener {
            runOnUiThread {
                finish()
                overridePendingTransition(R.anim.exit_from, R.anim.exit_to)

            }
        }

        btn_add_user_create.setOnClickListener {
            val name = edt_add_user_name.text.toString().trim()
            val email = edt_add_user_email.text.toString().trim()
            val password = edt_add_user_password.text.toString().trim()
            val confirmationPassword = edt_add_user_password_confirmation.text.toString().trim()
            validateEditText(name, email, password, confirmationPassword)
        }
    }

    private fun validateEditText(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
            displayDialog("Fields must not be empty!")
        } else if (name.isEmpty()) {
            displayDialog("Name must not be empty!")
        } else if (email.isEmpty()) {
            displayDialog("Email must not be empty!")
        } else if (password.isEmpty()) {
            displayDialog("Password must not be empty!")
        } else if (confirmPassword.isEmpty()) {
            displayDialog("Confirmation Password must not be empty!")
        } else if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {


            if (isEmailValid(email)) {
                if (password == confirmPassword) {
                    if (password.length >= 8 && confirmPassword.length >= 8) {
                        progress_layout_add_user.visibility = View.VISIBLE
                        addUserService(name, email, password, confirmPassword, userType)
                    } else {
                        displayDialog("The password must be at least 8 characters.")
                    }
                } else {
                    displayDialog("Password does not match!")
                }
            } else {
                displayDialog("Please enter valid email!")
            }
        }
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

    private fun addUserService(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        userType: String
    ) {

        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.addUsers(
            name,
            email,
            password,
            passwordConfirmation,
            userType,
            "application/json",
            "Bearer $apiToken"
        )

        callService.enqueue(object : retrofit2.Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                progress_layout_add_user.visibility = View.GONE
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                progress_layout_add_user.visibility = View.GONE
                when (response.code()) {
                    201 -> {
                        finish()
                        val intent =
                            Intent(applicationContext, SuccessAddUserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("userType", userType)
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                    else -> {
//
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                        Log.e(TAG, "${response.errorBody()} ${response.code()}")
                        displayDialog("The given data was invalid.")
                    }
                }
            }
        })
    }
}
