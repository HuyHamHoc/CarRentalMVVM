package com.carrentalapp.mvvm.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.adapter.CarsListAdapter
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.databinding.ActivitySearchBinding
import com.carrentalapp.mvvm.ui.detail.DetailActivity
import com.carrentalapp.mvvm.ui.home.HomeViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: HomeViewModel
    private val carsAdapter = CarsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setupRecyclerView()

        carsAdapter.itemClickCars = { car ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("yourKey", car.id)
            startActivity(intent)
        }
        binding.btnReturn.setOnClickListener {
            onBackPressed()
        }

        viewModel.loadCarsCategoryList()

        binding.btnSearchCars.setOnClickListener {
            onSearchButtonClicked()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.prgBar.visibility = View.VISIBLE
            } else {
                binding.prgBar.visibility = View.GONE
            }
        }
    }

    private fun onSearchButtonClicked() {
        val searchQuery = binding.edtSearch.text.toString()

        if (searchQuery.isNotEmpty()) {
            searchCars(searchQuery)
        } else {
            showToast(getString(R.string.please_name_car))
        }
    }

    private fun searchCars(query: String) {
        val carsLists = ArrayList<CarsList>()

        viewModel.carsListCategoryGetLiveData().observe(this) { categoryLists ->
            if (categoryLists.isNotEmpty()) {
                categoryLists.forEach { category ->
                    viewModel.loadCarsCategory(category.id)
                }
            }
        }
        viewModel.observerCarsCategoryLiveData().observe(this) { cars ->
            carsLists.addAll(cars.filter { it.name.contains(query, true) })
            carsAdapter.setDataCars(ArrayList(carsLists))
        }
    }
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = carsAdapter
        }
    }
    private fun showToast(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}