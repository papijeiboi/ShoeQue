package com.jemoje.shoeque.activities

import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.jemoje.shoeque.R
import com.jemoje.shoeque.adapters.ShoesAdapter
import com.jemoje.shoeque.adapters.StockOrderAdapter
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.helper.GridSpacingItemDecoration
import com.jemoje.shoeque.model.DisplayOrdersResponse
import com.jemoje.shoeque.model.DisplayShoesResponse
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_manage_stock.*
import kotlinx.android.synthetic.main.activity_stock_order.*
import retrofit2.Call
import retrofit2.Response

class StockOrderActivity : AppCompatActivity() {
    private val TAG = "StockOrderActivity"
    private var stockOrderAdapter: StockOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_stock_order)
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

//        rv_stock_order

        btn_stock_order_back.setOnClickListener {
            runOnUiThread {
                finish()
                overridePendingTransition(R.anim.exit_from, R.anim.exit_to)
            }
        }

        var spanCount = 245f
        var spanSpace = 25


        rv_stock_order.layoutManager = GridLayoutManager(applicationContext, 3)
        rv_stock_order.addItemDecoration(GridSpacingItemDecoration(3,spanSpace, true))
        displayOrders()
    }

    private fun displayOrders(){
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.displayOrders("created_at", "asc", "application/json", "Bearer $apiToken")

        callService.enqueue(object : retrofit2.Callback<DisplayOrdersResponse>{
            override fun onFailure(call: Call<DisplayOrdersResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<DisplayOrdersResponse>,
                response: Response<DisplayOrdersResponse>
            ) {
                when(response.code()){
                    200 -> {
                        val orders = response.body()!!.data
                        stockOrderAdapter = StockOrderAdapter(this@StockOrderActivity, orders!!)
                        rv_stock_order.adapter = stockOrderAdapter
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }
}
