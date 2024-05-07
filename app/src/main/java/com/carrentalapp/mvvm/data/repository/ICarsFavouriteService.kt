package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.CarsFavourite
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ICarsFavouriteService {
    @POST("favourite")
    fun addCarFavourite(@Body request: CarsFavourite) : Call<CarsFavourite>

    @GET("favourite")
    fun loadCarFavourite(
        @Query("customerId") customerId : String) : Call<List<CarsFavourite>>

    @DELETE("favourite")
    fun removeCarFavourite(
        @Query("carId") favouriteId : String) : Call<CarsFavourite>
}




