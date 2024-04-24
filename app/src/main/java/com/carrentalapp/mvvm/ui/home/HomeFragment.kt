package com.carrentalapp.mvvm.ui.home


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.carrentalapp.mvvm.adapter.CarsListAdapter
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.databinding.FragmentHomeBinding
import com.carrentalapp.mvvm.ui.detail.DetailActivity


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var adapterCarsList: CarsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.loadCars()
        observerLiveData()
        onClickDetailCars()
    }

    private fun onClickDetailCars() {
        adapterCarsList.itemClickCars = { cars ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("yourKey",cars.id)
            }
            context?.startActivity(intent)
        }
    }

    private fun observerLiveData() {
        viewModel.observerCarsListLiveData().observe(viewLifecycleOwner) { carsLists ->
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