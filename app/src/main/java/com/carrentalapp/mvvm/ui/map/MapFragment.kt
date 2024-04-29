package com.carrentalapp.mvvm.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var isLocationConfirmed = false
    private lateinit var locationViewModel: LocationViewModel

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment).commit()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationViewModel = ViewModelProvider(requireActivity())[LocationViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener { latLng ->
            confirmLocationAndNavigateToBooking(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title("Chọn vị trí này"))
            isLocationConfirmed = true
        }
        setupMap()
    }

    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }


        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
                // muốn mượt hơn 1 tí thì dùng move
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }

    private fun confirmLocationAndNavigateToBooking(latLng: LatLng) {
        val location = Location("").apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }
        locationViewModel.setLocation(location)
        parentFragmentManager.popBackStack()
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }
    override fun onMarkerClick(p0: Marker) = false
}
