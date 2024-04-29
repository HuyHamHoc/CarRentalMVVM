package com.carrentalapp.mvvm.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    fun setLocation(location: Location) {
        _location.value = location
    }
}
