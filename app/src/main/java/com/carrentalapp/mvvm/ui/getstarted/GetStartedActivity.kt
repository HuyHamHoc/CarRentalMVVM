package com.carrentalapp.mvvm.ui.getstarted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import com.carrentalapp.mvvm.databinding.ActivitySplashSecondBinding
import com.carrentalapp.mvvm.ui.signin.SignInActivity

class GetStartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashSecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        this.window.setFlags(
            LayoutParams.FLAG_FULLSCREEN,
            LayoutParams.FLAG_FULLSCREEN,
        )

        binding.btnGet.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}