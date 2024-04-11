package com.carrentalapp.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var num= 0
        binding.btnIncrease.setOnClickListener {
            num++
            binding.txtQuantity.text = num.toString()
        }

        binding.btnReduce.setOnClickListener {
            num--
            binding.txtQuantity.text = num.toString()
        }
    }
}