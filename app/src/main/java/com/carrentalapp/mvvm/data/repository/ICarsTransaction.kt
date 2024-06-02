package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.Transaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ICarsTransaction {
    @POST("transaction")
    fun transaction(@Body request: Transaction): Call<List<Transaction>>

    @GET("transaction")
    fun getTransaction(
        @Query("customerId") customerId : String) : Call<List<Transaction>>
}

