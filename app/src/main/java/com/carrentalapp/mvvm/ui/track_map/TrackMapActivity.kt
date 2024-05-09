package com.carrentalapp.mvvm.ui.track_map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.carrentalapp.mvvm.MainActivity
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

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
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
                LatLng(13.715326124109268, 109.20863789744938),
                LatLng(13.716858730276249, 109.20840908592997),
                LatLng(13.71735009807406, 109.20815618898747),
                LatLng(13.718356228926783, 109.2066990208903),
                LatLng(13.719584638688122, 109.20482035799932),
                LatLng(13.720087699864084, 109.20444703394072),
                LatLng(13.72095343052585, 109.20441090580609),
                LatLng(13.721971219742098, 109.20482252128207),
                LatLng(13.722810870471003, 109.20528661509078),
                LatLng(13.723236898992605, 109.20614667887027),
                LatLng(13.723307214319854, 109.2072324029283),
                LatLng(13.724490162912812, 109.20852675632034),
                LatLng(13.726417611851144, 109.20813504408244),
                LatLng(13.730007653184524, 109.20812223561991),
                LatLng(13.733333034832402, 109.21025962184511),
                LatLng(13.734458028946424, 109.21002970382635),
                LatLng(13.735434122350135, 109.20872683492753),
                LatLng(13.740979025277753, 109.2074246528524),
                LatLng(13.755273596713426, 109.20821947179147),
                LatLng(13.774584200545426, 109.22107081394242),
                LatLng(13.77543803349354, 109.22225099954848),
                LatLng(13.782221800375657, 109.22037233658594),
                LatLng(13.78304051761616, 109.2194330051212)

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



