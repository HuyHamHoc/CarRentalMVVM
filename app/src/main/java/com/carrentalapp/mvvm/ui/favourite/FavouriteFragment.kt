package com.carrentalapp.mvvm.ui.favourite


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carrentalapp.mvvm.adapter.CarsFavouriteAdapter
import com.carrentalapp.mvvm.databinding.FragmentFavoriteBinding
import com.carrentalapp.mvvm.ui.detail.DetailActivity

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var adapterCarsList: CarsFavouriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.getCarsFavourite(getCustomerId())
        observeCarsFavourite()
        onClickDetailCars()

    }



    private fun getCustomerId(): String {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("customerId", "") ?:""
    }


    private fun onClickDetailCars() {
        adapterCarsList.itemClickCars = { cars ->
            binding.prgBar.visibility = View.GONE
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("yourKey", cars.carId)
            }
            context?.startActivity(intent)
        }
    }

    private fun observeCarsFavourite() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.prgBar.visibility = View.VISIBLE
            } else {
                binding.prgBar.visibility = View.GONE
            }
        }

        viewModel.observerCarsFavouriteLiveData().observe(viewLifecycleOwner) { carsFavouriteList ->
            adapterCarsList.setDataCars(ArrayList(carsFavouriteList))
        }
    }
    private fun setupRecyclerView() {
        adapterCarsList = CarsFavouriteAdapter { favouriteId ->
            val customerId = getCustomerId()
            binding.prgBar.visibility = View.VISIBLE
            viewModel.removeCarsFavourite(favouriteId)
            viewModel.getCarsFavourite(customerId)
            binding.prgBar.visibility = View.GONE
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterCarsList
        }
    }
}