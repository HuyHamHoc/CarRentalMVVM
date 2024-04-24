package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.CarsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICarsDetailService {

    @GET("car")
    fun loadCarsDetail(
        @Query ("id") id : String
    ) : Call<List<CarsList>>
}