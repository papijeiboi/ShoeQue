package com.jemoje.shoeque.activities

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemoje.shoeque.R
import com.jemoje.shoeque.adapters.UsersAdapter
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.GetUsersResponse
import com.jemoje.shoeque.model.UserData
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_manage_user.*
import retrofit2.Call
import retrofit2.Response

class ManageUserActivity : AppCompatActivity() {
    private val TAG = "ManageUserActivity"
    private var userType: String = ""
    private var usersAdapter: UsersAdapter? = null
    private var usersResponseData: MutableList<UserData> = ArrayList()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_manage_user)
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        userType = intent.getStringExtra("userType")!!
        tv_manage_user_title.text = userType

        btn_manage_user_back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.exit_from, R.anim.exit_to)
        }

        btn_manage_user_add.setOnClickListener {
            runOnUiThread {
                val intent =
                    Intent(applicationContext, AddUserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("userType", userType)
                applicationContext.startActivity(intent)
                overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
            }
        }

        initRecyclerView()
        displayUsersService(userType)
    }

    private fun displayUsersService(userType: String){

        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.getUsers(userType, "application/json", "Bearer $apiToken")

        callService.enqueue(object: retrofit2.Callback<GetUsersResponse>{
            override fun onFailure(call: Call<GetUsersResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<GetUsersResponse>,
                response: Response<GetUsersResponse>
            ) {
                when(response.code()){
                    200 -> {
                        usersResponseData = response.body()!!.data!!
                        usersAdapter = UsersAdapter(this@ManageUserActivity, usersResponseData, userType)
                        rv_manage_user.adapter = usersAdapter
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {

        val mLayoutManagerHome = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_manage_user.layoutManager = mLayoutManagerHome
//        realView.rv_shops.adapter = reservationAdapter
    }

}
