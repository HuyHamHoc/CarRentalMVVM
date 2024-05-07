package com.carrentalapp.mvvm.ui.home


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.carrentalapp.mvvm.adapter.CarsCategoryAdapter
import com.carrentalapp.mvvm.adapter.CarsListAdapter
import com.carrentalapp.mvvm.data.model.CarsFavourite
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.data.model.CategoryModel
import com.carrentalapp.mvvm.databinding.FragmentHomeBinding
import com.carrentalapp.mvvm.ui.detail.DetailActivity
import com.carrentalapp.mvvm.ui.favourite.FavouriteViewModel
import com.carrentalapp.mvvm.ui.search.SearchActivity


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFavourite: FavouriteViewModel
    private lateinit var adapterCarsList: CarsListAdapter
    private lateinit var adapterCategoryList: CarsCategoryAdapter
    //    private lateinit var favouriteList: List<CarsFavourite>
    private val categoryList: ArrayList<CategoryModel> = arrayListOf()
    private val addedToFavouriteList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModelFavourite = ViewModelProvider(this)[FavouriteViewModel::class.java]
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
        viewModelFavourite.getCarsFavourite(getCustomerId())
        observerLiveData()
        onClickDetailCars()
        onClickFavouriteCars()
    }

    private fun onClickDetailCars() {
        adapterCarsList.itemClickCars = { cars ->
            binding.prgBar.visibility = View.GONE
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("yourKey", cars.id)
            }
            context?.startActivity(intent)
        }
    }
    private fun getCustomerId(): String {
        // Nhận customerId từ Intent
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("customerId", "") ?: ""
    }

    @SuppressLint("SuspiciousIndentation")
    private fun onClickFavouriteCars() {
        adapterCarsList.itemClickCarsFavourite = { carsList ->
            // Kiểm tra xem xe có trong danh sách yêu thích chưa
            if (!addedToFavouriteList.contains(carsList.id)) {
                // Nếu chưa có, thêm xe vào danh sách và thông báo thành công
                val favourite = CarsFavourite(
                    carId = carsList.id,
                    name = carsList.name,
                    seat = carsList.seat,
                    speed = carsList.speed,
                    price = carsList.price,
                    picture = carsList.picture,
                    categoryId = carsList.categoryId,
                    customerId = getCustomerId()
                )
                viewModelFavourite.addCarsFavourite(favourite)
                addedToFavouriteList.add(carsList.id)
                Toast.makeText(requireActivity(), "Add Success", Toast.LENGTH_SHORT).show()
            } else {
                // Nếu đã có trong danh sách, thông báo rằng xe đã tồn tại
                Toast.makeText(
                    requireActivity(),
                    "This car already exists in the Favourite List",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observerLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.prgBar.visibility = View.VISIBLE
            } else {
                binding.prgBar.visibility = View.GONE
            }
        }
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
        viewModelFavourite.observerCarsFavouriteLiveData()
            .observe(viewLifecycleOwner) {
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
        adapterCategoryList.setSelectedPosition(0)
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