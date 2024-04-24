package com.carrentalapp.mvvm.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.carrentalapp.mvvm.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]


        val detailId = intent.getStringExtra("yourKey")
        detailId?.let { id ->

            viewModel.loadCarsDetail(id)

            viewModel.observerCarsDetailLiveData().observe(this) { carsList ->
                carsList?.let { details ->
                    val detail = details.firstOrNull()
                    detail?.let {
                        binding.tvCarsName.text = it.name
                        binding.tvPriceDay.text = "$${it.price}/day"
                        binding.tvSpeed.text = "${it.speed}kmph"
                        binding.tvSeat.text = it.seat.toString()
                        Glide.with(this)
                            .load(it.picture)
                            .into(binding.imgCarDetail)
                    }
                }
            }
        }

        var num = 0

        binding.btnIncrease.setOnClickListener {
            num++
            binding.txtQuantity.text = num.toString()
        }

        binding.btnReduce.setOnClickListener {
            if (num > 0) {
                num--
                binding.txtQuantity.text = num.toString()
            }
        }

    }
}