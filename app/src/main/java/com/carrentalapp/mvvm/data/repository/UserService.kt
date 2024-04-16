package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.User
import retrofit2.Retrofit
import retrofit2.http.POST

interface UserService {
//    @GET("customer")
//    fun getCustomer(): Call<User>

    @POST("customer")
    suspend fun getUser(): List<User>

    companion object {
        fun retrofitService(retrofit: Retrofit): UserService {
            return retrofit.create(UserService::class.java)
        }
    }
}