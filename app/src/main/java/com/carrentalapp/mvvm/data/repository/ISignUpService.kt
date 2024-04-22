package com.carrentalapp.mvvm.data.repository

import com.carrentalapp.mvvm.data.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ISignUpService {

    @POST("customer")
    fun signUp(@Body request: SignUpRequest): Call<SignUpRequest>
}