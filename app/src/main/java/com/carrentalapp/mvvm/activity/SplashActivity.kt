package com.carrentalapp.mvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivitySplashBinding
import com.carrentalapp.mvvm.databinding.ActivitySplashSecondBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        supportActionBar?.hide()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.slide)
        binding.imgView.startAnimation(sideAnimation)

        Handler().postDelayed({
            startActivity(Intent(this,SplashSecondActivity::class.java))
            finish()
        },3000)
    }
}