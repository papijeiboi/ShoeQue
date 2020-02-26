package com.jemoje.shoeque.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.gson.Gson
import com.jemoje.shoeque.R
import com.jemoje.shoeque.adapters.UsersAdapter
import com.jemoje.shoeque.constant.Keys
import com.jemoje.shoeque.model.ShoesData
import com.jemoje.shoeque.webservice.UserService
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_manage_user.*
import kotlinx.android.synthetic.main.activity_scan.*
import retrofit2.Call
import retrofit2.Response

class ScanActivity : AppCompatActivity() {
    private val TAG = "ScanActivity"
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                scanWebService(it.text)
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        btn_scan_back.setOnClickListener {
            runOnUiThread {
                finish()
                overridePendingTransition(R.anim.exit_from, R.anim.exit_to)

            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun scanWebService(qrCode: String) {
        val apiToken = Prefs.getString(Keys.USER_TOKEN, "")
        val apiService = UserService.create(this.getString(R.string.base_url))

        val callService = apiService.scanQr(qrCode, "application/json", "Bearer $apiToken")

        callService.enqueue(object : retrofit2.Callback<ShoesData> {
            override fun onFailure(call: Call<ShoesData>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<ShoesData>, response: Response<ShoesData>) {
                when (response.code()) {
                    200 -> {

                        if (response.body()!!.sizes.isNullOrEmpty()){
                            val shoeDetails = response.body()
                            val gSon = Gson()
                            val shoeString = gSon.toJson(shoeDetails)

                            val intent = Intent(applicationContext, RecommendationActivity::class.java)
                            intent.putExtra("DATASHOES", shoeString)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            applicationContext.startActivity(intent)
                            overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                        }else{
                            val shoeDetails = response.body()
                            val gSon = Gson()
                            val shoeString = gSon.toJson(shoeDetails)

                            val intent = Intent(applicationContext, OrderActivity::class.java)
                            intent.putExtra("DATASHOES", shoeString)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            applicationContext.startActivity(intent)
                            overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
                        }

                    }
                    else -> {
                        Log.e(TAG, "callWebService responseCode: ${response.errorBody()}")
                    }
                }
            }
        })
    }
}
