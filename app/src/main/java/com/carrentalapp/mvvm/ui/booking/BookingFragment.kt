package com.carrentalapp.mvvm.ui.booking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carrentalapp.mvvm.adapter.CarsMyBookAdapter
import com.carrentalapp.mvvm.data.model.Transaction
import com.carrentalapp.mvvm.databinding.FragmentBookingBinding
import com.carrentalapp.mvvm.ui.payment.PaymentViewModel
import com.carrentalapp.mvvm.ui.track_map.TrackMapActivity


class BookingFragment : Fragment() {
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var binding: FragmentBookingBinding
    private lateinit var adapterCarsListMyBook: CarsMyBookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentViewModel.getTransaction(getCustomerId())
        setupRecyclerView()
        observerLiveData()
        onClickDetailCars()
    }

    private fun observerLiveData() {
        paymentViewModel.observerCarsMyBookLiveData().observe(viewLifecycleOwner) { carsLists ->
            adapterCarsListMyBook.setDataCars(carsLists as ArrayList<Transaction>)
            Log.d( "tag", carsLists.toString())
        }
    }

    private fun getCustomerId(): String {
        // Nhận customerId từ Intent
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("customerId", "") ?: ""
    }

    private fun setupRecyclerView() {
        adapterCarsListMyBook = CarsMyBookAdapter()
        binding.rvBooking.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterCarsListMyBook
        }
    }

    private fun onClickDetailCars() {
        adapterCarsListMyBook.itemClickCars = { cars ->
            val intent = Intent(context, TrackMapActivity::class.java).apply {
                putExtra("trackMap", cars.carId)
            }
            context?.startActivity(intent)
        }
    }
}