package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.GET

interface ISignInService {

    @GET("customer")
    fun signIn(): Call<List<LoginResponse>>

}