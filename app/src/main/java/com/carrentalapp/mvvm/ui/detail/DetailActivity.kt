package com.carrentalapp.mvvm.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivityDetailBinding
import com.carrentalapp.mvvm.ui.payment.PaymentActivity


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var selectedCarId = ""
    private var num = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        binding.btnBackHome.setOnClickListener {
            onBackPressed()
        }

        binding.btnBookNow.setOnClickListener {
            if (num > 0) {
                val intent = Intent(this@DetailActivity, PaymentActivity::class.java).apply {
                    putExtra("carId", selectedCarId)
                    putExtra("quantity", num)
                }
                startActivity(intent)
            } else {
                showToast(getString(R.string.select_quantity))
            }
        }
        intent.getStringExtra("yourKey")?.let { id ->
            selectedCarId = id
        }

        intent.getStringExtra("yourKey")?.let { id ->
            binding.prgBar.visibility = View.VISIBLE
            viewModel.loadCarsDetail(id)
            viewModel.observerCarsDetailLiveData().observe(this) { carsList ->
                binding.prgBar.visibility = View.GONE
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
        binding.btnIncrease.setOnClickListener { binding.txtQuantity.text = (++num).toString() }
        binding.btnReduce.setOnClickListener { if (num > 0) binding.txtQuantity.text = (--num).toString() }
    }
    private fun showToast(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}