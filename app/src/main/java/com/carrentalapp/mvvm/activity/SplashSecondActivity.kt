package com.carrentalapp.mvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivitySplashBinding
import com.carrentalapp.mvvm.databinding.ActivitySplashSecondBinding

class SplashSecondActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashSecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        supportActionBar?.hide()
        binding = ActivitySplashSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        this.window.setFlags(
            LayoutParams.FLAG_FULLSCREEN,
            LayoutParams.FLAG_FULLSCREEN,
        )

        binding.btnGet.setOnClickListener {
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
    }
}