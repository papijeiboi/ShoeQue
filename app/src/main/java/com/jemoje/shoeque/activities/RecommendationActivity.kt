package com.jemoje.shoeque.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.adapters.RecommendationAdapter
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.DisplayShoesResponse
import com.jemoje.shoeque.model.ShoesData
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_recommendation.*
import retrofit2.Call
import retrofit2.Response


class RecommendationActivity : AppCompatActivity() {
    private val TAG = "RecAct"
    private var recommendationAdapter: RecommendationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        val dataShoes: String = intent.getStringExtra("DATASHOES")!!
        val gson = Gson()
        val shoeData: ShoesData = gson.fromJson(dataShoes, ShoesData::class.java)
        val recommended = shoeData.description

        val i: Int = recommended!!.indexOf(' ')
        val word: String = recommended.substring(0, i)
        initRecyclerView()
        recommendedService(word)
//        val rest: String = recommended.substring(i)
//        Log.d(TAG, word)

//        initRecyclerView()

        btn_recommendation_back.setOnClickListener {
            finish()
        }

    }
    private fun recommendedService(word: String){
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.recommendation(word, "application/json", "Bearer $apiToken")

        callService.enqueue(object: retrofit2.Callback<DisplayShoesResponse>{
            override fun onFailure(call: Call<DisplayShoesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<DisplayShoesResponse>, response: Response<DisplayShoesResponse>) {
                when (response.code()) {
                    200 -> {
                        val data = response.body()!!.data
                        recommendationAdapter = RecommendationAdapter(this@RecommendationActivity, data!!)
                        rv_recommendation_shoes.adapter = recommendationAdapter
                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }

    private fun initRecyclerView(){
        val mLayoutManagerHome = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rv_recommendation_shoes.layoutManager = mLayoutManagerHome
    }
}
