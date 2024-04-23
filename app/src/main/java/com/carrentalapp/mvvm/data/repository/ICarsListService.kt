package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.CarsList
import retrofit2.Call
import retrofit2.http.GET

interface ICarsListService {
    @GET("car")
    fun loadCarsList(): Call<List<CarsList>>
}