package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.data.model.CategoryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICarsCategoryService {

    @GET("car")
    fun loadCarsCategory(
        @Query ("categoryId") categoryId : String
    ) : Call<List<CarsList>>

    @GET("category")
    fun loadCarsCategoryList() : Call<List<CategoryModel>>

}