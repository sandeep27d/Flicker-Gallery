package com.sand.flickergalary

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.sand.flickergalary.ui.main.FlickerActivity

@ExperimentalPagingApi
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, FlickerActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}