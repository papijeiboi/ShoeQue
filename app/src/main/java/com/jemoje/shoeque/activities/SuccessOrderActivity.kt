package com.jemoje.shoeque.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jemoje.shoeque.R
import kotlinx.android.synthetic.main.activity_success_order.*

class SuccessOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        btn_success_order.setOnClickListener {
            finish()
            val intent =
                Intent(
                    applicationContext,
                    MainMenuActivity::class.java
                )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
            overridePendingTransition(R.anim.enter_from, R.anim.enter_to)
        }
    }
}
