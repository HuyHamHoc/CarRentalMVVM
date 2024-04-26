package com.carrentalapp.mvvm.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.carrentalapp.mvvm.MainActivity
import com.carrentalapp.mvvm.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        binding.btnBackHome.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        intent.getStringExtra("yourKey")?.let { id ->
            viewModel.loadCarsDetail(id)

            viewModel.observerCarsDetailLiveData().observe(this) { carsList ->
                carsList?.firstOrNull()?.let { detail ->
                    with(binding) {
                        tvCarsName.text = detail.name
                        tvPriceDay.text = "$${detail.price}"
                        tvSpeed.text = "${detail.speed}kmph"
                        tvSeat.text = detail.seat.toString()
                        Glide.with(this@DetailActivity)
                            .load(detail.picture)
                            .into(imgCarDetail)
                    }
                }
            }
        }

        var num = 0
        binding.btnIncrease.setOnClickListener { binding.txtQuantity.text = (++num).toString() }
        binding.btnReduce.setOnClickListener { if (num > 0) binding.txtQuantity.text = (--num).toString() }
    }
}