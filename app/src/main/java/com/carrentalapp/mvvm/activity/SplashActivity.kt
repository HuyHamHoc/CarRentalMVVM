package com.carrentalapp.mvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.slide)
        binding.imgView.startAnimation(sideAnimation)

        Handler().postDelayed({
            startActivity(Intent(this,SplashSecondActivity::class.java))
            finish()
        },3000)
    }
}