package com.jemoje.shoeque.activities

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.adapters.ShoeImageAdapter
import com.jemoje.shoeque.adapters.ShoeInfoAdapter
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.*
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_manage_user.*
import kotlinx.android.synthetic.main.activity_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderActivity : AppCompatActivity() {
    private var shoeInfoAdapter: ShoeInfoAdapter? = null
    private var sizesData: MutableList<SizesData> = ArrayList()
    private val TAG = "OrderActivity"
    private var dataFromApi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        dataFromApi = Prefs.getString(Keys.USER_FULL_DATA, "")
        val profileResponse = Gson().fromJson<UserData>(dataFromApi, UserData::class.java)

        val dataShoes: String = intent.getStringExtra("DATASHOES")!!

        val gson = Gson()
        val shoeData: ShoesData = gson.fromJson(dataShoes, ShoesData::class.java)
        val shoeImages = shoeData.images
        sizesData = shoeData.sizes!!

        btn_review.setOnClickListener {
            serviceOrderId(profileResponse.id!!)
        }



        tv_order_sales_name.text = shoeData.name
        tv_order_sales_description.text = shoeData.description
        tv_order_sales_price.text = shoeData.price
        shoesImagesDisplay(shoeImages!!)
        initRecyclerView(sizesData)

    }

    private fun shoesImagesDisplay(images: MutableList<ImagesData>) {
        val adapter = ShoeImageAdapter(this, images)
        view_pager_shoes.adapter = adapter
        view_pager_shoes.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { /*empty*/
            }

            override fun onPageSelected(position: Int) {
                pageIndicatorView.selection = position
            }

            override fun onPageScrollStateChanged(state: Int) { /*empty*/
            }
        })
    }

    private fun initRecyclerView(
        shoe: MutableList<SizesData>
    ) {

        val mLayoutManagerHome = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_order_sales.layoutManager = mLayoutManagerHome
        shoeInfoAdapter = ShoeInfoAdapter(this, shoe)
        rv_order_sales.adapter = shoeInfoAdapter

    }

    private var itemSelected: MutableList<SizesData> = ArrayList()
    fun selectItem(position: Int) {
        sizesData[position].isSelected = true
        itemSelected.add(sizesData[position])
    }

    fun deSelectItem(position: Int) {
        sizesData[position].isSelected = false
        itemSelected.remove(sizesData[position])
    }

    private fun serviceOrderId(salesId: String) {
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService =
            apiService.createOrderId(salesId, "14", "application/json", "Bearer $apiToken")

        callService.enqueue(object : retrofit2.Callback<OrderIdResponse> {
            override fun onFailure(call: Call<OrderIdResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<OrderIdResponse>,
                response: Response<OrderIdResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val orderId = response.body()!!.id
                        itemSelected.forEach {
                            orderProduct(orderId!!, it.productId!!, it.id!!)
                        }
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }

    private fun orderProduct(orderId: String, productId: String, sizeId: String) {
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.orderProduct(
            orderId,
            productId,
            sizeId,
            "application/json",
            "Bearer $apiToken"
        )
        callService.enqueue(object : retrofit2.Callback<OrderProductResponse> {
            override fun onFailure(call: Call<OrderProductResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<OrderProductResponse>,
                response: Response<OrderProductResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        finish()
                        val intent =
                            Intent(applicationContext, SuccessOrderActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(intent)
                        overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }
}
