package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.GET

interface ILoginService {

    @GET("customer")
    fun login(): Call<List<LoginResponse>>

}