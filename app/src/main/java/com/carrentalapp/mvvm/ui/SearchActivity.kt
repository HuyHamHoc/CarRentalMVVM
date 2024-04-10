package com.carrentalapp.mvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.carrentalapp.mvvm.MainActivity
import com.carrentalapp.mvvm.adapter.CarsAdapter
import com.carrentalapp.mvvm.databinding.ActivitySearchBinding
import com.carrentalapp.mvvm.model.Cars

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val carsAdapter = CarsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        val cars = List(100) { index ->
            Cars(
                name = "BNW", price = 300
            )
        }
        carsAdapter.setCars(cars)

        binding.btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = carsAdapter
        }
    }
}