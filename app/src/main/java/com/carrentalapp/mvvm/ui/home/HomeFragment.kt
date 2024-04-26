package com.carrentalapp.mvvm.ui.home


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.adapter.CarsListAdapter
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.databinding.FragmentHomeBinding
import com.carrentalapp.mvvm.ui.detail.DetailActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapterCarsList: CarsListAdapter
    private val categoryIds = arrayOf(
        "8db69767-31f8-4004-a264-8b4eb0bd40b0",
        "82b18129-4c03-4130-9cb9-6bb5561f37e8",
        "450406ae-b525-46ad-b888-24dfeff1ab20",
        "4a2dc53b-733e-40e6-b86d-fd751e2b4d1d",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.loadCars()
        observerLiveData()
        onClickDetailCars()

        val categoryTextViews = arrayOf(
            binding.tvCate1,
            binding.tvCate2,
            binding.tvCate3,
            binding.tvCate4,
            binding.tvCateAll,
        )

        categoryTextViews.forEach { textView ->
            textView.setOnClickListener {
                if (textView == binding.tvCateAll) {
                    viewModel.loadCars()
                    // Khôi phục màu sắc của các TextView khác
                    selectCategory(textView)
                    categoryTextViews.filter { it != textView }.forEach { deselectCategory(it) }
                } else {

                    val index = categoryTextViews.indexOf(textView)
                    if (index != -1 && index < categoryIds.size) {
                        val categoryId = categoryIds[index]
                        viewModel.loadCarsCategory(categoryId)
                        // Thay đổi màu sắc của TextView khi được chọn
                        selectCategory(textView)
                        // Khôi phục màu sắc của các TextView khác
                        categoryTextViews.filter { it != textView }.forEach { deselectCategory(it) }
                    }
                }
            }
        }
        selectCategory(binding.tvCateAll)
    }



    private fun selectCategory(textView: TextView) {
        textView.setBackgroundColor(Color.parseColor("#2B4C59"))
        textView.setTextColor(Color.WHITE)
    }

    private fun deselectCategory(textView: TextView) {
        textView.setBackgroundResource(R.drawable.bg_dark_salmon_outside)
        textView.setTextColor(Color.BLACK)
    }

    private fun onClickDetailCars() {
        adapterCarsList.itemClickCars = { cars ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("yourKey", cars.id)
            }
            context?.startActivity(intent)
        }
    }

    private fun observerLiveData() {
        val isAllSelected = true

        viewModel.observerCarsListLiveData().observe(viewLifecycleOwner) { carsLists ->
            if (isAllSelected) {
                adapterCarsList.setDataCars(carsLists as ArrayList<CarsList>)
            }
        }

        viewModel.observerCarsCategoryLiveData().observe(viewLifecycleOwner) { carsLists ->
            adapterCarsList.setDataCars(carsLists as ArrayList<CarsList>)
        }
    }

    private fun setupRecyclerView() {
        adapterCarsList = CarsListAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterCarsList
        }
    }
}