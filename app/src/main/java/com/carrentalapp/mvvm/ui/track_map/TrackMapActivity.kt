package com.carrentalapp.mvvm.ui.track_map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivityTrackMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class TrackMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTrackMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.trackingMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.icBackTrackMap.setOnClickListener {
            onBackPressed()
        }

        val selectedLocation = intent.getStringExtra("selectedLocation")
        binding.tvLocation.text = selectedLocation
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        val bmt = LatLng(13.716198164080684, 109.2107557923857)
        mMap.addMarker(MarkerOptions().title("TMA Innovation Park").position(bmt))

        mMap.addPolyline(
            PolylineOptions().add(
                bmt,
            ).width(10f)
                .color(Color.RED)
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bmt, 18f))
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }
}



