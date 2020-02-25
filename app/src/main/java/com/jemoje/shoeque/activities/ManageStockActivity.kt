package com.jemoje.shoeque.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemoje.shoeque.R
import com.jemoje.shoeque.adapters.ShoesAdapter
import com.jemoje.shoeque.adapters.UsersAdapter
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.helper.GridSpacingItemDecoration
import com.jemoje.shoeque.model.DisplayShoesResponse
import com.jemoje.shoeque.model.ShoesData
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_manage_stock.*
import kotlinx.android.synthetic.main.activity_manage_user.*
import retrofit2.Call
import retrofit2.Response

class ManageStockActivity : AppCompatActivity() {
    private val TAG = "ManageStockActivity"
    private var userType: String = ""
    private var shoesAdapter: ShoesAdapter? = null
    private var shoesResponseData: MutableList<ShoesData> = ArrayList()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_manage_stock)
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        userType = intent.getStringExtra("userType")!!
        tv_manage_stock_title.text = userType

        btn_manage_stock_add.setOnClickListener {
            runOnUiThread {
                val intent =
                    Intent(applicationContext, AddUserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("userType", userType)
                applicationContext.startActivity(intent)
                overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
            }
        }
        btn_manage_stock_back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.exit_from, R.anim.exit_to)
        }


        var spanCount = 245f
        var spanSpace = 25
        val numCol = calculateNoOfColumns(this, spanCount)


        rv_manage_stock.layoutManager = GridLayoutManager(applicationContext, 3)
        rv_manage_stock.addItemDecoration(GridSpacingItemDecoration(3,spanSpace, true))
        displayShoesService(userType)

    }

    private fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.getResources().getDisplayMetrics()
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private fun displayShoesService(userType: String){
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.displayShoes("application/json", "Bearer $apiToken")

        callService.enqueue(object: retrofit2.Callback<DisplayShoesResponse>{
            override fun onFailure(call: Call<DisplayShoesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<DisplayShoesResponse>,
                response: Response<DisplayShoesResponse>
            ) {
                when(response.code()){
                    200 -> {
                        shoesResponseData = response.body()!!.data!!
                        shoesAdapter = ShoesAdapter(this@ManageStockActivity, shoesResponseData, userType)
                        rv_manage_stock.adapter = shoesAdapter
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }
}
