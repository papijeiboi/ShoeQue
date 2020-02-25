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
import com.jemoje.shoeque.model.DeleteResponse
import com.jemoje.shoeque.model.UserData
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_add_user.*
import kotlinx.android.synthetic.main.activity_update_user.*
import retrofit2.Call
import retrofit2.Response

class UpdateUserActivity : AppCompatActivity() {
    private val TAG = "UpdateUserActivity"
    private var selectedUserId = ""
    private var userType: String = ""

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_update_user)

        userType = intent.getStringExtra("userType")!!
        selectedUserId = intent.getStringExtra("selectedUserId")!!

        btn_update_user_create.setOnClickListener {
            val name = edt_update_user_name.text.toString().trim()
            val email = edt_update_user_email.text.toString().trim()
            val password = edt_update_user_password.text.toString().trim()
            val confirmPassword = edt_update_user_password_confirmation.text.toString().trim()
            validateEditText(name, email, password, confirmPassword)
        }

        btn_update_user_back.setOnClickListener {
            runOnUiThread {
                finish()
                overridePendingTransition(R.anim.exit_from, R.anim.exit_to)

            }
        }

        btn_update_user_delete.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete?")

            builder.setPositiveButton("YES") { dialog, which ->
                progress_layout_update_user.visibility = View.VISIBLE
                deleteUser(selectedUserId)
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


        getUserInformationService(selectedUserId)


    }

    private fun validateEditText(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (name.isEmpty() && email.isEmpty()) {
            displayDialog("Fields must not be empty!")
        } else if (name.isEmpty()) {
            displayDialog("Name must not be empty!")
        } else if (email.isEmpty()) {
            displayDialog("Email must not be empty!")
        } else if (name.isNotEmpty() && email.isNotEmpty()) {

            if (isEmailValid(email)) {
                if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (password.isEmpty()){
                        displayDialog("Password must not be empty!")
                    }else if(confirmPassword.isEmpty()){
                        displayDialog("Confirm Password must not be empty!")
                    }else {
                        if (password == confirmPassword) {
                            if (password.length >= 8 && confirmPassword.length >= 8) {
                                //HAVE PASSWORD
                                progress_layout_update_user.visibility = View.VISIBLE
                                updateUserInformationService(name, email, password, confirmPassword, selectedUserId)
                            } else {
                                displayDialog("The password must be at least 8 characters.")
                            }
                        } else {
                            displayDialog("Password does not match!")
                        }
                    }
                } else {
                    progress_layout_update_user.visibility = View.VISIBLE
                    updateUserInformationService(name, email, null, null, selectedUserId)
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

    private fun getUserInformationService(userId: String) {
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService =
            apiService.getUserInformation(userId, "application/json", "Bearer $apiToken")

        callService.enqueue(object : retrofit2.Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                when (response.code()) {
                    200 -> {
                        val userData = response.body()
                        edt_update_user_name.setText(userData!!.name)
                        edt_update_user_email.setText(userData.email)
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                        Log.e(TAG, "${response.errorBody()} ${response.code()}")
                    }
                }
            }
        })
    }

    private fun updateUserInformationService(
        name: String,
        email: String,
        password: String?,
        confirmPassword: String?,
        userId: String
    ) {
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        if (password == null && confirmPassword == null) {
            val callService = apiService.updateUserInformation(
                name,
                email,
                userId,
                userType,
                "PUT",
                userId,
                "application/json",
                "Bearer $apiToken"
            )

            callService.enqueue(object : retrofit2.Callback<UserData>{
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    progress_layout_update_user.visibility = View.GONE
                    Log.e(TAG, "onFailure: ${t.message}")
                }

                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    progress_layout_update_user.visibility = View.GONE
                    when (response.code()) {
                        200 -> {
                            finish()
                            val intent =
                                Intent(applicationContext, SuccessUpdateUserActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("userType", userType)
                            applicationContext.startActivity(intent)
                            overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                        }
                        else -> {
                            Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                            Log.e(TAG, "${response.errorBody()} ${response.code()}")
                        }
                    }
                }
            })


        } else {
            val callService =  apiService.updateUserInformationWithPassword(
                name,
                email,
                password!!,
                confirmPassword!!,
                userId,
                userType,
                "PUT",
                userId,
                "application/json",
                "Bearer $apiToken"
            )
            callService.enqueue(object : retrofit2.Callback<UserData>{
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    progress_layout_update_user.visibility = View.GONE
                    Log.e(TAG, "onFailure: ${t.message}")
                }

                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    progress_layout_update_user.visibility = View.GONE
                    when (response.code()) {
                        200 -> {
                            finish()
                            val intent =
                                Intent(applicationContext, SuccessUpdateUserActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("userType", userType)
                            applicationContext.startActivity(intent)
                            overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                        }
                        else -> {
                            Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                            Log.e(TAG, "${response.errorBody()} ${response.code()}")
                        }
                    }
                }
            })
        }
    }

    private fun deleteUser(userId: String){
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.deleteUser(userId, "application/json",
            "Bearer $apiToken")

        callService.enqueue(object : retrofit2.Callback<DeleteResponse>{
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                progress_layout_update_user.visibility = View.GONE
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                progress_layout_update_user.visibility = View.GONE
                when (response.code()) {
                    200 -> {
                        finish()
                        val intent =
                            Intent(applicationContext, DeleteUpdateUserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("userType", userType)
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                        Log.e(TAG, "${response.errorBody()} ${response.code()}")
                    }
                }
            }
        })
    }
}
