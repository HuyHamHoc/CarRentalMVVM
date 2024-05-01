package com.carrentalapp.mvvm.ui.home


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.carrentalapp.mvvm.adapter.CarsCategoryAdapter
import com.carrentalapp.mvvm.adapter.CarsListAdapter
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.data.model.CategoryModel
import com.carrentalapp.mvvm.databinding.FragmentHomeBinding
import com.carrentalapp.mvvm.ui.detail.DetailActivity
import com.carrentalapp.mvvm.ui.search.SearchActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapterCarsList: CarsListAdapter
    private lateinit var adapterCategoryList: CarsCategoryAdapter
    private val categoryList: ArrayList<CategoryModel> = arrayListOf()

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
        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        viewModel.loadCars()
        viewModel.loadCarsCategoryList()
        observerLiveData()
        onClickDetailCars()
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
        viewModel.observerCarsListLiveData().observe(viewLifecycleOwner) { carsLists ->
                adapterCarsList.setDataCars(carsLists as ArrayList<CarsList>)
        }

        viewModel.observerCarsCategoryLiveData().observe(viewLifecycleOwner) { carsLists ->
            adapterCarsList.setDataCars(carsLists as ArrayList<CarsList>)
        }

        //api gọi categoryList
        viewModel.carsListCategoryGetLiveData().observe(viewLifecycleOwner) { category ->
            categoryList.add(CategoryModel("0", "", "All"))
            category.forEach { item ->
                categoryList.add(item)
            }
            adapterCategoryList.setDataCars(categoryList)
        }
    }

    private fun setupRecyclerView() {
        adapterCarsList = CarsListAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterCarsList
        }

        adapterCategoryList = CarsCategoryAdapter()
        binding.rvCategory.apply {
            setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            binding.rvCategory.layoutManager = layoutManager
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL

            adapter = adapterCategoryList
            adapterCategoryList.itemClickCars =
                { id ->
                    if (id == "0") {
                        viewModel.loadCars()
                    } else {
                        viewModel.loadCarsCategory(categoryId = id)
                    }
                }
        }
    }
}