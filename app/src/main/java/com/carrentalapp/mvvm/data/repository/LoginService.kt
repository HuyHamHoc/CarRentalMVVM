package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

interface LoginService {

    @GET("customer")
    fun login(): Call<List<LoginResponse>>

    companion object {
        fun retrofitService(retrofit: Retrofit): LoginService {
            return retrofit.create(LoginService::class.java)
        }
    }
}